package com.teb.kilimanjaro.mvp.view;

import android.os.Message;

/**
 * Created by yangbin on 16/7/5.
 * 我的  View层处理方法
 */
public interface MineViewInf {
    
    // 已经退出登录
    void logouted();
    
    void sendVerifyPayPwdMessage(Message msg);
    void sendMyCardMessage(Message msg);
}
