package com.teb.kilimanjaro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.teb.kilimanjaro.App;
import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.models.entry.hall.RevenueByDaysModel.RevenueByDaysData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangbin on 16/7/22.
 */
public class RevenueByDaysAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private List<RevenueByDaysData> mList;

    public RevenueByDaysAdapter() {
        mList = new ArrayList<>();
        mInflater = (LayoutInflater) App.getAppContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setList(List<RevenueByDaysData> list) {
        mList = (list == null ? new ArrayList<RevenueByDaysData>() : list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public RevenueByDaysData getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_revenue_bydays, null);
            holder = new ViewHolder();
            holder.tv_adapter_time_bydays = (TextView) convertView.findViewById(R.id.tv_adapter_time_bydays);
            holder.tv_adapter_revenue_bydays = (TextView) convertView.findViewById(R.id.tv_adapter_revenue_bydays);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        RevenueByDaysData data = getItem(position);
        holder.tv_adapter_time_bydays.setText(data.getDate());
        holder.tv_adapter_revenue_bydays.setText(data.getRevenue() + "");

        return convertView;
    }

    class ViewHolder {
        TextView tv_adapter_time_bydays;
        TextView tv_adapter_revenue_bydays;
    }
}
