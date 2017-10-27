package com.teb.kilimanjaro.activitys.qq;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.teb.kilimanjaro.AppPrefs;
import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.activitys.BaseActivity;
import com.teb.kilimanjaro.activitys.coin.RechargeActivity;
import com.teb.kilimanjaro.adapters.BetNumGridViewAdapterQQ;
import com.teb.kilimanjaro.adapters.BetPatterGridViewAdapterQQ;
import com.teb.kilimanjaro.configs.BroadcastConfig;
import com.teb.kilimanjaro.configs.HandlerConfig;
import com.teb.kilimanjaro.models.entry.hall.LotteryModel;
import com.teb.kilimanjaro.models.entry.hall.OddsListModel.OddsData;
import com.teb.kilimanjaro.models.entry.qq.BetPatternsModelQQ.BetPatternsData;
import com.teb.kilimanjaro.mvp.presenter.BettingPresenterImpQQ;
import com.teb.kilimanjaro.mvp.view.BettingViewInfQQ;
import com.teb.kilimanjaro.utils.MyUtil;
import com.teb.kilimanjaro.utils.StringUtil;
import com.teb.kilimanjaro.views.MyActionBar;
import com.teb.kilimanjaro.views.dialogs.BetAllDialog;
import com.teb.kilimanjaro.views.dialogs.MyDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yangbin on 16/8/1.
 * QQ群玩法的投注
 */
