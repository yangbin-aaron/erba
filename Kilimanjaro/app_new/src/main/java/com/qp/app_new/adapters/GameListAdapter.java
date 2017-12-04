package com.qp.app_new.adapters;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.qp.app_new.R;
import com.qp.app_new.views.CircleImageView;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Aaron on 2017/11/8.
 */

public class GameListAdapter extends BaseMyAdapter<GameListAdapter.ViewHodler> {

    @Override
    public void setJSONArray(JSONArray jsonArray) {
        mJSONArray = null;
        mJSONArray = new JSONArray();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            if (jsonObject.optInt("visible") == 1) {
                mJSONArray.put(jsonObject);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getContentView() {
        return R.layout.item_game_list;
    }

    @Override
    public void setConvertView(View convertView, boolean isEmtpy, int position) {
        ViewHodler hodler;
        if (isEmtpy) {
            hodler = new ViewHodler();
            hodler.iconIV = (CircleImageView) convertView.findViewById(R.id.iv_icon);
            hodler.gameNameTV = (TextView) convertView.findViewById(R.id.tv_gamename);
            hodler.referNameTV = (TextView) convertView.findViewById(R.id.tv_referName);
            convertView.setTag(hodler);
        } else {
            hodler = (ViewHodler) convertView.getTag();
        }

        JSONObject jsonObject = mJSONArray.optJSONObject(position);
        String gameName = jsonObject.optString("gameName", "数据缺失");
        hodler.gameNameTV.setText(gameName);
        String referName = jsonObject.optString("referName", "数据缺失");
        hodler.referNameTV.setText(referName);
        String icon = jsonObject.optString("icon");
        if (!TextUtils.isEmpty(icon)) {
            ImageAware imageAware = new ImageViewAware(hodler.iconIV, false);
            hodler.iconIV.setTag(icon);
            ImageLoader.getInstance().displayImage(icon, imageAware);
        }
    }

    class ViewHodler {
        CircleImageView iconIV;
        TextView gameNameTV, referNameTV;
    }
}
