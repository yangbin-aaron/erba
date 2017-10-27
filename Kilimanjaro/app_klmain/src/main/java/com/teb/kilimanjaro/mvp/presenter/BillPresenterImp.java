package com.teb.kilimanjaro.mvp.presenter;

import android.os.Message;

import com.teb.kilimanjaro.mvp.OnFinishedListener;
import com.teb.kilimanjaro.mvp.model.BillModelImp;
import com.teb.kilimanjaro.mvp.view.BillViewInf;

import java.util.HashMap;

/**
 * Created by yangbin on 16/8/12.
 */
public class BillPresenterImp implements OnFinishedListener {

    private BillModelImp mBillModelImp;
    private BillViewInf mBillViewInf;

    public BillPresenterImp(BillViewInf billViewInf) {
        mBillViewInf = billViewInf;
        mBillModelImp = new BillModelImp();
    }

    public void getRechargeList(String json) {
        mBillModelImp.getRechargeList(this, json);
    }

    public void getWithdrawList(String json) {
        mBillModelImp.getWithdrawList(this, json);
    }

    @Override
    public void onFinished(Object object) {
        HashMap<String, Object> hashMap = (HashMap<String, Object>) object;
        String type = (String) hashMap.get("type");
        Message msg = (Message) hashMap.get("msg");
        if (type.equals(BillModelImp.GET_RECHARGE_LIST)) {
            mBillViewInf.sendRechargeListMessage(msg);
        } else if (type.equals(BillModelImp.GET_WITHDRAW_LIST)) {
            mBillViewInf.sendWithdrawListMessage(msg);
        }
    }
}