public class BettingActivityQQ extends BaseActivity
        implements BetNumGridViewAdapterQQ.OnBetNumItemClickListener, BetPatterGridViewAdapterQQ
        .OnBetModeItemClickListener, View.OnClickListener, BettingViewInfQQ {

    private final static String DANDIAN = "dandian";

    private BettingPresenterImpQQ mBettingPresenterImpQQ;
    private Dialog mLoadingDialog;

    private int stoBetSecond = 60;
    private LotteryModel.LotteryData mLotteryData;
    private long mTotalCoin;

    private MyActionBar mMyActionBar;
    private TextView mClearTV;// 清除

    private TextView mIssueTV, mLotteryTimeTV, mLotteryStateTV, mSurplusTimeTV;
    private int mSurplusTime = 0;

    private GridView mModeGV, mNumGV;
    private BetPatterGridViewAdapterQQ mModeAdapter;
    private List<BetPatternsData> mBettingModeDataList;
    private int mSelectIndex = -1;
    private BetNumGridViewAdapterQQ mNumAdapter;
    private List<OddsData> mOddsDataList;

    private TextView mLastIssueTV, mBetTV;// 上期投注，投注
    private String mBetNums = "";
    private int mDanDianId;// 单点的ID
    private TextView mGameCoinTV, mBetCoinTV;// 余额,投注额
    private long mBetCoin = 1000;// 默认投注额为1000

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
                mBetTV.setEnabled(false);
                mBetTV.setBackgroundResource(R.drawable.radio_gray_bg5);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stoBetSecond = AppPrefs.getInstance().getSelectedGame().getStopBetSecond();
        mLotteryData = (LotteryModel.LotteryData) getIntent().getSerializableExtra("data");
        mTotalCoin = getIntent().getLongExtra("total_coin", 0);
        setContentView(R.layout.activity_betting_qq);
        initViews();

        mBettingPresenterImpQQ = new BettingPresenterImpQQ(this);
        mLoadingDialog = MyDialog.createLoadingDialog(this, true);

        getBetPatters();
    }

    private void initViews() {
        // ******MyActionBar
        mMyActionBar = (MyActionBar) findViewById(R.id.actionBar);
        mMyActionBar.setBarBackGround(R.color.main_background);
        mMyActionBar.setBarLeftImage(R.drawable.ic_back_btn);
        mMyActionBar.setBarTitleText(R.string.qq_betting_bar);
        mMyActionBar.setOnActionBarClickListener(new MyActionBar.OnActionBarClickListener() {
            @Override
            public void onActionBarLeftClicked() {
                finish();
            }

            @Override
            public void onActionBarRightClicked() {
            }
        });

        // ******奖注信息
        mIssueTV = (TextView) findViewById(R.id.tv_issue);
        mLotteryTimeTV = (TextView) findViewById(R.id.tv_lottery_time);
        mSurplusTimeTV = (TextView) findViewById(R.id.tv_bet_surplus_time);
        mLotteryStateTV = (TextView) findViewById(R.id.tv_lottery_state);
        if (mLotteryData != null) {
            mIssueTV.setText(mLotteryData.getLotteryId() + "");
            mLotteryTimeTV.setText(mLotteryData.getLotteryTime());
            mSurplusTime = mLotteryData.getLotterySecond() - stoBetSecond;
            mHandler.post(mRunnable);
        }

        // ******清除按钮
        mClearTV = (TextView) findViewById(R.id.ll_change_mode);
        mClearTV.setOnClickListener(this);

        // ******GridView
        mModeGV = (GridView) findViewById(R.id.gv_mode);
        mModeAdapter = new BetPatterGridViewAdapterQQ();
        mModeAdapter.setOnBetModeItemClickListener(this);
        mModeGV.setAdapter(mModeAdapter);

        mNumGV = (GridView) findViewById(R.id.gv_nums);
        mNumAdapter = new BetNumGridViewAdapterQQ();
        mNumAdapter.setOnBetNumItemClickListener(this);
        mNumGV.setAdapter(mNumAdapter);
        mOddsDataList = new ArrayList<>();
        for (int i = 0; i < 28; i++) {
            OddsData oddsData = new OddsData();
            oddsData.setLotteryNumber(i);
            oddsData.setSelected(false);
            mOddsDataList.add(oddsData);
        }
        mNumAdapter.setList(mOddsDataList);

        // ******下面的两个按钮
        mLastIssueTV = (TextView) findViewById(R.id.btn_last_issue);
        mLastIssueTV.setOnClickListener(this);
        mBetTV = (TextView) findViewById(R.id.btn_betting);
        mBetTV.setOnClickListener(this);

        // ******余额，投注额
        mGameCoinTV = (TextView) findViewById(R.id.tv_amount);
        mBetCoinTV = (TextView) findViewById(R.id.tv_bet_coin);
        mBetCoinTV.setText(mBetCoin + "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        long gameCoin = AppPrefs.getInstance().getGameLong();
        mGameCoinTV.setText(gameCoin + "");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSurplusTime > -100) {
            mHandler.removeCallbacks(mRunnable);
        }
    }

    /**
     * 球的点击事件
     *
     * @param position
     */
    @Override
    public void onBetNumItemClickListener(int position) {
        OddsData data = mOddsDataList.get(position);
        data.setSelected(!data.isSelected());
        mOddsDataList.set(position, data);
        mNumAdapter.setList(mOddsDataList);
    }

    /**
     * 模式的点击事件
     *
     * @param position
     */
    @Override
    public void onBetModeItemClickListener(int position) {
        if (mSelectIndex != -1) {
            BetPatternsData selectData = mBettingModeDataList.get(mSelectIndex);
            selectData.setSelect(false);
        }
        mSelectIndex = position;
        BetPatternsData currentData = mBettingModeDataList.get(position);
        currentData.setSelect(true);
        mBettingModeDataList.set(position, currentData);
        mModeAdapter.setList(mBettingModeDataList);

        if (currentData.getCode().equals(DANDIAN)) {// 单点
            mDanDianId = currentData.getId();
            mOddsDataList = MyUtil.handlerOddsListByTimes(mOddsDataList, 0);
            mNumAdapter.setList(mOddsDataList);
            mNumAdapter.setCanClick(true);
        } else {// 其他
            mOddsDataList = MyUtil.handlerOddsList(currentData.getNums(), mOddsDataList);
            mNumAdapter.setList(mOddsDataList);
            mNumAdapter.setCanClick(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_last_issue:
                lastBetQQ();
                break;
            case R.id.btn_betting:
                if (mSelectIndex != -1) {
                    long canBetCoin = AppPrefs.getInstance().getSelectedGame().getCapCoin() - mTotalCoin;

                    int nums = 0;
                    boolean isDandian = false;
                    for (BetPatternsData patternsData : mBettingModeDataList) {
                        if (patternsData.isSelect() && patternsData.getCode().equals(DANDIAN)) {
                            isDandian = true;
                            for (OddsData oddsData : mOddsDataList) {
                                if (oddsData.isSelected()) {
                                    nums++;
                                }
                            }
                            break;
                        }
                    }
                    if (isDandian && nums == 0) return;
                    new BetAllDialog(this, nums)
                            .setDefaultCoin(mBetCoin + "")
                            .setMaxCoin(canBetCoin)
                            .listener(new BetAllDialog.OnDialogClickListener() {
                                @Override
                                public void onRightClicked(long coin) {
                                    mBetCoin = coin;
                                    compareGameCoin();
                                }
                            })
                            .show();
                }
                break;
            case R.id.ll_change_mode:// 清除
                // 恢复初始状态
                if (mSelectIndex != -1) {
                    BetPatternsData selectData = mBettingModeDataList.get(mSelectIndex);
                    selectData.setSelect(false);
                    mBettingModeDataList.set(mSelectIndex, selectData);
                    mModeAdapter.setList(mBettingModeDataList);
                    mSelectIndex = -1;

                    // 清除数字选择
                    mOddsDataList = MyUtil.handlerOddsListByTimes(mOddsDataList, 0);
                    mNumAdapter.setList(mOddsDataList);
                    mNumAdapter.setCanClick(false);
                }
                break;
        }
    }

    /**
     * 获取模式数据
     */
    private void getBetPatters() {
        mBettingPresenterImpQQ.getBetPatters(StringUtil.getNullJson());
        mLoadingDialog.show();
    }

    /**
     * 比较余额
     */
    private void compareGameCoin() {
        mBetCoinTV.setText(mBetCoin + "");
        // 判断余额是否充足
        long gameCoin = AppPrefs.getInstance().getGameLong();
        if (gameCoin < mBetCoin) {// 余额不足，请充值
            saveLastBetQQ();
            new MyDialog(this)
                    .cancelable(false)
                    .setMessage(getResources().getString(R.string.coin_nohave_residual))
                    .setTitleText(getResources().getString(R.string.betting_save_bet))
                    .setLeftText(getResources().getString(R.string.app_cancel))
                    .setRightText(getResources().getString(R.string.betting_goto_recharge))
                    .listener(new MyDialog.OnDialogClickListener() {
                        @Override
                        public void onLeftClicked() {
                            finish();
                        }

                        @Override
                        public void onRightClicked() {
                            MyUtil.startActivity(BettingActivityQQ.this, new Intent(BettingActivityQQ.this, RechargeActivity.class));
                        }
                    })
                    .show();
            return;
        }

        new MyDialog(this)
                .cancelable(true)
                .setMessage(getResources().getString(R.string.betting_confirm_bet, mBetCoin))
                .setTitleText(getResources().getString(R.string.betting_save_bet))
                .setDefaultBtnText()
                .listener(new MyDialog.OnDialogClickListener() {
                    @Override
                    public void onLeftClicked() {

                    }

                    @Override
                    public void onRightClicked() {
                        orderBet();
                    }
                })
                .show();
    }

    /**
     * 投注
     */
    private void orderBet() {
        for (OddsData data : mOddsDataList) {
            if (data.isSelected()) {
                if (TextUtils.isEmpty(mBetNums)) {
                    mBetNums = "" + data.getLotteryNumber();
                } else {
                    mBetNums += "," + data.getLotteryNumber();
                }
            }
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("gameId", AppPrefs.getInstance().getSelectedGame().getId());
        hashMap.put("id", mLotteryData.getId());
        hashMap.put("lotteryId", mLotteryData.getLotteryId());
        hashMap.put("betPatternId", mBettingModeDataList.get(mSelectIndex).getId());
        hashMap.put("betCoin", mBetCoin);
        hashMap.put("betNum", mBetNums);
        mBettingPresenterImpQQ.orderBet(StringUtil.getJson(hashMap));
        mLoadingDialog.show();
    }

    /**
     * 上次投注
     */
    private void lastBetQQ() {
        BetPatternsData data = AppPrefs.getInstance().getLastBetQQ();
        if (data != null && mBettingModeDataList != null) {
            mOddsDataList = MyUtil.handlerOddsList(data.getNums(), mOddsDataList);
            mNumAdapter.setList(mOddsDataList);
            mNumAdapter.setCanClick(mDanDianId == data.getId());

            // 设置模式按钮
            if (mSelectIndex != -1) {
                BetPatternsData selectData = mBettingModeDataList.get(mSelectIndex);
                selectData.setSelect(false);
            }
            int position = -1;
            for (int i = 0; i < mBettingModeDataList.size(); i++) {
                if (data.getId() == mBettingModeDataList.get(i).getId()) {
                    position = i;
                    break;
                }
            }
            mSelectIndex = position;
            BetPatternsData currentData = mBettingModeDataList.get(position);
            currentData.setSelect(true);
            mBettingModeDataList.set(position, currentData);
            mModeAdapter.setList(mBettingModeDataList);
        } else {
            MyUtil.showErrorDialog(this, "没有找到您的投注记录！");
        }
    }

    /**
     * 保存投注的信息（上次投注功能）
     */
    private void saveLastBetQQ() {
        BetPatternsData data = mBettingModeDataList.get(mSelectIndex);
        data.setNums(mBetNums);
        AppPrefs.getInstance().saveLastBetQQ(data);
    }

    // --------------------BettingViewInfQQ method---------------------

    /**
     * 获取模式
     *
     * @param msg
     */
    @Override
    public void sendPattersMessage(Message msg) {
        if (mLoadingDialog != null) mLoadingDialog.dismiss();
        switch (msg.what) {
            case HandlerConfig.WHAT_POST_SUCCESS:
                mBettingModeDataList = (List<BetPatternsData>) msg.obj;
                mModeAdapter.setList(mBettingModeDataList);
                break;
            default:
                MyUtil.handMessage(this, msg, "BettingActivityQQ---patters>>>");
                break;
        }
    }

    /**
     * 投注结果
     *
     * @param msg
     */
    @Override
    public void sendBetMessage(Message msg) {
        if (mLoadingDialog != null) mLoadingDialog.dismiss();
        saveLastBetQQ();
        switch (msg.what) {
            case HandlerConfig.WHAT_POST_SUCCESS:
                // 发送广播
                sendBroadcast(new Intent(BroadcastConfig.ACTION_BET_COMP));
                sendBroadcast(new Intent(BroadcastConfig.ACTION_REFRESH_HALLDATA));
                finish();
                break;
            default:
                finish();
                sendBroadcast(new Intent(BroadcastConfig.ACTION_REFRESH_HALLDATA));
                MyUtil.handMessage1(this, msg, "BettingActivityQQ---bet>>>");
                break;
        }
    }
}
