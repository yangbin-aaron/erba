package com.teb.kilimanjaro.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.teb.kilimanjaro.AppPrefs;
import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.activitys.mine.LoginActivity;
import com.teb.kilimanjaro.configs.BroadcastConfig;
import com.teb.kilimanjaro.configs.HandlerConfig;
import com.teb.kilimanjaro.httpnetwork.NetWorkManager;
import com.teb.kilimanjaro.models.bean.BetAutoModel;
import com.teb.kilimanjaro.models.entry.hall.LotteryModel;
import com.teb.kilimanjaro.models.entry.hall.OddsListModel;
import com.teb.kilimanjaro.utils.MyUtil;
import com.teb.kilimanjaro.utils.StringUtil;
import com.teb.kilimanjaro.utils.ToastUtil;

import java.util.HashMap;

/**
 * Created by yangbin on 16/7/27.
 */
public class BetAutoService extends Service {
    public static final String ACTION = "com.teb.kilimanjaro.services.BetAutoService";

    private BetAutoModel mBetAutoModel;
    private int mIssueStart;
    private int mIssueCount;
    private long mMaxCoin;
    private long mMinCoin;
    private int mGameId;

    private int mIssueCurr;// 当前期号

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HandlerConfig.WHAT_POST_SUCCESS:
                    Log.e("mode", "投注成功");
                    sendBroadcast(new Intent(BroadcastConfig.ACTION_REFRESH_HALLDATA));
                    break;
                case HandlerConfig.WHAT_TOKEN_IS_UPDATED:
                    removeRunnable();
                    ToastUtil.showToast(R.string.have_login_wrong);
                    // 清空UserJson数据和GameListJson数据
                    AppPrefs.getInstance().saveUserJson(null);
                    AppPrefs.getInstance().saveGameListJson(null);
                    Intent intent = new Intent(BetAutoService.this, LoginActivity.class);
                    startActivity(intent);
                    break;
                default:
                    MyUtil.handMessage(null, msg, "BetAutoService---bet>>>");
                    break;
            }
        }
    };

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            switch (canBetting()) {
                case -1:
                    removeRunnable();
                    break;
                case 1:
                    orderBet();
                case 0:
                    mHandler.postDelayed(mRunnable, 5000);
                    break;
            }
        }
    };

    /**
     * 下注
     */
    private void orderBet() {
        LotteryModel.LotteryData lotteryData = AppPrefs.getInstance().getCurrentBetInfo();
        if (lotteryData.getBetNum() > 0) {// 如果已经投注过，则跳过该期
            Log.e("mode", "该期已经投注过");
        } else {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("gameId", AppPrefs.getInstance().getSelectedGame().getId());
            hashMap.put("gameLotteryId", mIssueStart);
            HashMap<String, Object> map = new HashMap<>();
            for (OddsListModel.OddsData data : mBetAutoModel.getModeAutoDataOfStart().getList()) {
                if (data.isSelected() && !data.isBeted()) {// 第一次投注过的不能再次投注
                    map.put(data.getLotteryNumber() + "", data.getBetCoin());
                }
            }
            hashMap.put("bets", map);
            NetWorkManager.getInstance().orderBet(StringUtil.getJson(hashMap), mHandler);

            mIssueCount--;
        }
        mIssueStart++;
        mIssueCurr = lotteryData.getId();
    }

    /**
     * 能否下注
     *
     * @return 0：不能下注  1：下注  -1：退出任务
     */
    private int canBetting() {
        if (mGameId != AppPrefs.getInstance().getSelectedGame().getId()) {
            Log.e("mode", "切换了游戏，自动投注停止");
            return -1;
        }
        Log.e("mode", mIssueStart + "==" + AppPrefs.getInstance().getCurrentBetInfo().getId() + "  >>>  " + mIssueCount);
        if (mIssueCount == 0) {// 设定的自动投注次数完成
            Log.e("mode", "已经完成所有投注");
            return -1;
        }
        // 判断余额是否充足
        long gameCoin = AppPrefs.getInstance().getGameLong();
        if (gameCoin < mBetAutoModel.getModeAutoDataOfStart().getTotalCount()) {
            // 余额不足,终止任务
            ToastUtil.showToast(R.string.coin_not_has_more);
            Log.e("mode", "余额不足");
            return -1;
        }

        if (mIssueCurr >= AppPrefs.getInstance().getCurrentBetInfo().getId()) {// 还未开奖,不能下注，继续任务
            Log.e("mode", "还未开奖");
            return 0;
        }
        // 比较余额
        if (mMaxCoin != 0 && gameCoin >= mMaxCoin) {// 达到设定目标，退出任务
            Log.e("mode", "余额达到了" + gameCoin + ",已经超过设置金额：" + mMaxCoin);
            return -1;
        }
        if (gameCoin <= mMinCoin) {// 达到设定目标，退出任务
            Log.e("mode", "余额只有" + gameCoin + "了,已经低于设置金额：" + mMinCoin);
            return -1;
        }

        if (mIssueStart != AppPrefs.getInstance().getCurrentBetInfo().getId()) {// 一般在改变了默认的开始期数后才会出现该情况
            Log.e("mode", "当前期号不是自己设置的自动投注开始期号");
            return 0;
        }


        if (!AppPrefs.getInstance().isContinueAfterWin()) {// 设置了赢了之后不继续
            // 判断最新开奖信息是输还是赢
            LotteryModel.LotteryData newData = AppPrefs.getInstance().getNewBetInfo();
            if (newData != null && mIssueCurr == newData.getId()) {// 是自己投注的最新一期
                String newPure = newData.getPureProfit();
                if (newPure != null && !TextUtils.isEmpty(newPure)) {// 证明赢了，可以结束本次自动投注
                    if (Double.parseDouble(newPure) > 0) {
                        Log.e("mode", "赢了,结束本次自动投注");
                        return -1;
                    } else {
                        Log.e("mode", "输了,继续本次自动投注");
                    }
                }
            }
        }

        return 1;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("mode", "onCreate----------------------------------");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mBetAutoModel = AppPrefs.getInstance().getBetAutoModel();
        mIssueStart = mBetAutoModel.getStartIssue();
        mIssueCount = mBetAutoModel.getAutoCount();
        mMaxCoin = mBetAutoModel.getMaxCoin();
        mMinCoin = mBetAutoModel.getMinCoin();
        mGameId = AppPrefs.getInstance().getSelectedGame().getId();
        mIssueCurr = AppPrefs.getInstance().getCurrentBetInfo().getId();

        Log.e("mode", "onStartCommand----------------------------------");
        mHandler.post(mRunnable);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean stopService(Intent name) {
        Log.e("mode", "stopService----------------------------------");
        return super.stopService(name);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeRunnable();
        Log.e("mode", "onDestroy----------------------------------");
    }

    private void removeRunnable() {
        mHandler.removeCallbacks(mRunnable);
        AppPrefs.getInstance().saveBetAutoStartState(false);
    }

}
