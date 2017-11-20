package com.qp.app_new.adapters;

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
    public int getContentView() {
        return R.layout.item_actual_odds;
    }

    @Override
    public void setConvertView(View convertView, boolean isEmtpy, int position) {
        ViewHolder holder;
        if (isEmtpy) {
            holder = new ViewHolder();
            holder.tv_bet_mode = (TextView) convertView.findViewById(R.id.tv_bet_mode);
            holder.odds_tv = (TextView) convertView.findViewById(R.id.odds_tv);
            holder.tv_bet_nums = (TextView) convertView.findViewById(R.id.tv_bet_nums);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        JSONObject jsonObject = getItem(position);
        holder.tv_bet_mode.setText(jsonObject.optString("patternName"));
        holder.odds_tv.setText(App.mContext.getString(R.string.lottery_d_odds, jsonObject.optString("odds")));
        holder.tv_bet_nums.setText(jsonObject.optString("includeNum"));
    }

    class ViewHolder {
        TextView tv_bet_mode, odds_tv, tv_bet_nums;
    }
}
