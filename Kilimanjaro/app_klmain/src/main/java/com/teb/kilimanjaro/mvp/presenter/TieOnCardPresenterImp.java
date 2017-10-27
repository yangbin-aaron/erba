package com.teb.kilimanjaro.mvp.presenter;

import android.os.Message;

import com.teb.kilimanjaro.mvp.OnFinishedListener;
import com.teb.kilimanjaro.mvp.model.TieOnCardModelImp;
import com.teb.kilimanjaro.mvp.view.TieOnCardViewInf;

/**
 * Created by yangbin on 16/8/17.
 */
public class TieOnCardPresenterImp implements OnFinishedListener {

    private TieOnCardModelImp mTieOnCardModelImp;
    private TieOnCardViewInf mTieOnCardViewInf;

    public TieOnCardPresenterImp(TieOnCardViewInf tieOnCardViewInf) {
        mTieOnCardViewInf = tieOnCardViewInf;
        mTieOnCardModelImp = new TieOnCardModelImp();
    }

    public void tieOnCard(String json) {
        mTieOnCardModelImp.tieOnCard(this, json);
    }

    @Override
    public void onFinished(Object object) {
        Message msg = (Message) object;
        mTieOnCardViewInf.sendTieOnCardMessage(msg);
    }
}
