package com.teb.kilimanjaro.mvp.model;

import android.os.Handler;
import android.os.Message;

import com.teb.kilimanjaro.httpnetwork.NetWorkManager;
import com.teb.kilimanjaro.mvp.OnFinishedListener;

import java.util.HashMap;

/**
 * Created by yangbin on 16/7/15.
 */
public class LotteryDefaultModelImp  {
    private OnFinishedListener mOnFinishedListener;

    private String type = ROSTER;
    public static final String MY_BET = "mey_bet";
    public static final String ROSTER = "roster";
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("type", type);
            hashMap.put("msg", msg);
            mOnFinishedListener.onFinished(hashMap);
        }
    };

    public void getMyBetList(OnFinishedListener onFinishedListener, String json) {
        this.mOnFinishedListener = onFinishedListener;
        type = MY_BET;
        NetWorkManager.getInstance().orderHadLotteryBets(json, mHandler);
    }

    public void getRosterList(OnFinishedListener onFinishedListener, String json) {
        this.mOnFinishedListener = onFinishedListener;
        type = ROSTER;
        NetWorkManager.getInstance().orderWinnersList(json, mHandler);
    }
}
