package com.qp.app_new.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.qp.app_new.App;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Aaron on 17/11/12.
 */

public abstract class BaseMyAdapter<T> extends BaseAdapter {

    public JSONArray mJSONArray = new JSONArray ();
    private LayoutInflater mInflater;

    public void setJSONArray (JSONArray jsonArray) {
        mJSONArray = jsonArray;
        notifyDataSetChanged ();
    }


    @Override
    public int getCount () {
        return mJSONArray.length ();
    }

    @Override
    public JSONObject getItem (int position) {
        return mJSONArray.optJSONObject (position);
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    public abstract int getContentView ();

    public abstract void setConvertView (View convertView, boolean isEmtpy, int position);

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        if (mInflater == null) mInflater = LayoutInflater.from (App.mContext);
        boolean isnull = convertView == null;
        if (isnull) {
            convertView = mInflater.inflate (getContentView (), null);
        }
        setConvertView (convertView, isnull, position);
        return convertView;
    }
}
