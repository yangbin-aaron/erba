package com.teb.kilimanjaro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.teb.kilimanjaro.App;
import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.models.entry.hall.LotteryModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangbin on 16/7/7.
 */
public class HallHistoryAdapter extends BaseAdapter {

    private List<LotteryModel.LotteryData> mList;
    private LayoutInflater mInflater;

    public HallHistoryAdapter() {
        mInflater = (LayoutInflater) App.getAppContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mList = new ArrayList<>();// 初始化
    }

    public void setList(List<LotteryModel.LotteryData> list) {
        mList = (list == null ? new ArrayList<LotteryModel.LotteryData>() : list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public LotteryModel.LotteryData getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.adapter_hall_history, null);
            holder.hallRL = (RelativeLayout) convertView.findViewById(R.id.rl_adapter_hall);
            holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
            holder.tv_betting_count = (TextView) convertView.findViewById(R.id.tv_betting_count);
            holder.tv_cumulative = (TextView) convertView.findViewById(R.id.tv_cumulative);
            holder.tv_issue = (TextView) convertView.findViewById(R.id.tv_issue);
            holder.tv_revenue = (TextView) convertView.findViewById(R.id.tv_revenue);
            holder.tv_lottery_time = (TextView) convertView.findViewById(R.id.tv_lottery_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final LotteryModel.LotteryData data = getItem(position);
        holder.tv_lottery_time.setText(data.getLotteryTime());
        holder.tv_issue.setText(data.getLotteryId() + "");
        holder.tv_cumulative.setText(data.getCumulative());
        holder.tv_betting_count.setText(data.getBetNum() + "");
        if (data.getBetNum() > 0) {
            holder.tv_betting_count.setBackgroundResource(R.drawable.radio_orange_bg);
            holder.tv_revenue.setText(App.getAppContext().getResources().getString(R.string.revenue)
                            + "  " + data.getPureProfit()
//                    + "  (" + data.getIncomeRate() + ")"
            );
            holder.tv_revenue.setTextColor(App.getAppContext().getResources().getColor(Double.parseDouble(data.getPureProfit()) > 0 ? R.color.red_text : R.color.green));
            convertView.findViewById(R.id.tv_revenue).setVisibility(View.VISIBLE);
        } else {
            holder.tv_betting_count.setBackgroundResource(R.drawable.radio_gray_bg);
            convertView.findViewById(R.id.tv_revenue).setVisibility(View.GONE);
        }

        // 设置监听事件
        holder.hallRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListenerHistory.onItemClickHistory(data);
            }
        });
        holder.tv_num.setText(data.getLotteryResult() + "");
        return convertView;
    }

    class ViewHolder {
        RelativeLayout hallRL;// 布局
        TextView tv_num;// 开奖号码
        TextView tv_betting_count;// 投注数量
        TextView tv_cumulative;// 奖池
        TextView tv_issue;// 期数
        TextView tv_revenue;// 亏盈
        TextView tv_lottery_time;// 开奖时间
    }

    private OnItemClickListenerHistory mListenerHistory = null;

    public void setListenerHistory(OnItemClickListenerHistory listenerHistory) {
        mListenerHistory = listenerHistory;
    }

    public interface OnItemClickListenerHistory {
        void onItemClickHistory(LotteryModel.LotteryData data);
    }
}
