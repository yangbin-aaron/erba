package com.teb.kilimanjaro.mvp.presenter;

import android.os.Message;

import com.teb.kilimanjaro.mvp.OnFinishedListener;
import com.teb.kilimanjaro.mvp.model.UpdatePwdModelImp;
import com.teb.kilimanjaro.mvp.view.UpdatePwdViewInf;

import java.util.HashMap;

/**
 * Created by yangbin on 16/7/6.
 */
public class UpdatePwdPresenterImp implements OnFinishedListener {

    private UpdatePwdModelImp mUpdatePwdModelImp;
    private UpdatePwdViewInf mUpdatePwdViewInf;

    public UpdatePwdPresenterImp(UpdatePwdViewInf updatePwdViewInf) {
        mUpdatePwdViewInf = updatePwdViewInf;
        mUpdatePwdModelImp = new UpdatePwdModelImp();
    }

    public void updateLoginPwd(String pwdJson) {
        mUpdatePwdModelImp.updateLoginPwd(this, pwdJson);
    }

    public void updatePayPwd(String pwdJson) {
        mUpdatePwdModelImp.updatePayPwd(this, pwdJson);
    }

    public void emailResetPayPassword(String json) {
        mUpdatePwdModelImp.emailResetPayPassword(this, json);
    }

    @Override
    public void onFinished(Object object) {
        HashMap<String, Object> hashMap = (HashMap<String, Object>) object;
        String type = (String) hashMap.get("type");
        Message msg = (Message) hashMap.get("msg");
        if (type.equals(UpdatePwdModelImp.LOGIN_PWD)) {
            mUpdatePwdViewInf.sendUpdateLoginPwdMessage(msg);
        } else if (type.equals(UpdatePwdModelImp.PAY_PWD)) {
            mUpdatePwdViewInf.sendUpdatePayPwdMessage(msg);
        } else if (type.equals(UpdatePwdModelImp.RESET_PAY_PWD)) {
            mUpdatePwdViewInf.sendResetPayPwdMessage(msg);
        }
    }
}
