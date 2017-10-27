package com.teb.kilimanjaro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.teb.kilimanjaro.App;
import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.models.entry.coin.WithdrawDetailModel.RecordsData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangbin on 16/8/18.
 */
public class WithdrawDetailAdapter extends BaseAdapter {
    private List<RecordsData> mList;
    private LayoutInflater mInflater;

    public WithdrawDetailAdapter() {
        mList = new ArrayList<>();
        mInflater = (LayoutInflater) App.getAppContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setList(List<RecordsData> list) {
        mList = (list == null ? new ArrayList<RecordsData>() : list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public RecordsData getItem(int position) {
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
            convertView = mInflater.inflate(R.layout.adapter_withdrawdetail, null);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_desc = (TextView) convertView.findViewById(R.id.tv_desc);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        RecordsData data = getItem(position);
        holder.tv_time.setText(data.getCreateTime());
        holder.tv_desc.setText(data.getDesc());
        return convertView;
    }

    class ViewHolder {
        TextView tv_time, tv_desc;
    }
}
