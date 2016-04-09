package com.example.santhosh.assessment;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by santhosh on 4/8/16.
 */
public class ListAdapter extends ArrayAdapter<String> {

    private List<String> list;
    private Context mContext;

    public ListAdapter(Context context, int textViewResourceId,
                     List<String> fileList) {
        super(context,textViewResourceId,fileList);
        this.list = list;
        context = mContext;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        Holder holder = null;

        if (convertView == null) {
            view = View.inflate(mContext, R.layout.list_item, null);
            holder = new Holder();
            holder.contentView = (TextView) view.findViewById(R.id.list_text);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        String fileName = list.get(position);
        holder.contentView.setText(fileName);
        return view;
    }
}

    class Holder {
    public TextView contentView;
}

