package com.teb.kilimanjaro.mvp.view;

import android.os.Message;

/**
 * Created by yangbin on 16/7/4.
 * 登录View层处理方法
 */
public interface LoginViewInf {
    
    void sendLoginMessage(Message msg);
    void sendActiveAccountMessage(Message msg);
}
