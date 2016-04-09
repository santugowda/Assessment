package com.example.santhosh.assessment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private File file;
    private List<String> fileList;
    ArrayList<File> listFiles = new ArrayList<File>();
    private ListView listView;
    private ListAdapter listAdapter;
    ArrayAdapter<String> arrayAdapter;
    private ProgressDialog m_ProgressDialog = null;
    List<Long> listValues = new ArrayList<>();
    Boolean flagGetScanningResult = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fileList =  new ArrayList<String>();
        file =  new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        m_ProgressDialog = ProgressDialog.show(this, "Progress Update",
                "Loading Data", true);
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                flagGetScanningResult = true;
                fileList = getFileListfromSDCard();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        m_ProgressDialog.dismiss();
                    }
                });
            }
        }).start();

        listView = (ListView)findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                fileList);
        //fileList = getFileListfromSDCard();
        //listAdapter = new ListAdapter(this, R.layout.list_item,fileList);
        listView.setAdapter(arrayAdapter);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_big_files) {
            fileList = getLargerFiles();
            arrayAdapter.clear();
            arrayAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1,
                    fileList);
            listView.setAdapter(arrayAdapter);
            return true;
        }
        if (id == R.id.action_frequent_file) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*This override method will handle
     When app is stopped by the user (by pressing BACK button), the scan must be stopped immediately.*/

    @Override
    public void onBackPressed() {
        if(flagGetScanningResult){
            flagGetScanningResult = false;
        }
        super.onBackPressed();
    }

    /*This override method will handle
     When app is sent to background (by pressing HOME button), the scan should continue */

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_HOME) {
            getFileListfromSDCard();
            return true;
        }else {
            return false;
        }
    }


    /*Method to get the complete list of files available
        on SD Card along with the size of the file*/
    public List<String> getFileListfromSDCard(){
        String state = Environment.getExternalStorageState();
        List<String> flLst = new ArrayList<String>();

        if (Environment.MEDIA_MOUNTED.equals(state) && file.isDirectory()) {
            File[] fileArr = file.listFiles();
            int length = fileArr.length;
            for (int i = 0; i < length; i++) {
                File f = fileArr[i];
                flLst.add(f.getName() + " " + FileSize(f));
                listValues.add(FileSize(f));
            }
        }
        return flLst;
    }

    /* Method used to get different file extension */
    public ArrayList<File> getfile(File dir,String fileType){
        File listfl[] = dir.listFiles();
        if (listfl != null && listfl.length > 0) {
            for (int i = 0; i < listfl.length; i++) {
                if (listfl[i].isDirectory()) {
                    getfile(listfl[i],fileType);
                }
                else {
                    if("doc".equals(fileType)) {
                        if(listfl[i].getName().endsWith(".pdf") || listfl[i].getName().endsWith(".txt") ||
                                listfl[i].getName().endsWith(".xml") || listfl[i].getName().endsWith(".doc") ||
                                listfl[i].getName().endsWith(".xls") || listfl[i].getName().endsWith(".xlsx")) {
                            listFiles.add(listfl[i]);
                        }
                    }
                    else if("music".equals(fileType)) {
                        if(listfl[i].getName().endsWith(".mp3")) {
                            listFiles.add(listfl[i]);
                        }
                    }
                    else if("video".equals(fileType)) {
                        if(listfl[i].getName().endsWith(".mp4")) {
                            listFiles.add(listfl[i]);
                        }
                    }
                    else if("image".equals(fileType)) {
                        if(listfl[i].getName().endsWith(".png") || listfl[i].getName().endsWith(".jpg")
                                || listfl[i].getName().endsWith(".jpeg") || listfl[i].getName().endsWith(".gif")) {
                            listFiles.add(listfl[i]);
                        }
                    }
                }
            }
        }
        return listFiles;
    }


    /* In this method is to calculate Average file size of the file */
    private static long FileSize(File dir) {
        long result = 0;
        Stack<File> dirlist= new Stack<File>();
        dirlist.clear();
        dirlist.push(dir);
        while(!dirlist.isEmpty()) {
            File dirCurrent = dirlist.pop();
            File[] fileList = dirCurrent.listFiles();
            for(File f: fileList){
                if(f.isDirectory())
                    dirlist.push(f);
                else
                    result += f.length();
            }
        }
        return result;
    }

    /* In this method, we sort the files depending on
        the size of the file get the associated file name*/
    public List<String> getLargerFiles(){
        Collections.sort(listValues);
        int nameSize[] = new int[10];
        File[] nameArr = file.listFiles();
        for (int i = 0; i < nameSize.length; i++){
            fileList.add(nameArr[i].getName());
        }
        return fileList;
    }

}
