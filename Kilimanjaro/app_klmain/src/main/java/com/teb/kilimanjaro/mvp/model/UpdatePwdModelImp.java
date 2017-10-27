package com.teb.kilimanjaro.mvp.model;

import android.os.Handler;
import android.os.Message;

import com.teb.kilimanjaro.httpnetwork.NetWorkManager;
import com.teb.kilimanjaro.mvp.OnFinishedListener;

import java.util.HashMap;

/**
 * Created by yangbin on 16/7/6.
 */
public class UpdatePwdModelImp {

    private String type = "";
    public static final String LOGIN_PWD = "login_pwd";
    public static final String PAY_PWD = "pay_pwd";
    public static final String RESET_PAY_PWD = "emailResetPayPassword";

    private OnFinishedListener mOnFinishedListener;

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

    public void updateLoginPwd(OnFinishedListener onFinishedListener, String pwdJson) {
        mOnFinishedListener = onFinishedListener;
        type = LOGIN_PWD;
        NetWorkManager.getInstance().updateLoginPwd(pwdJson, mHandler);
    }

    public void updatePayPwd(OnFinishedListener onFinishedListener, String pwdJson) {
        mOnFinishedListener = onFinishedListener;
        type = PAY_PWD;
        NetWorkManager.getInstance().updatePayPwd(pwdJson, mHandler);
    }

    public void emailResetPayPassword(OnFinishedListener onFinishedListener, String json) {
        mOnFinishedListener = onFinishedListener;
        type = RESET_PAY_PWD;
        NetWorkManager.getInstance().emailResetPayPassword(json, mHandler);
    }
}
