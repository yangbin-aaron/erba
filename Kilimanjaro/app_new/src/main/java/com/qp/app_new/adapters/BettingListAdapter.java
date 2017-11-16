package com.qp.app_new.adapters;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qp.app_new.App;
import com.qp.app_new.R;
import com.qp.app_new.utils.StringUtil;

import org.json.JSONObject;

/**
 * Created by toughegg on 16/8/2.
 */
public class BettingListAdapter extends BaseMyAdapter<BettingListAdapter.ViewHolder> {

    class ViewHolder {
        TextView tv_bet_mode, tv_order_no, tv_bet_coin, tv_revenue, tv_bet_nums, tv_bet_time;
        LinearLayout ll_revenue;
    }

    @Override
    public int getContentView() {
        return R.layout.item_betting_list;
    }

    @Override
    public void setConvertView(View convertView, boolean isEmtpy, int position) {
        ViewHolder holder;
        if (isEmtpy) {
            holder = new ViewHolder();
            holder.tv_bet_mode = (TextView) convertView.findViewById(R.id.tv_bet_mode);
            holder.tv_order_no = (TextView) convertView.findViewById(R.id.tv_order_no);
            holder.tv_bet_coin = (TextView) convertView.findViewById(R.id.tv_bet_coin);
            holder.ll_revenue = (LinearLayout) convertView.findViewById(R.id.ll_revenue);
            holder.tv_revenue = (TextView) convertView.findViewById(R.id.tv_revenue);
            holder.tv_bet_nums = (TextView) convertView.findViewById(R.id.tv_bet_nums);
            holder.tv_bet_time = (TextView) convertView.findViewById(R.id.tv_bet_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        JSONObject jsonObject = getItem(position);
        holder.tv_order_no.setText(jsonObject.optString("orderNo"));
        holder.tv_bet_mode.setText(jsonObject.optString("patternName"));
        holder.tv_bet_coin.setText(jsonObject.optString("betCoin"));
        holder.tv_bet_nums.setText(jsonObject.optString("betNum"));
        holder.tv_bet_time.setText(StringUtil.getShowTime14(jsonObject.optString("createTime")));

        if (TextUtils.isEmpty(jsonObject.optString("revenue")) || jsonObject.optString("revenue").equals("null")) {
            holder.ll_revenue.setVisibility(View.INVISIBLE);
        } else {
            holder.ll_revenue.setVisibility(View.VISIBLE);
            holder.tv_revenue.setText(jsonObject.optString("revenue"));
            holder.tv_revenue.setTextColor(
                    App.mContext.getResources().getColor(
                            jsonObject.optInt("revenue") > 0 ? R.color.red_text : R.color.green));
        }
    }
}
