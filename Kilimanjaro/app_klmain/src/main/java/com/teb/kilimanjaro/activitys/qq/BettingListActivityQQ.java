package com.teb.kilimanjaro.activitys.qq;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.teb.kilimanjaro.AppPrefs;
import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.activitys.BaseActivity;
import com.teb.kilimanjaro.adapters.BettingListAdapterQQ;
import com.teb.kilimanjaro.configs.BroadcastConfig;
import com.teb.kilimanjaro.configs.HandlerConfig;
import com.teb.kilimanjaro.models.entry.hall.LotteryModel;
import com.teb.kilimanjaro.models.entry.qq.BettingListModelQQ;
import com.teb.kilimanjaro.mvp.presenter.BettingListOfNoLotteryPresenterImpQQ;
import com.teb.kilimanjaro.mvp.view.BettingListOfNoLotteryViewInfQQ;
import com.teb.kilimanjaro.utils.MyUtil;
import com.teb.kilimanjaro.utils.StringUtil;
import com.teb.kilimanjaro.views.MyActionBar;
import com.teb.kilimanjaro.views.dialogs.MyDialog;

import java.util.HashMap;
import java.util.List;

/**
 * Created by yangbin on 16/8/1.
 * 已经投注的列表
 */
public class BettingListActivityQQ extends BaseActivity implements BettingListOfNoLotteryViewInfQQ {

    private BettingListOfNoLotteryPresenterImpQQ mBettingListOfNoLotteryPresenterImpQQ;
    private Dialog mLoadingDialog;

    private LotteryModel.LotteryData mLotteryData;

    private int stoBetSecond = 60;
    private TextView mIssueTV, mLotteryTimeTV, mLotteryStateTV, mSurplusTimeTV;

    private int mSurplusTime = 0;

    private MyActionBar mMyActionBar;
    // 加注
    private LinearLayout mAddBetLL;
    private TextView mAddBetTV;

    private long mTotalCoin;// 已经投注的总额

    // 列表
    private ListView mListView;
    private List<BettingListModelQQ.BettingListData> mList;
    private BettingListAdapterQQ mAdapterQQ;

    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mSurplusTime--;
            if (mSurplusTime > 0) {
//                mAddBetLL.setEnabled(true);
                mLotteryStateTV.setVisibility(View.GONE);
                findViewById(R.id.ll_bet_state).setVisibility(View.VISIBLE);
                mSurplusTimeTV.setText(mSurplusTime + "");// 逻辑没有问题，先减少1s再setText
            } else {
                mAddBetLL.setEnabled(false);
                mAddBetLL.setBackgroundResource(R.color.gray_x);
                mAddBetTV.setTextColor(getResources().getColor(R.color.gray_xx));
                mLotteryStateTV.setVisibility(View.VISIBLE);
                findViewById(R.id.ll_bet_state).setVisibility(View.GONE);
                if (mSurplusTime > -stoBetSecond) {
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

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BroadcastConfig.ACTION_BET_COMP)) {
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bettinglist_qq);
        stoBetSecond = AppPrefs.getInstance().getSelectedGame().getStopBetSecond();
        mLotteryData = (LotteryModel.LotteryData) getIntent().getSerializableExtra("data");
        initViews();

        mBettingListOfNoLotteryPresenterImpQQ = new BettingListOfNoLotteryPresenterImpQQ(this);
        mLoadingDialog = MyDialog.createLoadingDialog(this, true);

        getBettingList();

        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastConfig.ACTION_BET_COMP);
        registerReceiver(mReceiver, filter);
    }

    private void initViews() {
        // ******MyActionBar
        mMyActionBar = (MyActionBar) findViewById(R.id.actionBar);
        mMyActionBar.setBarBackGround(R.color.main_background);
        mMyActionBar.setBarLeftImage(R.drawable.ic_back_btn);
        mMyActionBar.setBarTitleText(R.string.qq_bettinglist_bar);
        mMyActionBar.setOnActionBarClickListener(new MyActionBar.OnActionBarClickListener() {
            @Override
            public void onActionBarLeftClicked() {
                finish();
            }

            @Override
            public void onActionBarRightClicked() {

            }
        });

        // 加注按钮
        mAddBetLL = (LinearLayout) findViewById(R.id.ll_add_betting);
        mAddBetTV = (TextView) findViewById(R.id.tv_add_betting);
        mAddBetLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BettingListActivityQQ.this, BettingActivityQQ.class);
                Bundle bundle = new Bundle();
                mLotteryData.setLotterySecond(mSurplusTime + stoBetSecond);
                bundle.putSerializable("data", mLotteryData);
                intent.putExtras(bundle);
                intent.putExtra("total_coin", mTotalCoin);// 本期已经投注数额
                MyUtil.startActivity(BettingListActivityQQ.this, intent);
            }
        });

        // ******奖注信息
        mIssueTV = (TextView) findViewById(R.id.tv_issue);
        mIssueTV.setText(mLotteryData.getLotteryId() + "");
        mLotteryTimeTV = (TextView) findViewById(R.id.tv_lottery_time);
        mLotteryTimeTV.setText(mLotteryData.getLotteryTime());
        mSurplusTimeTV = (TextView) findViewById(R.id.tv_bet_surplus_time);
        mLotteryStateTV = (TextView) findViewById(R.id.tv_lottery_state);
        mSurplusTime = mLotteryData.getLotterySecond() - stoBetSecond;
        mHandler.post(mRunnable);

        // ******ListView
        mListView = (ListView) findViewById(R.id.lv_bettinglist);
        mAdapterQQ = new BettingListAdapterQQ();
        mListView.setAdapter(mAdapterQQ);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        if (mSurplusTime > -100) {
            mHandler.removeCallbacks(mRunnable);
        }
    }

    /**
     * 获取网络数据
     */
    private void getBettingList() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("gameId", AppPrefs.getInstance().getSelectedGame().getId());
        hashMap.put("id", mLotteryData.getId());
        mBettingListOfNoLotteryPresenterImpQQ.getBettingListOfNoLottery(StringUtil.getJson(hashMap));
        mLoadingDialog.show();
    }

    // --------------------BettingListOfNoLotteryViewInfQQ method---------------------

    @Override
    public void sendBettingListOfNoLotteryMssage(Message msg) {
        switch (msg.what) {
            case HandlerConfig.WHAT_POST_SUCCESS:
                mList = (List<BettingListModelQQ.BettingListData>) msg.obj;
                for (BettingListModelQQ.BettingListData data : mList) {
                    mTotalCoin += Long.parseLong(data.getBetCoin());
                }
                if (mTotalCoin >= AppPrefs.getInstance().getSelectedGame().getCapCoin()) {
                    mAddBetLL.setEnabled(false);
                    mAddBetLL.setBackgroundResource(R.color.gray_x);
                    mAddBetTV.setTextColor(getResources().getColor(R.color.gray_xx));
                }
                mAdapterQQ.setList(mList);
                break;
            default:
                MyUtil.handMessage(this, msg, "BettingListActivityQQ---BettingList>>>");
                break;
        }
        if (mLoadingDialog != null) mLoadingDialog.dismiss();
    }
}
