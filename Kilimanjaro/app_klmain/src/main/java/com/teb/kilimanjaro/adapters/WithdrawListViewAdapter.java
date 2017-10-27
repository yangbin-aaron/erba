package com.teb.kilimanjaro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.teb.kilimanjaro.App;
import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.models.entry.coin.WithdrawListModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangbin on 16/8/12.
 */
public class WithdrawListViewAdapter extends BaseAdapter {

    private List<WithdrawListModel.WithdrawData> mList;
    private LayoutInflater mInflater;

    public WithdrawListViewAdapter() {
        mInflater = (LayoutInflater) App.getAppContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mList = new ArrayList<>();
    }

    public void setList(List<WithdrawListModel.WithdrawData> list) {
        mList = (list == null ? new ArrayList<WithdrawListModel.WithdrawData>() : list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public WithdrawListModel.WithdrawData getItem(int position) {
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
            convertView = mInflater.inflate(R.layout.adapter_withdraw_list, null);
            holder.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
            holder.tv_drawAmt = (TextView) convertView.findViewById(R.id.tv_drawAmt);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        WithdrawListModel.WithdrawData withdrawData = getItem(position);
        holder.tv_drawAmt.setText(withdrawData.getDrawAmt() + "元");
        holder.tv_time.setText(withdrawData.getCreateTime());
        switch (withdrawData.getStatus()) {
            case -1:
                holder.tv_status.setText("待处理");
                holder.tv_status.setTextColor(App.getAppContext().getResources().getColor(R.color.gray_xx));
                break;
            case -2:
                holder.tv_status.setText("处理中");
                holder.tv_status.setTextColor(App.getAppContext().getResources().getColor(R.color.gray_xx));
                break;
            case -4:
                holder.tv_status.setText("提现成功");
                holder.tv_status.setTextColor(App.getAppContext().getResources().getColor(R.color.green));
                break;
            case -3:
            case -5:
            case -6:
                holder.tv_status.setText("提现失败");
                holder.tv_status.setTextColor(App.getAppContext().getResources().getColor(R.color.red_text));
                break;
        }
        holder.tv_drawAmt.setText(withdrawData.getDrawAmt());
        return convertView;
    }

    class ViewHolder {
        TextView tv_status, tv_drawAmt, tv_time;
    }
}
