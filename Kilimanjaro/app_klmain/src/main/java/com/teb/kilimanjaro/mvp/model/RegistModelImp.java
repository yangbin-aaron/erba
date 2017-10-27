package com.teb.kilimanjaro.mvp.model;

import android.os.Handler;
import android.os.Message;

import com.teb.kilimanjaro.httpnetwork.NetWorkManager;
import com.teb.kilimanjaro.mvp.OnFinishedListener;

import java.util.HashMap;

/**
 * Created by yangbin on 16/7/4.
 * 注册model方法类
 */
public class RegistModelImp {

    private OnFinishedListener mOnFinishedListener;

    private String type = "";// 用来区分不同的请求
    public static final String SEND_SMS = "send_sms";// 发送短信
    public static final String SEND_VIRIFY = "send_virify";// 发送验证码
    public static final String REGIST = "regist";// 注册

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
        NetWorkManager.getInstance().sendRegistSMS(phone, mHandler);
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
     * 完成注册
     *
     * @param onFinishedListener
     * @param registJson
     */
    public void compaleRegist(OnFinishedListener onFinishedListener, String registJson) {
        mOnFinishedListener = onFinishedListener;
        type = REGIST;
        NetWorkManager.getInstance().compaleRegist(registJson, mHandler);
    }
}
