package com.qp.app_new.adapters;

import android.view.View;
import android.widget.TextView;

import com.qp.app_new.App;
import com.qp.app_new.R;

import org.json.JSONObject;

/**
 * Created by Aaron on 2017/11/21.
 */

public class TrandBetAdapter extends BaseMyAdapter<TrandBetAdapter.ViewHolder> {

    @Override
    public int getContentView() {
        return R.layout.item_trand_bet;
    }

    @Override
    public void setConvertView(View convertView, boolean isEmtpy, int position) {
        ViewHolder holder;
        if (isEmtpy) {
            holder = new ViewHolder();
            holder.tv_no = (TextView) convertView.findViewById(R.id.tv_no);
            holder.tv_qh = (TextView) convertView.findViewById(R.id.tv_qh);
            holder.tv_betcoin = (TextView) convertView.findViewById(R.id.tv_betcoin);
            holder.tv_revenue = (TextView) convertView.findViewById(R.id.tv_revenue);
            holder.tv_coin = (TextView) convertView.findViewById(R.id.tv_coin);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        JSONObject jsonObject = getItem(position);
        holder.tv_no.setText("No. " + jsonObject.optString("profitNo"));
        holder.tv_qh.setText(jsonObject.optString("gameName") + App.mContext.getString(R.string.item_game_qh_str, jsonObject.optString("lotteryId")));
        long betCoin = jsonObject.optLong("betCoin");
        long revenue = jsonObject.optLong("revenue");
        long coin = revenue - betCoin;
        holder.tv_betcoin.setText(App.mContext.getString(R.string.item_betcoin_str, betCoin));
        holder.tv_revenue.setText(App.mContext.getString(R.string.item_revenue_str, revenue));
        if (coin > 0) {
            holder.tv_coin.setTextColor(App.mContext.getResources().getColor(R.color.orange_text));
            holder.tv_coin.setText("+" + coin);
        } else {
            holder.tv_coin.setTextColor(App.mContext.getResources().getColor(R.color.black_text));
            holder.tv_coin.setText(String.valueOf(coin));
        }
    }

    class ViewHolder {
        TextView tv_no, tv_qh, tv_betcoin, tv_revenue, tv_coin;
    }
}
