package com.teb.kilimanjaro.mvp.view;

import android.os.Message;

/**
 * Created by yangbin on 16/7/8.
 */
public interface HallViewInf {
    void sendHistoryLotteryMessage(Message msg);
    void sendNoLotteryMessage(Message msg);
    void sendGameCoinMessage(Message msg);
}
