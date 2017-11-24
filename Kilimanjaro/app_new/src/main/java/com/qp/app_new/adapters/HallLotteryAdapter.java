package com.qp.app_new.adapters;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qp.app_new.App;
import com.qp.app_new.R;
import com.qp.app_new.utils.LogUtil;
import com.qp.app_new.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by yangbin on 16/9/3.
 */
public class HallLotteryAdapter extends BaseAdapter {

    private JSONArray mList;
    private JSONArray mNoList;
    private JSONArray mHadList;
    private int mNoLotteryCount = 0;// 未开奖的数据数量

    private LayoutInflater mInflater;

    private int stopBetSecond = 60;
    private static final int OPEN_TIME = 15;// 开奖时间

    private Handler mHandler;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mNoLotteryCount > 0) {// 有未开奖数据
                for (int i = 0; i < mNoList.length(); i++) {
                    JSONObject jsonObject = mNoList.optJSONObject(i);
                    int second = jsonObject.optInt("lotterySecond") - 1;
                    try {
                        jsonObject.put("lotterySecond", second);
                        mList.put(i, jsonObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                notifyDataSetChanged();
            }
            mHandler.postDelayed(mRunnable, 1000);
        }
    };

    public void removeRunnable() {
        mHandler.removeCallbacks(mRunnable);
    }

    public HallLotteryAdapter(int stopBetSecond) {
        this.stopBetSecond = stopBetSecond;
        mInflater = (LayoutInflater) App.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mList = new JSONArray();// 初始化
        mNoList = new JSONArray();// 初始化
        mHadList = new JSONArray();// 初始化
        mHandler = new Handler();
        mHandler.post(mRunnable);
    }

    public void setNoList(JSONArray noList) {
        if (noList == null) {
            mNoList = null;
            mNoList = new JSONArray();
        } else {
            mNoList = noList;
        }
        mNoLotteryCount = mNoList.length();
        handlerList();
    }

    public void setHadList(JSONArray hadList) {
        mHadList = hadList == null ? new JSONArray() : hadList;
        handlerList();
    }

    private void handlerList() {
        mList = null;
        mList = new JSONArray();
        for (int i = 0; i < mNoList.length(); i++) {
            JSONObject jsonObject = mNoList.optJSONObject(i);
            mList.put(jsonObject);
        }
        for (int i = 0; i < mHadList.length(); i++) {
            JSONObject jsonObject = mHadList.optJSONObject(i);
            mList.put(jsonObject);
        }
        LogUtil.e("handlerList", mList.toString());
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.length();
    }

    @Override
    public JSONObject getItem(int position) {
        return mList.optJSONObject(position);
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
            convertView = mInflater.inflate(R.layout.item_hall_lottery, null);
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

        final JSONObject data = getItem(position);
        convertView.findViewById(R.id.view_line).setVisibility(position == mNoLotteryCount - 1 ? View.VISIBLE : View.GONE);// 在未开奖和开奖历史中间添加分割线
        setVisibility(convertView, data.optBoolean("isNoLottery"));
        holder.tv_issue.setText(data.optString("lotteryId"));
        holder.tv_num.setText(data.optString("lotteryResult"));
        holder.tv_lottery_time.setText(StringUtil.getShowTime4(data.optString("lotteryTime")));
        holder.tv_betting_count.setText(data.optString("betNum"));
        holder.tv_betting_count.setBackgroundResource(data.optInt("betNum") > 0 ? R.drawable.shape_r2_orange_str0 : R.drawable.shape_r2_grayx_str0);
        holder.tv_cumulative.setText(data.optString("cumulative"));

        holder.tv_betting_count.setBackgroundResource(R.drawable.shape_r2_grayx_str0);
        convertView.findViewById(R.id.tv_revenue).setVisibility(View.GONE);
        if (data.optInt("betNum") > 0) {
            holder.tv_betting_count.setBackgroundResource(R.drawable.shape_r2_orange_str0);
            if (!data.optBoolean("isNoLottery")) {
                holder.tv_revenue.setText(App.mContext.getString(R.string.revenue) + "  " + data.optDouble("pureProfit"));
                holder.tv_revenue.setTextColor(App.mContext.getResources().getColor(data.optDouble("pureProfit") > 0 ? R.color.red_text : R.color.green));
                convertView.findViewById(R.id.tv_revenue).setVisibility(View.VISIBLE);
            }
        }

        if (data.optInt("lotterySecond") > 0) {
            holder.tv_time.setEnabled(false);
            holder.tv_time.setBackgroundResource(R.drawable.shape_circle_main_str0);
            holder.tv_time.setText((data.optInt("lotterySecond") > 999 ? 999 : data.optInt("lotterySecond")) + "秒");
            if (data.optInt("lotterySecond") <= stopBetSecond) {// 结束投注
                holder.btn_betting.setEnabled(data.optInt("betNum") > 0);// 有投注才能查看
                holder.btn_betting.setBackgroundResource(
                        data.optInt("betNum") > 0 ? R.drawable.shape_r5_orange_str0 : R.drawable.shape_r5_grayx_str0);
                holder.btn_betting.setText(R.string.betting_endbet);
            } else {
                holder.btn_betting.setEnabled(true);
                holder.btn_betting.setBackgroundResource(data.optInt("betNum") > 0 ? R.drawable.selector_r5_orange_orangex_str0 : R.drawable
                        .selector_r5_main_mainx_str0);
                holder.btn_betting.setText(R.string.betting_str);
            }
            if (data.optInt("betNum") > 0) {
                holder.btn_betting.setText(R.string.betting_hasbet);
                holder.btn_betting.setBackgroundResource(R.drawable.shape_r5_orange_str0);
            }
        } else {
            holder.btn_betting.setText(R.string.betting_open_com);
            holder.tv_time.setEnabled(true);// 开奖中
            holder.tv_time.setBackgroundResource(R.drawable.shape_circle_orange_str0);
            holder.tv_time.setText(R.string.betting_refresh);
            holder.btn_betting.setEnabled(data.optInt("betNum") > 0);// 有投注才能查看
            holder.btn_betting.setBackgroundResource(data.optInt("betNum") > 0 ? R.drawable.shape_r5_orange_str0 : R.drawable.shape_r5_grayx_str0);
        }


        // -----------历史数据的点击事件
        // 设置监听事件
        // --------------投注按钮的相关设置
        if (data.optBoolean("isNoLottery")) {
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
        } else {
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
            convertView.findViewById(R.id.rl_adapter_hall).setBackgroundResource(R.drawable.selector_r0_white_orange_str0);
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
        void onItemClickRaelTime(View view, JSONObject data);
    }

    // ************* 历史数据的点击事件
    private OnItemClickListenerHistory mListenerHistory = null;

    public void setListenerHistory(OnItemClickListenerHistory listenerHistory) {
        mListenerHistory = listenerHistory;
    }

    public interface OnItemClickListenerHistory {
        void onItemClickHistory(JSONObject data);
    }
}
