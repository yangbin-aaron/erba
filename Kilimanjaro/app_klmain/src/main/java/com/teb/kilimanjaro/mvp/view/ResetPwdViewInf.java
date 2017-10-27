package com.teb.kilimanjaro.mvp.view;

import android.os.Message;

/**
 * Created by yangbin on 16/7/7.
 */
public interface ResetPwdViewInf {

    // 返回获取验证码msg
    void sendSMSMessage(Message msg);
    // 返回验证验证码msg
    void sendCaptchaMessage(Message msg);
    // 返回重置密码msg
    void sendResetMessage(Message msg);
}
