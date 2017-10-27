package com.teb.kilimanjaro.mvp.view;

import android.os.Message;

/**
 * Created by yangbin on 16/8/12.
 */
public interface BillViewInf {
    void sendRechargeListMessage(Message msg);
    void sendWithdrawListMessage(Message msg);
}
