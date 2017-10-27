package com.teb.kilimanjaro.mvp.model;

import android.os.Handler;
import android.os.Message;

import com.teb.kilimanjaro.httpnetwork.NetWorkManager;
import com.teb.kilimanjaro.mvp.OnFinishedListener;

import java.util.HashMap;

/**
 * Created by yangbin on 16/7/7.
 */
public class ResetPwdModelImp {

    private OnFinishedListener mOnFinishedListener;

    private String type = "";// 用来区分不同的请求
    public static final String SEND_SMS = "send_sms";// 发送短信
    public static final String SEND_VIRIFY = "send_virify";// 发送验证码
    public static final String RESET = "reset";// 重置密码

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
     * 发送短信
     *
     * @param phone
     */
    public void sendSMS(OnFinishedListener onFinishedListener, String phone) {
        mOnFinishedListener = onFinishedListener;
        type = SEND_SMS;
        NetWorkManager.getInstance().getCaptcha(phone, mHandler);
    }

    /**
     * 发送验证码
     *
     * @param onFinishedListener
     * @param captchaJson
     */
    public void sendCaptcha(OnFinishedListener onFinishedListener, String captchaJson) {
        mOnFinishedListener = onFinishedListener;
        type = SEND_VIRIFY;
        NetWorkManager.getInstance().verityCaptcha(captchaJson, mHandler);
    }

    /**
     * 重置
     *
     * @param onFinishedListener
     * @param pwdJson
     */
    public void resetPwd(OnFinishedListener onFinishedListener, String pwdJson) {
        mOnFinishedListener = onFinishedListener;
        type = RESET;
        NetWorkManager.getInstance().resetPwd(pwdJson, mHandler);
    }
}
