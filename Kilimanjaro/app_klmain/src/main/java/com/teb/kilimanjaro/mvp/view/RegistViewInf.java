package com.teb.kilimanjaro.mvp.view;

import android.os.Message;

/**
 * Created by yangbin on 16/7/6.
 * 注册  View层处理方法
 */
public interface RegistViewInf {
    // 返回获取验证码msg
    void sendSMSMessage(Message msg);
    // 返回验证验证码msg
    void sendCaptchaMessage(Message msg);
    // 返回注册msg
    void sendRegistMessage(Message msg);
}
