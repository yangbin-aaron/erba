package com.qp.app_new.adapters;

import android.view.View;
import android.widget.TextView;

import com.qp.app_new.R;

import org.json.JSONObject;

/**
 * Created by Aaron on 2017/11/20.
 */

public class RevenueByDaysAdapter extends BaseMyAdapter<RevenueByDaysAdapter.ViewHolder> {

    @Override
    public int getContentView() {
        return R.layout.item_revenue_bydays;
    }

    @Override
    public void setConvertView(View convertView, boolean isEmtpy, int position) {
        ViewHolder holder;
        if (isEmtpy) {
            holder = new ViewHolder();
            holder.tv_adapter_time_bydays = (TextView) convertView.findViewById(R.id.tv_adapter_time_bydays);
            holder.tv_adapter_revenue_bydays = (TextView) convertView.findViewById(R.id.tv_adapter_revenue_bydays);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        JSONObject jsonObject = getItem(position);
        holder.tv_adapter_time_bydays.setText(jsonObject.optString("date"));
        holder.tv_adapter_revenue_bydays.setText(jsonObject.optString("revenue"));
    }

    class ViewHolder {
        TextView tv_adapter_time_bydays;
        TextView tv_adapter_revenue_bydays;
    }
}
