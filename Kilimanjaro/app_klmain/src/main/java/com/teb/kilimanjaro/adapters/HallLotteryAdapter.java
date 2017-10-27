package com.teb.kilimanjaro.adapters;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.teb.kilimanjaro.App;
import com.teb.kilimanjaro.AppPrefs;
import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.models.entry.hall.GameListModel;
import com.teb.kilimanjaro.models.entry.hall.LotteryModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangbin on 16/9/3.
 */
public class HallLotteryAdapter extends BaseAdapter {

    private List<LotteryModel.LotteryData> mList;
    private List<LotteryModel.LotteryData> mNoList;
    private List<LotteryModel.LotteryData> mHadList;
    private int mNoLotteryCount = 0;// 未开奖的数据数量

    private LayoutInflater mInflater;

    private int stoBetSecond = 60;
    private static final int OPEN_TIME = 15;// 开奖时间

    private Handler mHandler;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mNoLotteryCount > 0) {// 有未开奖数据
                for (int i = 0; i < mNoList.size(); i++) {
                    LotteryModel.LotteryData data = mList.get(i);
                    int second = data.getLotterySecond() - 1;
                    data.setLotterySecond(second);
                    mList.set(i, data);
                }
                notifyDataSetChanged();
            }
            mHandler.postDelayed(mRunnable, 1000);
        }
    };

    public void removeRunnable() {
        mHandler.removeCallbacks(mRunnable);
    }

    public HallLotteryAdapter() {
        mInflater = (LayoutInflater) App.getAppContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mList = new ArrayList<>();// 初始化
        mNoList = new ArrayList<>();// 初始化
        mHadList = new ArrayList<>();// 初始化
        mHandler = new Handler();
        mHandler.post(mRunnable);
    }

    public void setNoList(List<LotteryModel.LotteryData> noList) {
        mNoList = noList == null ? new ArrayList<LotteryModel.LotteryData>() : noList;
        mNoLotteryCount = mNoList.size();
        handlerList();
    }

    public void setHadList(List<LotteryModel.LotteryData> hadList) {
        mHadList = hadList == null ? new ArrayList<LotteryModel.LotteryData>() : hadList;
        handlerList();
    }

    private void handlerList() {
        GameListModel.GameListData gameListData = AppPrefs.getInstance().getSelectedGame();
        if (gameListData != null) {
            stoBetSecond = gameListData.getStopBetSecond();
        }
        mList.clear();
        mList.addAll(mNoList);
        mList.addAll(mHadList);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.adapter_hall_lottery, null);
            holder.btn_betting = (TextView) convertView.findViewById(R.id.btn_betting);
            holder.rl_adapter_hall = (RelativeLayout) convertView.findViewById(R.id.rl_adapter_hall);
            holder.tv_issue = (TextView) convertView.findViewById(R.id.tv_issue);
            holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_lottery_time = (TextView) convertView.findViewById(R.id.tv_lottery_time);
            holder.tv_betting_count = (TextView) convertView.findViewById(R.id.tv_betting_count);
            holder.tv_cumulative = (TextView) convertView.findViewById(R.id.tv_cumulative);
            holder.tv_revenue = (TextView) convertView.findViewById(R.id.tv_revenue);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final LotteryModel.LotteryData data = getItem(position);
        convertView.findViewById(R.id.view_line).setVisibility(position == mNoLotteryCount - 1 ? View.VISIBLE : View.GONE);// 在未开奖和开奖历史中间添加分割线
        setVisibility(convertView, data.isNoLottery());
        holder.tv_issue.setText(data.getLotteryId() + "");
        holder.tv_num.setText(data.getLotteryResult() + "");
        holder.tv_lottery_time.setText(data.getLotteryTime());
        holder.tv_betting_count.setText(data.getBetNum() + "");
        holder.tv_betting_count.setBackgroundResource(data.getBetNum() > 0 ? R.drawable.radio_orange_bg : R.drawable.radio_gray_bg);
        holder.tv_cumulative.setText(data.getCumulative());

        holder.tv_betting_count.setBackgroundResource(R.drawable.radio_gray_bg);
        convertView.findViewById(R.id.tv_revenue).setVisibility(View.GONE);
        if (data.getBetNum() > 0) {
            holder.tv_betting_count.setBackgroundResource(R.drawable.radio_orange_bg);
            if (!data.isNoLottery()) {
                holder.tv_revenue.setText(App.getAppContext().getString(R.string.revenue) + "  " + data.getPureProfit());
                holder.tv_revenue.setTextColor(App.getAppContext().getResources().getColor(Double.parseDouble(data.getPureProfit()) > 0 ? R.color.red_text : R.color.green));
                convertView.findViewById(R.id.tv_revenue).setVisibility(View.VISIBLE);
            }
        }

        // --------------投注按钮的相关设置
        if (data.isNoLottery()) {
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
        }

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


        // -----------历史数据的点击事件
        // 设置监听事件
        if (!data.isNoLottery()) {
            holder.rl_adapter_hall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListenerHistory.onItemClickHistory(data);
                }
            });
        }
        return convertView;
    }

    private void setVisibility(View convertView, boolean isNoLottery) {
        if (isNoLottery) {
            convertView.findViewById(R.id.rl_adapter_hall).setBackgroundResource(R.color.white);
            convertView.findViewById(R.id.btn_betting).setVisibility(View.VISIBLE);
            convertView.findViewById(R.id.tv_time).setVisibility(View.VISIBLE);
            convertView.findViewById(R.id.tv_num).setVisibility(View.INVISIBLE);
            convertView.findViewById(R.id.tv_revenue).setVisibility(View.GONE);
        } else {
            convertView.findViewById(R.id.rl_adapter_hall).setBackgroundResource(R.drawable.btn_lottery_selector_bg);
            convertView.findViewById(R.id.btn_betting).setVisibility(View.GONE);
            convertView.findViewById(R.id.tv_time).setVisibility(View.INVISIBLE);
            convertView.findViewById(R.id.tv_num).setVisibility(View.VISIBLE);
            convertView.findViewById(R.id.tv_revenue).setVisibility(View.VISIBLE);
        }
    }

    class ViewHolder {
        RelativeLayout rl_adapter_hall;
        TextView tv_issue;// 期号
        TextView btn_betting;// 投注
        TextView tv_num;// 开奖号码
        TextView tv_time;// 开奖剩余时间
        TextView tv_lottery_time;// 开奖时间
        TextView tv_betting_count;// 投注数量
        TextView tv_cumulative;// 奖池
        TextView tv_revenue;// 盈利
    }

    // ************* 未开奖的点击事件
    private OnItemClickListenerRaelTime mListenerRaelTime = null;

    public void setListenerRaelTime(OnItemClickListenerRaelTime listenerRaelTime) {
        mListenerRaelTime = listenerRaelTime;
    }

    public interface OnItemClickListenerRaelTime {
        void onItemClickRaelTime(View view, LotteryModel.LotteryData data);
    }

    // ************* 历史数据的点击事件
    private OnItemClickListenerHistory mListenerHistory = null;

    public void setListenerHistory(OnItemClickListenerHistory listenerHistory) {
        mListenerHistory = listenerHistory;
    }

    public interface OnItemClickListenerHistory {
        void onItemClickHistory(LotteryModel.LotteryData data);
    }
}
