package com.qp.app_new.adapters;

import android.view.View;
import android.widget.TextView;

import com.qp.app_new.R;

import org.json.JSONObject;

/**
 * Created by Aaron on 17/11/12.
 */

public class RankingAdapter extends BaseMyAdapter<RankingAdapter.ViewHodler> {

    class ViewHodler {
        TextView rangkingTV, nickNameTV, revenueTV;
    }

    @Override
    public int getContentView () {
        return R.layout.item_ranking_list;
    }

    @Override
    public void setConvertView (View convertView, boolean isEmtpy, int position) {
        ViewHodler hodler;
        if (isEmtpy) {
            hodler = new ViewHodler ();
            hodler.rangkingTV = (TextView) convertView.findViewById (R.id.tv_ranking);
            hodler.nickNameTV = (TextView) convertView.findViewById (R.id.tv_nickName);
            hodler.revenueTV = (TextView) convertView.findViewById (R.id.tv_revenue);
            convertView.setTag (hodler);
        } else {
            hodler = (ViewHodler) convertView.getTag ();
        }

        JSONObject jsonObject = getItem (position);
        hodler.rangkingTV.setText ((position + 1) + "");
        hodler.nickNameTV.setText (jsonObject.optString ("nickName"));
        hodler.revenueTV.setText (jsonObject.optString ("revenue"));
    }
}
