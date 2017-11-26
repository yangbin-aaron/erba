package com.qp.app_new.adapters;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.qp.app_new.App;
import com.qp.app_new.R;

import org.json.JSONObject;

/**
 * Created by Aaron on 2017/11/20.
 */

public class ActualOddsAdapter extends BaseMyAdapter<ActualOddsAdapter.ViewHolder> {

    @Override
    public int getContentView () {
        return R.layout.item_actual_odds;
    }

    @Override
    public void setConvertView (View convertView, boolean isEmtpy, int position) {
        ViewHolder holder;
        if (isEmtpy) {
            holder = new ViewHolder ();
            holder.tv_bet_mode = (TextView) convertView.findViewById (R.id.tv_bet_mode);
            holder.odds_tv = (TextView) convertView.findViewById (R.id.odds_tv);
            holder.tv_bet_nums = (TextView) convertView.findViewById (R.id.tv_bet_nums);
            holder.tv_bet_hint = (TextView) convertView.findViewById (R.id.tv_bet_hint);
            convertView.setTag (holder);
        } else {
            holder = (ViewHolder) convertView.getTag ();
        }

        JSONObject jsonObject = getItem (position);
        holder.odds_tv.setText (App.mContext.getString (R.string.lottery_d_odds, jsonObject.optString ("odds")));
        holder.tv_bet_nums.setText (jsonObject.optString ("includeNum"));

        String patternName = jsonObject.optString ("patternName");
        holder.tv_bet_mode.setText (patternName);
        long balanceCoin = jsonObject.optLong ("balanceCoin");
        String greaterSpecialOdds = jsonObject.optString ("greaterSpecialOdds");

        String hint = "";
        switch (patternName) {
            case "大双":
            case "大":
            case "双":
                hint = "14";
                break;
            case "小单":
            case "小":
            case "单":
                hint = "13";
                break;
            default:
                hint = "";
        }
        if (!TextUtils.isEmpty (hint)) {
            hint = "开奖号码为" + hint;
            if (balanceCoin > 0) {
                hint += "，且当期投注额>" + balanceCoin;
            }
            hint += "时，赔率为" + greaterSpecialOdds + "倍";
        }
        holder.tv_bet_hint.setText (hint);
    }

    class ViewHolder {
        TextView tv_bet_mode, odds_tv, tv_bet_nums, tv_bet_hint;
    }
}
