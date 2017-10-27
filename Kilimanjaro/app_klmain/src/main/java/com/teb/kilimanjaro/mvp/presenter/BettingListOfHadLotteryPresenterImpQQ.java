package com.teb.kilimanjaro.mvp.presenter;

import android.os.Message;

import com.teb.kilimanjaro.mvp.OnFinishedListener;
import com.teb.kilimanjaro.mvp.model.BettingListOfHadLotteryModelImpQQ;
import com.teb.kilimanjaro.mvp.view.BettingListOfHadLotteryViewInfQQ;

/**
 * Created by yangbin on 16/8/3.
 */
public class BettingListOfHadLotteryPresenterImpQQ implements OnFinishedListener {

    private BettingListOfHadLotteryViewInfQQ mBettingListViewInfQQ;
    private BettingListOfHadLotteryModelImpQQ mBettingListModelImpQQ;

    public BettingListOfHadLotteryPresenterImpQQ(BettingListOfHadLotteryViewInfQQ bettingListViewInfQQ) {
        mBettingListViewInfQQ = bettingListViewInfQQ;
        mBettingListModelImpQQ = new BettingListOfHadLotteryModelImpQQ();
    }

    public void getBettingListOfHadLottery(String json) {
        mBettingListModelImpQQ.getBettingListOfHadLottery(this, json);
    }

    @Override
    public void onFinished(Object object) {
        Message msg = (Message) object;
        mBettingListViewInfQQ.sendBettingListOfHadLotteryMssage(msg);
    }
}
