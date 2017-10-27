package com.teb.kilimanjaro.mvp.model;

import android.os.Handler;
import android.os.Message;

import com.teb.kilimanjaro.httpnetwork.NetWorkManager;
import com.teb.kilimanjaro.mvp.OnFinishedListener;

/**
 * Created by yangbin on 16/8/18.
 */
public class WithdrawDetailModelImp {
    private OnFinishedListener mOnFinishedListener;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mOnFinishedListener.onFinished(msg);
        }
    };

    public void getWithdrawDetail(OnFinishedListener onFinishedListener, String json) {
        mOnFinishedListener = onFinishedListener;
        NetWorkManager.getInstance().getWithdrawDetail(json, mHandler);
    }
}
