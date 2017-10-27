package com.teb.kilimanjaro.activitys.hall;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teb.kilimanjaro.AppPrefs;
import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.configs.BroadcastConfig;
import com.teb.kilimanjaro.configs.HandlerConfig;
import com.teb.kilimanjaro.fragments.bettings.BetFragment0;
import com.teb.kilimanjaro.fragments.bettings.BetFragment1;
import com.teb.kilimanjaro.models.entry.hall.LotteryModel;
import com.teb.kilimanjaro.models.entry.hall.OddsListModel;
import com.teb.kilimanjaro.mvp.presenter.BettingPresenterImp;
import com.teb.kilimanjaro.mvp.view.BettingViewInf;
import com.teb.kilimanjaro.utils.MyUtil;
import com.teb.kilimanjaro.utils.StringUtil;
import com.teb.kilimanjaro.views.MyActionBar;
import com.teb.kilimanjaro.views.dialogs.MyDialog;

import java.util.HashMap;
import java.util.List;

/**
 * Created by yangbin on 16/7/13.
 */
public class BettingActivity extends FragmentActivity implements MyActionBar.OnActionBarClickListener, View.OnClickListener, BettingViewInf {

    private int stoBetSecond = 60;

    private BettingPresenterImp mBettingPresenterImp;
    private Dialog mLoadingDialog;

    private LotteryModel.LotteryData mLotteryData;

    private MyActionBar mMyActionBar;

    private List<OddsListModel.OddsData> mList0, mList1;
    private List<OddsListModel.OddsData> mBetList;// 最终投注的list，需要记录

    // 头部奖注信息:期数   开奖时间  开奖状态   投注剩余时间
    private TextView mIssueTV, mLotteryTimeTV, mLotteryStateTV, mSurplusTimeTV;
    private int mSurplusTime = 0;

    // 改变模式
    private LinearLayout mChangeModeLL;
    private TextView mChangeModeTV;
    private ImageView mChangeModeIV;
    private int mMode = AppPrefs.getInstance().getBetMode();// 0 投注界面   1极速投注界面

    private FrameLayout mFrameLayout0, mFrameLayout1;
    private BetFragment0 mBetFragment0;
    private BetFragment1 mBetFragment1;

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
                mLotteryStateTV.setVisibility(View.VISIBLE);
                findViewById(R.id.ll_bet_state).setVisibility(View.GONE);
                if (mSurplusTime > -stoBetSecond) {
                    mLotteryStateTV.setText(R.string.betting_endbet);
                    mBetFragment1.finishBetBtn(R.string.betting_endbet);
                } else {
                    mLotteryStateTV.setText(R.string.betting_open_result);
                    mBetFragment1.finishBetBtn(R.string.betting_open_result);
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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 强制为竖屏
        setContentView(R.layout.activity_betting);
        mLotteryData = (LotteryModel.LotteryData) getIntent().getSerializableExtra("data");
        mBettingPresenterImp = new BettingPresenterImp(this);
        mLoadingDialog = MyDialog.createLoadingDialog(this, true);
        initViews();
        getOddsList();
    }

    /**
     * 获取网络数据
     */
    private void getOddsList() {
        mLoadingDialog.show();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("gameId", mLotteryData.getGameId());
        if (mLotteryData.getBetNum() > 0) {// 获取未开奖的列表：和倍数列表数据相似
            hashMap.put("gameLotteryId", mLotteryData.getId());
            mBettingPresenterImp.getMybetList(StringUtil.getJson(hashMap));
        } else {// 获取倍数列表
            mBettingPresenterImp.getOddsList(StringUtil.getJson(hashMap));
        }
    }

    /**
     * 获取上期投注
     */
    public void getLastMyBetList() {
        List<OddsListModel.OddsData> tempList = AppPrefs.getInstance().getLastBetList();
        if (AppPrefs.getInstance().getBetMode() == 0) {
            mList0 = tempList;
            mBetFragment0.setOddsDataList(mList0);
        } else {
            mList1 = tempList;
            mBetFragment1.setOddsDataList(mList1);
        }
    }

    private void initViews() {
        // ******ActionBar
        mMyActionBar = (MyActionBar) findViewById(R.id.actionBar);
        mMyActionBar.setBarBackGround(R.color.main_background);
        mMyActionBar.setBarLeftImage(R.drawable.ic_back_btn);
        mMyActionBar.setBarTitleText(AppPrefs.getInstance().getSelectedGame().getGameName());
        mMyActionBar.setOnActionBarClickListener(this);

        // ******奖注信息
        mIssueTV = (TextView) findViewById(R.id.tv_issue);
        mIssueTV.setText(mLotteryData.getId() + "");
        mLotteryTimeTV = (TextView) findViewById(R.id.tv_lottery_time);
        mLotteryTimeTV.setText(mLotteryData.getLotteryTime());
        mSurplusTimeTV = (TextView) findViewById(R.id.tv_bet_surplus_time);
        mLotteryStateTV = (TextView) findViewById(R.id.tv_lottery_state);
        mSurplusTime = mLotteryData.getLotterySecond() - stoBetSecond;
        mHandler.post(mRunnable);

        // ******改变模式按钮
        mChangeModeLL = (LinearLayout) findViewById(R.id.ll_change_mode);
        mChangeModeLL.setOnClickListener(this);
        mChangeModeTV = (TextView) findViewById(R.id.tv_change_mode);
        mChangeModeIV = (ImageView) findViewById(R.id.iv_change_mode);

        // ******中间部分
        mFrameLayout0 = (FrameLayout) findViewById(R.id.fl_mode_0);
        mFrameLayout1 = (FrameLayout) findViewById(R.id.fl_mode_1);
        mBetFragment0 = new BetFragment0();
        mBetFragment1 = new BetFragment1();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction f_transaction = fragmentManager.beginTransaction();
        f_transaction.replace(R.id.fl_mode_0, mBetFragment0);
        f_transaction.replace(R.id.fl_mode_1, mBetFragment1);
        f_transaction.commit();

        changeModeLL();
    }

