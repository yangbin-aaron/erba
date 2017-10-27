package com.teb.kilimanjaro.mvp.presenter;

import android.os.Message;

import com.teb.kilimanjaro.mvp.OnFinishedListener;
import com.teb.kilimanjaro.mvp.model.BettingModelImpQQ;
import com.teb.kilimanjaro.mvp.view.BettingViewInfQQ;

import java.util.HashMap;

/**
 * Created by yangbin on 16/8/3.
 */
public class BettingPresenterImpQQ implements  OnFinishedListener {

    private BettingViewInfQQ mBettingViewInfQQ;
    private BettingModelImpQQ mBettingModelImpQQ;

    public BettingPresenterImpQQ(BettingViewInfQQ bettingViewInfQQ) {
        mBettingViewInfQQ = bettingViewInfQQ;
        mBettingModelImpQQ = new BettingModelImpQQ();
    }

    public void getBetPatters(String json) {
        mBettingModelImpQQ.getBetPatters(this, json);
    }

    public void orderBet(String json) {
        mBettingModelImpQQ.orderBet(this, json);
    }

    @Override
    public void onFinished(Object object) {
        HashMap<String, Object> hashMap = (HashMap<String, Object>) object;
        String type = (String) hashMap.get("type");
        Message msg = (Message) hashMap.get("msg");
        if (type.equals(BettingModelImpQQ.BET)) {
            mBettingViewInfQQ.sendBetMessage(msg);
        } else if (type.equals(BettingModelImpQQ.GET_MODE_LIST)) {
            mBettingViewInfQQ.sendPattersMessage(msg);
        }
    }
}
