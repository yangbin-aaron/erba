package com.teb.kilimanjaro.mvp.presenter;

import android.os.Message;

import com.teb.kilimanjaro.mvp.OnFinishedListener;
import com.teb.kilimanjaro.mvp.model.ResetPwdModelImp;
import com.teb.kilimanjaro.mvp.view.ResetPwdViewInf;

import java.util.HashMap;

/**
 * Created by yangbin on 16/7/7.
 */
public class ResetPwdPresenterImp implements  OnFinishedListener {

    private ResetPwdModelImp mResetPwdModelImp;
    private ResetPwdViewInf mResetPwdViewInf;

    public ResetPwdPresenterImp(ResetPwdViewInf resetPwdViewInf) {
        mResetPwdViewInf = resetPwdViewInf;
        mResetPwdModelImp = new ResetPwdModelImp();
    }


    public void sendSMS(String phone) {
        mResetPwdModelImp.sendSMS(this, phone);
    }

    public void sendCaptcha(String captchaJson) {
        mResetPwdModelImp.sendCaptcha(this, captchaJson);
    }

    public void resetPwd(String pwdJson) {
        mResetPwdModelImp.resetPwd(this, pwdJson);
    }

    @Override
    public void onFinished(Object object) {
        HashMap<String, Object> hashMap = (HashMap<String, Object>) object;
        String type = (String) hashMap.get("type");
        Message msg = (Message) hashMap.get("msg");
        if (type.equals(ResetPwdModelImp.SEND_SMS)) {
            mResetPwdViewInf.sendSMSMessage(msg);
        } else if (type.equals(ResetPwdModelImp.SEND_VIRIFY)) {
            mResetPwdViewInf.sendCaptchaMessage(msg);
        } else if (type.equals(ResetPwdModelImp.RESET)) {
            mResetPwdViewInf.sendResetMessage(msg);
        }
    }
}
