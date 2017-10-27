package com.teb.kilimanjaro.mvp.presenter;

import android.os.Message;

import com.teb.kilimanjaro.mvp.OnFinishedListener;
import com.teb.kilimanjaro.mvp.model.RevenueModelImp;
import com.teb.kilimanjaro.mvp.view.RevenueViewInf;

import java.util.HashMap;

/**
 * Created by yangbin on 16/7/21.
 * 我的亏盈
 */
public class RevenuePresenterImp implements OnFinishedListener {

    private RevenueModelImp mRevenueModelImp;
    private RevenueViewInf mRevenueViewInf;

    public RevenuePresenterImp(RevenueViewInf revenueViewInf) {
        mRevenueViewInf = revenueViewInf;
        mRevenueModelImp = new RevenueModelImp();
    }

    public void getRevenueGeneral(String json) {
        mRevenueModelImp.getRevenueGeneral(this, json);
    }

    public void getRevenueByDays(String json) {
        mRevenueModelImp.getRevenueByDays(this, json);
    }

    public void getRevenueByPeriods(String json) {
        mRevenueModelImp.getRevenueByPeriods(this, json);
    }

    @Override
    public void onFinished(Object object) {
        HashMap<String, Object> hashMap = (HashMap<String, Object>) object;
        String type = (String) hashMap.get("type");
        Message msg = (Message) hashMap.get("msg");
        if (type.equals(RevenueModelImp.GENERAL)) {
            mRevenueViewInf.sendGeneralMessage(msg);
        } else if (type.equals(RevenueModelImp.DAYS)) {
            mRevenueViewInf.sendDaysMessage(msg);
        } else if (type.equals(RevenueModelImp.PERIODS)) {
            mRevenueViewInf.sendPeriodsMessage(msg);
        }
    }
}
