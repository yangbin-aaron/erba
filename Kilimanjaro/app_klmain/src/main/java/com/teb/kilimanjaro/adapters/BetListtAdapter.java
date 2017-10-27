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
import com.teb.kilimanjaro.models.entry.hall.OddsListModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangbin on 16/7/13.
 */
public class BetListtAdapter extends BaseAdapter {

    private List<OddsListModel.OddsData> mList;

    private LayoutInflater mInflater;

    public BetListtAdapter() {
        mInflater = (LayoutInflater) App.getAppContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mList = new ArrayList<>();
    }

    public void setList(List<OddsListModel.OddsData> list) {
        mList = (list == null ? new ArrayList<OddsListModel.OddsData>() : list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public OddsListModel.OddsData getItem(int position) {
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
            convertView = mInflater.inflate(R.layout.adapter_bet_list, null);
            holder = new ViewHolder();
            holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
            holder.tv_odds = (TextView) convertView.findViewById(R.id.tv_odds);
            holder.tv_times_01 = (TextView) convertView.findViewById(R.id.tv_times_01);
            holder.tv_times_05 = (TextView) convertView.findViewById(R.id.tv_times_05);
            holder.tv_times_10 = (TextView) convertView.findViewById(R.id.tv_times_10);
            holder.tv_bet_coin = (TextView) convertView.findViewById(R.id.tv_bet_coin);
            holder.et_bet_coin = (LinearLayout) convertView.findViewById(R.id.et_bet_coin);
            holder.view_gb = convertView.findViewById(R.id.view_gb);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final OddsListModel.OddsData data = getItem(position);
        holder.tv_num.setText(data.getLotteryNumber() + "");
        holder.tv_odds.setText(data.getOdds() + "");

        holder.tv_bet_coin.setText(data.getBetCoin() + "");
        holder.view_gb.setVisibility(data.isFocus() ? View.VISIBLE : View.INVISIBLE);
        holder.tv_num.setBackgroundResource(data.isSelected() ? R.drawable.circle_red_bg : R.drawable.circle_orange_bg);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tv_num:
                        mOnBetCoinItemClickListener.onNumItemClickListener(position);
                        break;
                    case R.id.et_bet_coin:
                        mOnBetCoinItemClickListener.onBetCoinItemClickListener(position);
                        break;
                    case R.id.tv_times_01:
                        mOnBetCoinItemClickListener.onTimesItemClickListener(position, 0.1f);
                        break;
                    case R.id.tv_times_05:
                        mOnBetCoinItemClickListener.onTimesItemClickListener(position, 0.5f);
                        break;
                    case R.id.tv_times_10:
                        mOnBetCoinItemClickListener.onTimesItemClickListener(position, 10);
                        break;
                }
            }
        };

        holder.et_bet_coin.setOnClickListener(listener);
        holder.tv_times_01.setOnClickListener(listener);
        holder.tv_times_05.setOnClickListener(listener);
        holder.tv_times_10.setOnClickListener(listener);
        holder.tv_num.setOnClickListener(listener);

        return convertView;
    }

    private OnBetCoinItemClickListener mOnBetCoinItemClickListener;

    public void setOnBetCoinItemClickListener(OnBetCoinItemClickListener onBetCoinItemClickListener) {
        mOnBetCoinItemClickListener = onBetCoinItemClickListener;
    }

    public interface OnBetCoinItemClickListener {
        /**
         * 获取输入框的焦点
         *
         * @param position
         */
        void onBetCoinItemClickListener(int position);

        /**
         * 设置单个号码的倍数
         *
         * @param position
         * @param times
         */
        void onTimesItemClickListener(int position, float times);

        /**
         * 点击数字回调
         *
         * @param position
         */
        void onNumItemClickListener(int position);
    }

    class ViewHolder {
        TextView tv_num;
        TextView tv_odds;
        TextView tv_times_01;
        TextView tv_times_05;
        TextView tv_times_10;
        LinearLayout et_bet_coin;// 金额
        TextView tv_bet_coin;// 金额
        View view_gb;// 光标
    }
}
