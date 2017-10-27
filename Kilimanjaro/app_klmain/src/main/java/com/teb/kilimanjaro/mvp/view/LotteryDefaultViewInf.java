package com.teb.kilimanjaro.mvp.view;

import android.os.Message;

/**
 * Created by yangbin on 16/7/15.
 */
public interface LotteryDefaultViewInf {
    
    void sendMyBetMessage(Message msg);
    
    void sendRosterMessage(Message msg);
}