    @Override
    public void onActionBarLeftClicked() {
        finish();
    }

    @Override
    public void onActionBarRightClicked() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSurplusTime > -100) {
            mHandler.removeCallbacks(mRunnable);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_change_mode:
                mMode = (mMode + 1) % 2;
                AppPrefs.getInstance().saveBetMode(mMode);
                changeModeLL();
                if (mList0 == null || mList1 == null) getOddsList();
                break;

        }
    }

    /**
     * 改变转化模式按钮的式样
     */
    private void changeModeLL() {
        switch (mMode) {
            case 0:
                mChangeModeTV.setText(R.string.betting_change0);
                mChangeModeIV.setImageResource(R.drawable.ic_betting_mode_selected);
                mFrameLayout0.setVisibility(View.VISIBLE);
                mFrameLayout1.setVisibility(View.GONE);
                break;
            case 1:
                mChangeModeTV.setText(R.string.betting_change1);
                mChangeModeIV.setImageResource(R.drawable.ic_betting_mode_unselected);
                mFrameLayout0.setVisibility(View.GONE);
                mFrameLayout1.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 投注
     *
     * @param oddsDatas
     */
    public void orderBet(final List<OddsListModel.OddsData> oddsDatas, int betSum) {
        mBetList = oddsDatas;
        // 没有做修改则不做提交处理
        int sumed = 0;
        for (OddsListModel.OddsData data : oddsDatas) {// 计算之前投注过的（本次不再投注）
            if (data.isBeted()) sumed += data.getBetCoin();
        }
        long betCoin = betSum - sumed;// 档次投注金额
        if (betCoin <= 0) return;

        // 判断余额是否充足
        long gameCoin = AppPrefs.getInstance().getGameLong();
        if (gameCoin < betCoin) {// 余额不足，请充值
            // 保存投注的记录（下次可以直接投注）
            AppPrefs.getInstance().saveBetList(mBetList);
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
                            MyDialog.showWaitDialog(BettingActivity.this);
                        }
                    })
                    .show();
            return;
        }

        new MyDialog(this)
                .cancelable(true)
                .setMessage(getResources().getString(R.string.betting_confirm_bet, betSum))
                .setTitleText(getResources().getString(R.string.betting_save_bet))
                .setDefaultBtnText()
                .listener(new MyDialog.OnDialogClickListener() {
                    @Override
                    public void onLeftClicked() {

                    }

                    @Override
                    public void onRightClicked() {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("gameId", mLotteryData.getGameId());
                        hashMap.put("gameLotteryId", mLotteryData.getId());
                        HashMap<String, Object> map = new HashMap<>();
                        for (OddsListModel.OddsData data : oddsDatas) {
                            if (data.isSelected() && !data.isBeted()) {// 第一次投注过的不能再次投注
                                map.put(data.getLotteryNumber() + "", data.getBetCoin());
                            }
                        }
                        hashMap.put("bets", map);
                        mBettingPresenterImp.betOrder(StringUtil.getJson(hashMap));
                        mLoadingDialog.show();
                    }
                })
                .show();
    }

    // --------------------BettingViewInf method---------------------

    @Override
    public void sendOddsMessage(Message msg) {
        if (mLoadingDialog != null) mLoadingDialog.dismiss();
        switch (msg.what) {
            case HandlerConfig.WHAT_POST_SUCCESS:
                if (AppPrefs.getInstance().getBetMode() == 0) {
                    mList0 = (List<OddsListModel.OddsData>) msg.obj;
                    mBetFragment0.setOddsDataList(mList0);
                } else {
                    mList1 = (List<OddsListModel.OddsData>) msg.obj;
                    mBetFragment1.setOddsDataList(mList1);
                }
                break;
            default:
                MyUtil.handMessage(this, msg, "bettingActivity---Odds>>>");
                break;
        }
    }

    /**
     * 完成下注
     *
     * @param msg
     */
    @Override
    public void sendBetMessage(Message msg) {
        if (mLoadingDialog != null) mLoadingDialog.dismiss();
        switch (msg.what) {
            case HandlerConfig.WHAT_POST_SUCCESS:// 投注成功
                AppPrefs.getInstance().saveBetList(mBetList);
                sendBroadcast(new Intent(BroadcastConfig.ACTION_REFRESH_HALLDATA));
                finish();
                break;
            default:
                MyUtil.handMessage(this, msg, "bettingActivity---bet>>>");
                break;
        }
    }
}
