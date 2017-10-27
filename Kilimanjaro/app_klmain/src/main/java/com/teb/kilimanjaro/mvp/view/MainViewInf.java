package com.teb.kilimanjaro.mvp.view;

import android.os.Message;

/**
 * Created by yangbin on 16/8/6.
 */
public interface MainViewInf {
    void sendGameListMessage(Message msg);
    void sendVersionMessage(Message msg);
}
