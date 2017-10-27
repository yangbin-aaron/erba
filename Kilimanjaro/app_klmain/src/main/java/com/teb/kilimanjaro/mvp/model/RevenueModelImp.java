package com.teb.kilimanjaro.mvp.model;

import android.os.Handler;
import android.os.Message;

import com.teb.kilimanjaro.httpnetwork.NetWorkManager;
import com.teb.kilimanjaro.mvp.OnFinishedListener;

import java.util.HashMap;

/**
 * Created by yangbin on 16/7/21.
 * 我的亏盈
 */
public class RevenueModelImp {

    private OnFinishedListener mOnFinishedListener;

    public static final String GENERAL = "general";
    public static final String DAYS = "days";
    public static final String PERIODS = "periods";

    /**
     * 内部调用
     *
     * @param type
     * @param msg
     */
    private void onFinishedMessage(String type, Message msg) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("type", type);
        hashMap.put("msg", msg);
        mOnFinishedListener.onFinished(hashMap);
    }

    private Handler mHandler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            onFinishedMessage(GENERAL, msg);
        }
    };

    private Handler mHandler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            onFinishedMessage(DAYS, msg);
        }
    };

    private Handler mHandler3 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            onFinishedMessage(PERIODS, msg);
        }
    };

    public void getRevenueGeneral(OnFinishedListener onFinishedListener, String json) {
        mOnFinishedListener = onFinishedListener;
        NetWorkManager.getInstance().getRevenueGeneral(json, mHandler1);
    }

    public void getRevenueByDays(OnFinishedListener onFinishedListener, String json) {
        mOnFinishedListener = onFinishedListener;
        NetWorkManager.getInstance().getRevenueByDays(json, mHandler2);
    }

    public void getRevenueByPeriods(OnFinishedListener onFinishedListener, String json) {
        mOnFinishedListener = onFinishedListener;
        NetWorkManager.getInstance().getRevenueByPeriods(json, mHandler3);
    }
}
