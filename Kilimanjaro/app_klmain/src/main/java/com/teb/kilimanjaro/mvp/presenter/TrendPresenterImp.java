package com.teb.kilimanjaro.mvp.presenter;

import android.os.Message;

import com.teb.kilimanjaro.mvp.OnFinishedListener;
import com.teb.kilimanjaro.mvp.model.TrendModelImp;
import com.teb.kilimanjaro.mvp.view.TrendViewInf;

/**
 * Created by yangbin on 16/7/8.
 */
public class TrendPresenterImp implements  OnFinishedListener {

    private TrendModelImp mTrendModelImp;

    private TrendViewInf mTrendViewInf;

    public TrendPresenterImp(TrendViewInf trendViewInf) {
        mTrendViewInf = trendViewInf;
        mTrendModelImp = new TrendModelImp();
    }

    public void getChart(String getChartJson, boolean isBig) {
        mTrendModelImp.getChart(this, getChartJson, isBig);
    }


    @Override
    public void onFinished(Object object) {
        mTrendViewInf.sendGetChartMessage((Message) object);
    }
}
