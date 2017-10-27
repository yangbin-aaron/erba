package com.teb.kilimanjaro.mvp.presenter;

import android.os.Message;

import com.teb.kilimanjaro.mvp.OnFinishedListener;
import com.teb.kilimanjaro.mvp.model.RegistModelImp;
import com.teb.kilimanjaro.mvp.view.RegistViewInf;

import java.util.HashMap;

/**
 * Created by yangbin on 16/7/4.
 * 登录Presenter方法类
 */
public class RegistPresenterImp implements  OnFinishedListener {

    private RegistModelImp mRegistModelImp;
    private RegistViewInf mRegistViewInf;

    public RegistPresenterImp(RegistViewInf registViewInf) {
        mRegistViewInf = registViewInf;
        mRegistModelImp = new RegistModelImp();
    }

    /**
     * 发送短信
     *
     * @param phone
     */
    public void sendSMS(String phone) {
        mRegistModelImp.sendSMS(this, phone);
    }

    /**
     * 发送验证码
     *
     * @param captchaJson
     */
    public void sendCaptcha(String captchaJson) {
        mRegistModelImp.sendCaptcha(this, captchaJson);
    }

    /**
     * 完成注册
     *
     * @param registJson
     */
    public void compaleRegist(String registJson) {
        mRegistModelImp.compaleRegist(this, registJson);
    }

    @Override
    public void onFinished(Object object) {
        HashMap<String, Object> hashMap = (HashMap<String, Object>) object;
        String type = (String) hashMap.get("type");
        Message msg = (Message) hashMap.get("msg");
        if (type.equals(RegistModelImp.SEND_SMS)) {
            mRegistViewInf.sendSMSMessage(msg);
        } else if (type.equals(RegistModelImp.SEND_VIRIFY)) {
            mRegistViewInf.sendCaptchaMessage(msg);
        } else if (type.equals(RegistModelImp.REGIST)) {
            mRegistViewInf.sendRegistMessage(msg);
        }
    }
}
