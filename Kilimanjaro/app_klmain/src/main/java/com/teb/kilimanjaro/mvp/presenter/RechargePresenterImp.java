package com.teb.kilimanjaro.mvp.presenter;

import android.os.Message;

import com.teb.kilimanjaro.mvp.OnFinishedListener;
import com.teb.kilimanjaro.mvp.model.RechargeModelImp;
import com.teb.kilimanjaro.mvp.view.RechargeViewInf;

/**
 * Created by yangbin on 16/8/10.
 */
public class RechargePresenterImp implements  OnFinishedListener {

    private RechargeViewInf mRechargeViewInf;
    private RechargeModelImp mRechargeModelImp;

    public RechargePresenterImp(RechargeViewInf rechargeViewInf) {
        mRechargeViewInf = rechargeViewInf;
        mRechargeModelImp = new RechargeModelImp();
    }

    public void getOrderNo(String json) {
        mRechargeModelImp.getOrderNo(this, json);
    }

    @Override
    public void onFinished(Object object) {
        Message msg = (Message) object;
        mRechargeViewInf.sendOrderIdMessage(msg);
    }
}
