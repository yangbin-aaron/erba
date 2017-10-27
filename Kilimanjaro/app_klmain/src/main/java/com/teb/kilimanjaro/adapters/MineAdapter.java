package com.teb.kilimanjaro.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by yangbin on 16/7/1.
 * “我的”界面里面listView的适配器
 */
public class MineAdapter extends BaseAdapter {


    public MineAdapter() {
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public View getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return convertView;
    }
}
