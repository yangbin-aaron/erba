package com.teb.kilimanjaro.mvp.presenter;

import android.os.Message;

import com.teb.kilimanjaro.mvp.OnFinishedListener;
import com.teb.kilimanjaro.mvp.model.LoginModelImp;
import com.teb.kilimanjaro.mvp.view.LoginViewInf;

/**
 * Created by yangbin on 16/7/4.
 * 登录Presenter方法类
 */
public class LoginPresenterImp implements OnFinishedListener {

    private LoginModelImp mLoginModelImp;
    private LoginViewInf mLoginViewInf;

    private int type = 0;

    public LoginPresenterImp(LoginViewInf loginViewInf) {
        mLoginViewInf = loginViewInf;
        mLoginModelImp = new LoginModelImp();
    }

    public void login(String loginJson) {
        type = 0;
        mLoginModelImp.login(this, loginJson);
    }

    public void emailActiveAccount(String json) {
        type = 1;
        mLoginModelImp.emailActiveAccount(this, json);
    }

    @Override
    public void onFinished(Object object) {
        Message msg = (Message) object;
        switch (type) {
            case 0:
                mLoginViewInf.sendLoginMessage(msg);
                break;
            case 1:
                mLoginViewInf.sendActiveAccountMessage(msg);
                break;
        }
    }
}
