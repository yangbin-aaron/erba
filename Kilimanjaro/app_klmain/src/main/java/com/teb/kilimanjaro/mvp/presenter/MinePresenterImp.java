package com.teb.kilimanjaro.mvp.presenter;

import android.os.Message;

import com.teb.kilimanjaro.mvp.OnFinishedListener;
import com.teb.kilimanjaro.mvp.model.MineModelImp;
import com.teb.kilimanjaro.mvp.view.MineViewInf;

import java.util.HashMap;

/**
 * Created by yangbin on 16/7/5.
 * 我的   presenter层方法类
 */
public class MinePresenterImp implements OnFinishedListener {

    private MineViewInf mMineViewInf;
    private MineModelImp mMineModelImp;

    public MinePresenterImp(MineViewInf mineViewInf) {
        mMineViewInf = mineViewInf;
        mMineModelImp = new MineModelImp();
    }

    /**
     * 退出登录
     */
    public void logout() {
        mMineModelImp.logout();
        mMineViewInf.logouted();
    }

    public void verifyPayPwd(String json) {
        mMineModelImp.verifyPayPwd(this, json);
    }

    public void getPrepareInfo(String json) {
        mMineModelImp.getPrepareInfo(this, json);
    }

    @Override
    public void onFinished(Object object) {
        HashMap<String, Object> hashMap = (HashMap<String, Object>) object;
        String type = (String) hashMap.get("type");
        Message msg = (Message) hashMap.get("msg");
        if (type.equals(MineModelImp.VERIFY_PAYPWD)) {
            mMineViewInf.sendVerifyPayPwdMessage(msg);
        } else if (type.equals(MineModelImp.GET_PREPAREINFO)) {
            mMineViewInf.sendMyCardMessage(msg);
        }
    }
}
