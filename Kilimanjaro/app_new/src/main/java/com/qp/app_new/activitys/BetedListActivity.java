package com.qp.app_new.activitys;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.qp.app_new.R;
import com.qp.app_new.adapters.BettingListAdapter;
import com.qp.app_new.configs.BroadcastConfig;
import com.qp.app_new.httpnetworks.NetWorkManager;
import com.qp.app_new.interfaces.NetListener;
import com.qp.app_new.utils.ActivityStartUtils;
import com.qp.app_new.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Aaron on 2017/11/16.
 * 已投注列表
 */

public class BetedListActivity extends BaseActivity {

    @Override
    public int getContentView() {
        return R.layout.activity_beted_list;
    }

    private int mSurplusTime = 0;
    private int stopBetSecond = 60;

    private JSONObject mGameJSONObject, mLotteryJSONObject;
    private TextView mIssueTV, mLotteryTimeTV, mLotteryStateTV, mSurplusTimeTV;

    // 加注
    private LinearLayout mAddBetLL;
    private TextView mAddBetTV;

    private ListView mListView;
    private BettingListAdapter mAdapter;

    private long mTotalCoin;// 已经投注的总额
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mSurplusTime--;
            if (mSurplusTime > 0) {
                mLotteryStateTV.setVisibility(View.GONE);
                findViewById(R.id.ll_bet_state).setVisibility(View.VISIBLE);
                mSurplusTimeTV.setText(mSurplusTime + "");// 逻辑没有问题，先减少1s再setText
            } else {
                mAddBetLL.setEnabled(false);
                mAddBetLL.setBackgroundResource(R.drawable.shape_r5_grayx_str0);
                mAddBetTV.setTextColor(getResources().getColor(R.color.gray_xx));
                mLotteryStateTV.setVisibility(View.VISIBLE);
                findViewById(R.id.ll_bet_state).setVisibility(View.GONE);
                if (mSurplusTime > -stopBetSecond) {
                    mLotteryStateTV.setText(R.string.betting_endbet);
                } else {
                    mLotteryStateTV.setText(R.string.betting_open_result);
                }
            }
            if (mSurplusTime < -100) {
                mHandler.removeCallbacks(mRunnable);
            } else {
                mHandler.postDelayed(mRunnable, 1000);
            }
        }
    };

    @Override
    public void getIntentData() {
        super.getIntentData();
        try {
            mGameJSONObject = new JSONObject(getIntent().getStringExtra("gameJsonObject"));
            stopBetSecond = mGameJSONObject.optInt("stopBetSecond", 60);
            mLotteryJSONObject = new JSONObject(getIntent().getStringExtra("lotteryJsonObject"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addFilterAction(IntentFilter filter) {
        super.addFilterAction(filter);
        filter.addAction(BroadcastConfig.ACTION_REFRESH_GAMELIST);
    }

    @Override
    public void handlerBroadcastReceiver(Intent intent) {
        super.handlerBroadcastReceiver(intent);
        if (intent.getAction().equals(BroadcastConfig.ACTION_REFRESH_GAMELIST)) {
           getBettingList();
        }
    }

    @Override
    public void initView() {
        initActionBar();
        setLeftIV(R.drawable.ic_back_btn);
        setTitle(R.string.qq_bettinglist_bar);

        // ******奖注信息
        mIssueTV = (TextView) findViewById(R.id.tv_issue);
        mLotteryTimeTV = (TextView) findViewById(R.id.tv_lottery_time);
        mSurplusTimeTV = (TextView) findViewById(R.id.tv_bet_surplus_time);
        mLotteryStateTV = (TextView) findViewById(R.id.tv_lottery_state);

        if (mLotteryJSONObject != null) {
            mIssueTV.setText(mLotteryJSONObject.optString("lotteryId"));
            mLotteryTimeTV.setText(StringUtil.getShowTime4(mLotteryJSONObject.optString("lotteryTime")));
            mSurplusTime = mLotteryJSONObject.optInt("lotterySecond") - stopBetSecond;
            mHandler.post(mRunnable);
        }

        // 加注按钮
        mAddBetLL = (LinearLayout) findViewById(R.id.ll_add_betting);
        mAddBetTV = (TextView) findViewById(R.id.tv_add_betting);
        mAddBetLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mLotteryJSONObject.put("lotterySecond", mSurplusTime + stopBetSecond);
                    mLotteryJSONObject.put("total_coin", mTotalCoin);
                    ActivityStartUtils.startBetActivity(BetedListActivity.this, mGameJSONObject, mLotteryJSONObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        // ******ListView
        mListView = (ListView) findViewById(R.id.lv_bettinglist);
        mAdapter = new BettingListAdapter();
        mListView.setAdapter(mAdapter);

        getBettingList();
    }

    private void getBettingList() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("gameId", mGameJSONObject.optString("id"));
        hashMap.put("id", mLotteryJSONObject.optString("id"));
        NetWorkManager.getInstance().getBettingListOfNoLottery(StringUtil.getJson(hashMap), mLoadingDialog, new NetListener(this) {
            @Override
            public void onSuccessResponse(String msg, JSONArray jsonArray) {
                super.onSuccessResponse(msg, jsonArray);
                mAdapter.setJSONArray(jsonArray);
            }

            @Override
            public void onErrorResponse(int errorWhat, String message) {
                super.onErrorResponse(errorWhat, message);
            }
        });
    }
}
