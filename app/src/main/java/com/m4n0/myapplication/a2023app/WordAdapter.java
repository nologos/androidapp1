package com.m4n0.myapplication.a2023app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class WordAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mWords;

    public WordAdapter(Context context, List<String> words) {
        mContext = context;
        mWords = words;
    }

    @Override
    public int getCount() {
        return mWords.size();
    }

    @Override
    public String getItem(int position) {
        return mWords.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.gridItemTextView);
        textView.setText(getItem(position));

        return convertView;
    }
}
