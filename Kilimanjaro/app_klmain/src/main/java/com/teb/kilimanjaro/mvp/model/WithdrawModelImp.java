package com.teb.kilimanjaro.mvp.model;

import android.os.Handler;
import android.os.Message;

import com.teb.kilimanjaro.httpnetwork.NetWorkManager;
import com.teb.kilimanjaro.mvp.OnFinishedListener;

import java.util.HashMap;

/**
 * Created by yangbin on 16/8/17.
 */
public class WithdrawModelImp {

    private OnFinishedListener mOnFinishedListener;

    private String type = "";
    public final static String GET_CAPTCHA = "get_captcha";
    public final static String VERITY_CAPTCHA = "verity_captcha";
    public final static String WITHDRAW = "withdraw";
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

    /**
     * 获取验证码
     *
     * @param json
     */
    public void getCaptcha(OnFinishedListener onFinishedListener, String json) {
        mOnFinishedListener = onFinishedListener;
        type = GET_CAPTCHA;
        NetWorkManager.getInstance().getCaptcha(json, mHandler);
    }

    /**
     * 验证验证码
     *
     * @param json
     */
    public void verityCaptcha(OnFinishedListener onFinishedListener, String json) {
        mOnFinishedListener = onFinishedListener;
        type = VERITY_CAPTCHA;
        NetWorkManager.getInstance().verityCaptcha(json, mHandler);
    }

    /**
     * 提交申请
     *
     * @param onFinishedListener
     * @param json
     */
    public void withdraw(OnFinishedListener onFinishedListener, String json) {
        mOnFinishedListener = onFinishedListener;
        type = WITHDRAW;
        NetWorkManager.getInstance().withdraw(json, mHandler);
    }
}
