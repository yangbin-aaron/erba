package com.teb.kilimanjaro.mvp.model;

import android.os.Handler;
import android.os.Message;

import com.teb.kilimanjaro.httpnetwork.NetWorkManager;
import com.teb.kilimanjaro.mvp.OnFinishedListener;

/**
 * Created by yangbin on 16/7/8.
 */
public class TrendModelImp {

    private OnFinishedListener mOnFinishedListener;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mOnFinishedListener.onFinished(msg);
        }
    };

    public void getChart(OnFinishedListener onFinishedListener, String getChartJson, boolean isBig) {
        mOnFinishedListener = onFinishedListener;
        NetWorkManager.getInstance().getChart(getChartJson, isBig, mHandler);
    }
}
