package com.teb.kilimanjaro.mvp.presenter;

import android.os.Message;

import com.teb.kilimanjaro.mvp.OnFinishedListener;
import com.teb.kilimanjaro.mvp.model.WithdrawModelImp;
import com.teb.kilimanjaro.mvp.view.WithdrawViewInf;

import java.util.HashMap;

/**
 * Created by yangbin on 16/8/17.
 */
public class WithdrawPresenterImp implements OnFinishedListener {

    private WithdrawViewInf mWithdrawViewInf;
    private WithdrawModelImp mWithdrawModelImp;

    public WithdrawPresenterImp(WithdrawViewInf withdrawViewInf) {
        mWithdrawViewInf = withdrawViewInf;
        mWithdrawModelImp = new WithdrawModelImp();
    }

    /**
     * 获取验证码
     *
     * @param json
     */
    public void getCaptcha(String json) {
        mWithdrawModelImp.getCaptcha(this, json);
    }

    /**
     * 验证验证码
     *
     * @param json
     */
    public void verityCaptcha(String json) {
        mWithdrawModelImp.verityCaptcha(this, json);
    }

    public void withdraw(String json) {
        mWithdrawModelImp.withdraw(this, json);
    }

    @Override
    public void onFinished(Object object) {
        HashMap<String, Object> hashMap = (HashMap<String, Object>) object;
        String type = (String) hashMap.get("type");
        Message msg = (Message) hashMap.get("msg");
        if (type.equals(WithdrawModelImp.GET_CAPTCHA)) {
            mWithdrawViewInf.sendGetCaptchaMessage(msg);
        } else if (type.equals(WithdrawModelImp.VERITY_CAPTCHA)) {
            mWithdrawViewInf.sendVerityCaptchaMessage(msg);
        } else if (type.equals(WithdrawModelImp.WITHDRAW)) {
            mWithdrawViewInf.sendWithdrawMessage(msg);
        }
    }
}
