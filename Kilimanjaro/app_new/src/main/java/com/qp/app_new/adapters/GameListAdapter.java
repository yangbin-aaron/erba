package com.qp.app_new.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.qp.app_new.App;
import com.qp.app_new.R;
import com.qp.app_new.views.CircleImageView;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Aaron on 2017/11/8.
 */

public class GameListAdapter extends BaseAdapter {

    private JSONArray mJSONArray;
    private LayoutInflater mInflater;

    public GameListAdapter () {
        mJSONArray = new JSONArray ();
        mInflater = LayoutInflater.from (App.mContext);
    }

    public void setJSONArray (JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length (); i++) {
            JSONObject jsonObject = jsonArray.optJSONObject (i);
            if (jsonObject.optInt ("id") != 3 && jsonObject.optInt ("visible") == 1) {// 去掉模拟场数据
                mJSONArray.put (jsonObject);
            }
        }
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

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        ViewHodler hodler;
        if (convertView == null) {
            hodler = new ViewHodler ();
            convertView = mInflater.inflate (R.layout.item_game_list, null);
            hodler.iconIV = (CircleImageView) convertView.findViewById (R.id.iv_icon);
            hodler.gameNameTV = (TextView) convertView.findViewById (R.id.tv_gamename);
            hodler.referNameTV = (TextView) convertView.findViewById (R.id.tv_referName);
            convertView.setTag (hodler);
        } else {
            hodler = (ViewHodler) convertView.getTag ();
        }

        JSONObject jsonObject = mJSONArray.optJSONObject (position);
        String gameName = jsonObject.optString ("gameName", "数据缺失");
        hodler.gameNameTV.setText (gameName);
        String referName = jsonObject.optString ("referName", "数据缺失");
        hodler.referNameTV.setText (referName);
        String icon = jsonObject.optString ("icon");
        if (!TextUtils.isEmpty (icon)) {
            ImageAware imageAware = new ImageViewAware (hodler.iconIV, false);
            hodler.iconIV.setTag (icon);
            ImageLoader.getInstance ().displayImage (icon, imageAware);
        }
        return convertView;
    }

    class ViewHodler {
        CircleImageView iconIV;
        TextView gameNameTV, referNameTV;
    }
}
