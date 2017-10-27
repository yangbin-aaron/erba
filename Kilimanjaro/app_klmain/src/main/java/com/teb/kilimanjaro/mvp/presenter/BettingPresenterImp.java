package com.teb.kilimanjaro.mvp.presenter;

import android.os.Message;

import com.teb.kilimanjaro.mvp.OnFinishedListener;
import com.teb.kilimanjaro.mvp.model.BettingModelImp;
import com.teb.kilimanjaro.mvp.view.BettingViewInf;

import java.util.HashMap;

/**
 * Created by yangbin on 16/7/14.
 */
public class BettingPresenterImp implements  OnFinishedListener {

    private BettingModelImp mBettingModelImp;
    private BettingViewInf mBettingViewInf;

    public BettingPresenterImp(BettingViewInf bettingViewInf) {
        mBettingViewInf = bettingViewInf;
        mBettingModelImp = new BettingModelImp();
    }

    public void getOddsList(String oddsJson) {
        mBettingModelImp.getOddsList(this, oddsJson);
    }

    public void betOrder(String betJson) {
        mBettingModelImp.betOrder(this, betJson);
    }

    public void getMybetList(String json) {
        mBettingModelImp.getMybetList(this, json);
    }

    @Override
    public void onFinished(Object object) {
        HashMap<String, Object> hashMap = (HashMap<String, Object>) object;
        String type = (String) hashMap.get("type");
        Message msg = (Message) hashMap.get("msg");
        if (type.equals(BettingModelImp.GET_ODDS_DATA)) {
            mBettingViewInf.sendOddsMessage(msg);
        } else if (type.equals(BettingModelImp.BET)) {
            mBettingViewInf.sendBetMessage(msg);
        }
    }
}
