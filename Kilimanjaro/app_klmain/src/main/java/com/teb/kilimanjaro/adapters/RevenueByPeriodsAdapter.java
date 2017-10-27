package com.teb.kilimanjaro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.teb.kilimanjaro.App;
import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.models.entry.hall.RevenueByPeriodsModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangbin on 16/7/22.
 */
public class RevenueByPeriodsAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private List<RevenueByPeriodsModel.RevenueByPeriodsData> mList;

    public RevenueByPeriodsAdapter() {
        mList = new ArrayList<>();
        mInflater = (LayoutInflater) App.getAppContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setList(List<RevenueByPeriodsModel.RevenueByPeriodsData> list) {
        mList = (list == null ? new ArrayList<RevenueByPeriodsModel.RevenueByPeriodsData>() : list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public RevenueByPeriodsModel.RevenueByPeriodsData getItem(int position) {
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
            convertView = mInflater.inflate(R.layout.adapter_revenue_byperiods, null);
            holder = new ViewHolder();
            holder.tv_adapter_issue_byperiods = (TextView) convertView.findViewById(R.id.tv_adapter_issue_byperiods);
            holder.tv_adapter_num_byperiods = (TextView) convertView.findViewById(R.id.tv_adapter_num_byperiods);
            holder.tv_adapter_betcoin_byperiods = (TextView) convertView.findViewById(R.id.tv_adapter_betcoin_byperiods);
            holder.tv_adapter_revenue_byperiods = (TextView) convertView.findViewById(R.id.tv_adapter_revenue_byperiods);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        RevenueByPeriodsModel.RevenueByPeriodsData data = getItem(position);
        holder.tv_adapter_issue_byperiods.setText(data.getLotteryId() + "");
        holder.tv_adapter_num_byperiods.setText(data.getLotteryNumber());
        holder.tv_adapter_betcoin_byperiods.setText(data.getBetCoin() + "");
        holder.tv_adapter_revenue_byperiods.setText(data.getRevenue() + "");

        return convertView;
    }

    class ViewHolder {
        TextView tv_adapter_issue_byperiods;
        TextView tv_adapter_num_byperiods;
        TextView tv_adapter_betcoin_byperiods;
        TextView tv_adapter_revenue_byperiods;
    }
}
