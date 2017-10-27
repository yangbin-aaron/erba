package com.teb.kilimanjaro.mvp.view;

import android.os.Message;

/**
 * Created by yangbin on 16/8/17.
 */
public interface WithdrawViewInf {
    void sendGetCaptchaMessage(Message msg);
    void sendVerityCaptchaMessage(Message msg);
    void sendWithdrawMessage(Message msg);
}
