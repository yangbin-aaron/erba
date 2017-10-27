package com.teb.kilimanjaro.mvp.presenter;

import android.os.Message;

import com.teb.kilimanjaro.mvp.OnFinishedListener;
import com.teb.kilimanjaro.mvp.model.MainModelImp;
import com.teb.kilimanjaro.mvp.view.MainViewInf;

/**
 * Created by yangbin on 16/8/6.
 */
public class MainPresenterImp implements OnFinishedListener {

    private MainModelImp mMainModelImp;
    private MainViewInf mMainViewInf;

    private int type = 0;

    public MainPresenterImp(MainViewInf mainViewInf) {
        mMainViewInf = mainViewInf;
        mMainModelImp = new MainModelImp();
    }

    public void getGameList(String json) {
        type = 0;
        mMainModelImp.getGameList(this, json);
    }

    public void getUpdateApp(String json) {
        type = 1;
        mMainModelImp.getUpdateApp(this, json);
    }

    @Override
    public void onFinished(Object object) {
        Message msg = (Message) object;
        switch (type) {
            case 0:
                mMainViewInf.sendGameListMessage(msg);
                break;
            case 1:
                mMainViewInf.sendVersionMessage(msg);
                break;
        }
    }
}
