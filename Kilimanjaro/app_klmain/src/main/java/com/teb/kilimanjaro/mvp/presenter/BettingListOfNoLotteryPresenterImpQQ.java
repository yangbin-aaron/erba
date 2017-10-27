package com.teb.kilimanjaro.mvp.presenter;

import android.os.Message;

import com.teb.kilimanjaro.mvp.OnFinishedListener;
import com.teb.kilimanjaro.mvp.model.BettingListOfNoLottertModelImpQQ;
import com.teb.kilimanjaro.mvp.view.BettingListOfNoLotteryViewInfQQ;

/**
 * Created by yangbin on 16/8/3.
 */
public class BettingListOfNoLotteryPresenterImpQQ implements OnFinishedListener {

    private BettingListOfNoLotteryViewInfQQ mBettingListOfNoLottertViewInfQQ;
    private BettingListOfNoLottertModelImpQQ mBettingListOfNoLottertModelImpQQ;

    public BettingListOfNoLotteryPresenterImpQQ(BettingListOfNoLotteryViewInfQQ bettingListOfNoLottertViewInfQQ) {
        mBettingListOfNoLottertViewInfQQ = bettingListOfNoLottertViewInfQQ;
        mBettingListOfNoLottertModelImpQQ = new BettingListOfNoLottertModelImpQQ();
    }

    public void getBettingListOfNoLottery(String json) {
        mBettingListOfNoLottertModelImpQQ.getBettingListOfNoLottery(this, json);
    }

    @Override
    public void onFinished(Object object) {
        Message msg = (Message) object;
        mBettingListOfNoLottertViewInfQQ.sendBettingListOfNoLotteryMssage(msg);
    }
}
