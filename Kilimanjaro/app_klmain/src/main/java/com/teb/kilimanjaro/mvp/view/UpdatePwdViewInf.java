package com.teb.kilimanjaro.mvp.view;

import android.os.Message;

/**
 * Created by yangbin on 16/7/6.
 */
public interface UpdatePwdViewInf {

    void sendUpdateLoginPwdMessage(Message msg);

    void sendUpdatePayPwdMessage(Message msg);
    void sendResetPayPwdMessage(Message msg);
}
