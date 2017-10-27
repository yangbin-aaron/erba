package com.teb.kilimanjaro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.teb.kilimanjaro.App;
import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.models.entry.coin.RechargeListModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangbin on 16/8/12.
 */
public class RechargeListViewAdapter extends BaseAdapter {

    private List<RechargeListModel.RechargeData> mList;
    private LayoutInflater mInflater;

    public RechargeListViewAdapter() {
        mInflater = (LayoutInflater) App.getAppContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mList = new ArrayList<>();
    }

    public void setList(List<RechargeListModel.RechargeData> list) {
        mList = (list == null ? new ArrayList<RechargeListModel.RechargeData>() : list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public RechargeListModel.RechargeData getItem(int position) {
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
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.adapter_recharge_list, null);
            holder.tv_coin = (TextView) convertView.findViewById(R.id.tv_coin);
            holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        RechargeListModel.RechargeData rechargeData = getItem(position);
        holder.tv_type.setText(rechargeData.getDesc());
        holder.tv_coin.setText(rechargeData.getCoin());
        holder.tv_time.setText(rechargeData.getCreateTime());
        return convertView;
    }

    class ViewHolder {
        TextView tv_coin, tv_type, tv_time;
    }
}
