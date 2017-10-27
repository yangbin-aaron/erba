package com.teb.kilimanjaro.mvp.model;

import android.os.Handler;
import android.os.Message;

import com.teb.kilimanjaro.httpnetwork.NetWorkManager;
import com.teb.kilimanjaro.mvp.OnFinishedListener;

/**
 * Created by yangbin on 16/7/4.
 * 登录model方法类
 */
public class LoginModelImp {

    private OnFinishedListener mOnFinishedListener;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mOnFinishedListener.onFinished(msg);
        }
    };

    /**
     * 登录
     *
     * @param onFinishedListener
     * @param loginJson
     */
    public void login(OnFinishedListener onFinishedListener, String loginJson) {
        this.mOnFinishedListener = onFinishedListener;
        NetWorkManager.getInstance().login(loginJson, mHandler);
    }

    public void emailActiveAccount(OnFinishedListener onFinishedListener, String json) {
        this.mOnFinishedListener = onFinishedListener;
        NetWorkManager.getInstance().emailActiveAccount(json, mHandler);
    }
}
