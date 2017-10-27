package com.teb.kilimanjaro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teb.kilimanjaro.App;
import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.models.entry.qq.BettingListModelQQ.BettingListData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toughegg on 16/8/2.
 */
public class BettingListAdapterQQ extends BaseAdapter {

    private List<BettingListData> mList;
    private LayoutInflater mInflater;

    public BettingListAdapterQQ() {
        mList = new ArrayList<>();
        mInflater = (LayoutInflater) App.getAppContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setList(List<BettingListData> list) {
        mList = (list == null ? new ArrayList<BettingListData>() : list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public BettingListData getItem(int position) {
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
            convertView = mInflater.inflate(R.layout.adapter_qq_betting_list, null);
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

        BettingListData data = getItem(position);
        holder.tv_order_no.setText(data.getOrderNo());
        holder.tv_bet_mode.setText(data.getPatternName());
        holder.tv_bet_coin.setText(data.getBetCoin());
        holder.tv_bet_nums.setText(data.getBetNum());
        holder.tv_bet_time.setText(data.getCreateTime());

        if (data.getRevenue() == null || data.getRevenue().equals("")) {
            holder.ll_revenue.setVisibility(View.INVISIBLE);
        } else {
            holder.ll_revenue.setVisibility(View.VISIBLE);
            holder.tv_revenue.setText(data.getRevenue());
            holder.tv_revenue.setTextColor(
                    App.getAppContext().getResources().getColor(
                            Double.parseDouble(data.getRevenue()) > 0 ? R.color.red_text : R.color.green));
        }

        return convertView;
    }

    class ViewHolder {
        TextView tv_bet_mode, tv_order_no, tv_bet_coin, tv_revenue, tv_bet_nums, tv_bet_time;
        LinearLayout ll_revenue;
    }
}
