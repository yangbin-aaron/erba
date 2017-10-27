package com.teb.kilimanjaro.mvp.presenter;

import android.os.Message;

import com.teb.kilimanjaro.mvp.OnFinishedListener;
import com.teb.kilimanjaro.mvp.model.LotteryDefaultModelImp;
import com.teb.kilimanjaro.mvp.view.LotteryDefaultViewInf;

import java.util.HashMap;

/**
 * Created by yangbin on 16/7/15.
 */
public class LotteryDefaultPresenterImp implements  OnFinishedListener {

    private LotteryDefaultModelImp mLotteryDefaultModelImp;
    private LotteryDefaultViewInf mLotteryDefaultViewInf;

    public LotteryDefaultPresenterImp(LotteryDefaultViewInf lotteryDefaultViewInf) {
        mLotteryDefaultViewInf = lotteryDefaultViewInf;
        mLotteryDefaultModelImp = new LotteryDefaultModelImp();
    }


    public void getMyBetList(String json) {
        mLotteryDefaultModelImp.getMyBetList(this, json);
    }

    public void getRosterList(String json) {
        mLotteryDefaultModelImp.getRosterList(this, json);
    }

    public void onFinished(Object object) {
        HashMap<String, Object> hashMap = (HashMap<String, Object>) object;
        String type = (String) hashMap.get("type");
        Message msg = (Message) hashMap.get("msg");
        if (type.equals(LotteryDefaultModelImp.MY_BET)) {
            mLotteryDefaultViewInf.sendMyBetMessage(msg);
        } else if (type.equals(LotteryDefaultModelImp.ROSTER)) {
            mLotteryDefaultViewInf.sendRosterMessage(msg);
        }
    }
}
