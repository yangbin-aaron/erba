package com.teb.kilimanjaro.mvp.view;

import android.os.Message;

/**
 * Created by yangbin on 16/8/3.
 * QQ玩法投注界面接口
 */
public interface BettingViewInfQQ {
    void sendPattersMessage(Message msg);

    void sendBetMessage(Message msg);
}
