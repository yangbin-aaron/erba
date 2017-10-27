package com.teb.kilimanjaro.mvp.view;

import android.os.Message;

/**
 * Created by yangbin on 16/7/14.
 */
public interface BettingViewInf {
    
    void sendOddsMessage(Message msg);
    
    void sendBetMessage(Message msg);
}
