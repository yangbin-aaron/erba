package com.qp.app_new.adapters;

import android.view.View;
import android.widget.TextView;

import com.qp.app_new.R;

import org.json.JSONObject;

/**
 * Created by Aaron on 2017/11/20.
 */

public class RevenueByPeriodsAdapter extends BaseMyAdapter<RevenueByPeriodsAdapter.ViewHolder> {


    @Override
    public int getContentView() {
        return R.layout.item_revenue_byperiods;
    }

    @Override
    public void setConvertView(View convertView, boolean isEmtpy, int position) {
        ViewHolder holder;
        if (isEmtpy) {
            holder = new ViewHolder();
            holder.tv_adapter_issue_byperiods = (TextView) convertView.findViewById(R.id.tv_adapter_issue_byperiods);
            holder.tv_adapter_num_byperiods = (TextView) convertView.findViewById(R.id.tv_adapter_num_byperiods);
            holder.tv_adapter_betcoin_byperiods = (TextView) convertView.findViewById(R.id.tv_adapter_betcoin_byperiods);
            holder.tv_adapter_revenue_byperiods = (TextView) convertView.findViewById(R.id.tv_adapter_revenue_byperiods);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        JSONObject jsonObject = getItem(position);
        holder.tv_adapter_issue_byperiods.setText(jsonObject.optString("lotteryId"));
        holder.tv_adapter_num_byperiods.setText(jsonObject.optString("lotteryNumber"));
        holder.tv_adapter_betcoin_byperiods.setText(jsonObject.optString("betCoin"));
        holder.tv_adapter_revenue_byperiods.setText(jsonObject.optString("revenue"));
    }

    class ViewHolder {
        TextView tv_adapter_issue_byperiods;
        TextView tv_adapter_num_byperiods;
        TextView tv_adapter_betcoin_byperiods;
        TextView tv_adapter_revenue_byperiods;
    }
}
