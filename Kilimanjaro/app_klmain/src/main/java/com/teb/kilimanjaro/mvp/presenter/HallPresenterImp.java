package com.teb.kilimanjaro.mvp.presenter;

import android.os.Message;

import com.teb.kilimanjaro.mvp.OnFinishedListener;
import com.teb.kilimanjaro.mvp.model.HallModelImp;
import com.teb.kilimanjaro.mvp.view.HallViewInf;

import java.util.HashMap;

/**
 * Created by yangbin on 16/7/8.
 */
public class HallPresenterImp implements  OnFinishedListener {

    private HallModelImp mHallModelImp;
    private HallViewInf mHallViewInf;

    public HallPresenterImp(HallViewInf hallViewInf) {
        mHallViewInf = hallViewInf;
        mHallModelImp = new HallModelImp();
    }

    public void getHistoryLotteryList(String lotteryJson) {
        mHallModelImp.getHistoryLotteryList(this, lotteryJson);
    }

    public void getNoLotteryList(String lotteryJson) {
        mHallModelImp.getNoLotteryList(this, lotteryJson);
    }

    public void getGameCoin(String json) {
        mHallModelImp.getGameCoin(this, json);
    }

    @Override
    public void onFinished(Object object) {
        HashMap<String, Object> hashMap = (HashMap<String, Object>) object;
        String type = (String) hashMap.get("type");
        Message msg = (Message) hashMap.get("msg");
        if (type.equals(HallModelImp.HISTORY)) {
            mHallViewInf.sendHistoryLotteryMessage(msg);
        } else if (type.equals(HallModelImp.NO)) {
            mHallViewInf.sendNoLotteryMessage(msg);
        } else if (type.equals(HallModelImp.GAME_COIN)) {
            mHallViewInf.sendGameCoinMessage(msg);
        }
    }
}
