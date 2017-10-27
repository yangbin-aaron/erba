package com.teb.kilimanjaro.mvp.model;

import android.os.Handler;
import android.os.Message;

import com.teb.kilimanjaro.httpnetwork.NetWorkManager;
import com.teb.kilimanjaro.mvp.OnFinishedListener;

import java.util.HashMap;

/**
 * Created by yangbin on 16/7/8.
 */
public class HallModelImp {

    private OnFinishedListener mOnFinishedListener;

    public static final String HISTORY = "history";// 历史数据 
    public static final String NO = "no";// 未开奖数据 
    public static final String GAME_COIN = "game_coin";// 游戏币余额 

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("type", HISTORY);
            hashMap.put("msg", msg);
            mOnFinishedListener.onFinished(hashMap);
        }
    };

    private Handler mHandler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("type", NO);
            hashMap.put("msg", msg);
            mOnFinishedListener.onFinished(hashMap);
        }
    };

    private Handler mHandler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("type", GAME_COIN);
            hashMap.put("msg", msg);
            mOnFinishedListener.onFinished(hashMap);
        }
    };

    public void getHistoryLotteryList(OnFinishedListener onFinishedListener, String lotteryJson) {
        mOnFinishedListener = onFinishedListener;
        NetWorkManager.getInstance().getHistoryLotteryList(lotteryJson, mHandler);
    }

    public void getNoLotteryList(OnFinishedListener onFinishedListener, String lotteryJson) {
        mOnFinishedListener = onFinishedListener;
        NetWorkManager.getInstance().getNoLotteryList(lotteryJson, mHandler1);
    }

    public void getGameCoin(OnFinishedListener onFinishedListener, String json) {
        mOnFinishedListener = onFinishedListener;
        NetWorkManager.getInstance().getGameCoin(json, mHandler2);
    }
}
