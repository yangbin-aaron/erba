package com.teb.kilimanjaro.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.teb.kilimanjaro.App;
import com.teb.kilimanjaro.AppPrefs;
import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.configs.BroadcastConfig;
import com.teb.kilimanjaro.models.entry.hall.LotteryModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangbin on 16/7/7.
 * 实时数据Adapter
 */
public class HallRaelTimeAdapter extends BaseAdapter {

    private List<LotteryModel.LotteryData> mList;
    private LayoutInflater mInflater;
    private static final int OPEN_TIME = 20;// 开奖时间

    private int stoBetSecond = 60;

    public HallRaelTimeAdapter() {
        stoBetSecond = AppPrefs.getInstance().getSelectedGame().getStopBetSecond();
        mInflater = (LayoutInflater) App.getAppContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mList = new ArrayList<>();// 初始化
        mHandler = new Handler();
        mHandler.post(mRunnable);
    }

    private Handler mHandler;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mList.size() > 0) {
                for (int i = 0; i < mList.size(); i++) {
                    LotteryModel.LotteryData data = mList.get(i);
                    int second = data.getLotterySecond() - 1;
                    data.setLotterySecond(second);
                    if (second == stoBetSecond || second == -OPEN_TIME || second == -OPEN_TIME - 10) {
                        Intent intent = new Intent(BroadcastConfig.ACTION_REFRESH_HALLDATA);
                        intent.putExtra("show_dialog", false);
//                        App.getAppContext().sendBroadcast(intent);
                    }
                    mList.set(i, data);
                }
                notifyDataSetChanged();
            }
            mHandler.postDelayed(mRunnable, 1000);
        }
    };

    public void setList(List<LotteryModel.LotteryData> list) {
        mList = (list == null ? new ArrayList<LotteryModel.LotteryData>() : list);
        notifyDataSetChanged();
    }

    public void removeRunnable() {
        mHandler.removeCallbacks(mRunnable);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.adapter_hall_raeltime, null);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.btn_betting = (TextView) convertView.findViewById(R.id.btn_betting);
            holder.tv_betting_count = (TextView) convertView.findViewById(R.id.tv_betting_count);
            holder.tv_cumulative = (TextView) convertView.findViewById(R.id.tv_cumulative);
            holder.tv_issue = (TextView) convertView.findViewById(R.id.tv_issue);
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
        } else {
            holder.tv_betting_count.setBackgroundResource(R.drawable.radio_gray_bg);
        }

        // 点击事件回调方法
        holder.btn_betting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListenerRaelTime.onItemClickRaelTime(v, data);
            }
        });
        holder.tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListenerRaelTime.onItemClickRaelTime(v, data);
            }
        });

        if (data.getLotterySecond() > 0) {
            holder.tv_time.setEnabled(false);
            holder.tv_time.setBackgroundResource(R.drawable.circle_red_bg);
            holder.tv_time.setText((data.getLotterySecond() > 999 ? 999 : data.getLotterySecond()) + "秒");
            if (data.getLotterySecond() <= stoBetSecond) {// 结束投注
                holder.btn_betting.setEnabled(data.getBetNum() > 0);// 有投注才能查看
                holder.btn_betting.setBackgroundResource(
                        data.getBetNum() > 0 ? R.drawable.btn_betting_unselect_bg : R.drawable.radio_gray_bg5);
                holder.btn_betting.setText(R.string.betting_endbet);
            } else {
                holder.btn_betting.setEnabled(true);
                holder.btn_betting.setBackgroundResource(data.getBetNum() > 0 ? R.drawable.btn_betting_selector_bg : R.drawable
                        .btn_login_selector_bg);
                holder.btn_betting.setText(R.string.betting_str);
            }
            if (data.getBetNum() > 0) {
                holder.btn_betting.setText(R.string.betting_hasbet);
                holder.btn_betting.setBackgroundResource(R.drawable.btn_betting_unselect_bg);
            }
        } else {
            holder.btn_betting.setText(R.string.betting_open_com);
            holder.tv_time.setEnabled(true);// 开奖中
            holder.tv_time.setBackgroundResource(R.drawable.circle_orange_bg);
            holder.tv_time.setText(R.string.betting_refresh);
            holder.btn_betting.setEnabled(data.getBetNum() > 0);// 有投注才能查看
            holder.btn_betting.setBackgroundResource(R.drawable.btn_betting_unselect_bg);
        }
        
        
        return convertView;
    }

    class ViewHolder {
        TextView tv_time;
        TextView btn_betting;
        TextView tv_betting_count;// 投注数量
        TextView tv_cumulative;// 奖池
        TextView tv_issue;// 期数
        TextView tv_lottery_time;// 开奖时间
    }

    private OnItemClickListenerRaelTime mListenerRaelTime = null;

    public void setListenerRaelTime(OnItemClickListenerRaelTime listenerRaelTime) {
        mListenerRaelTime = listenerRaelTime;
    }

    public interface OnItemClickListenerRaelTime {
        void onItemClickRaelTime(View view, LotteryModel.LotteryData data);
    }
}
