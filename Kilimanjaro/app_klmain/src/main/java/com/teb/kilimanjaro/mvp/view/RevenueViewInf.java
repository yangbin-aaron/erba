package com.teb.kilimanjaro.mvp.view;

import android.os.Message;

/**
 * Created by yangbin on 16/7/21.
 * 我的亏盈
 */
public interface RevenueViewInf {
    void sendGeneralMessage(Message msg);

    void sendDaysMessage(Message msg);

    void sendPeriodsMessage(Message msg);
}
