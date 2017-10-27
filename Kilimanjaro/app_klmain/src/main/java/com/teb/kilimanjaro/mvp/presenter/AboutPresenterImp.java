package com.teb.kilimanjaro.mvp.presenter;

import android.os.Message;

import com.teb.kilimanjaro.mvp.OnFinishedListener;
import com.teb.kilimanjaro.mvp.model.AboutModelImp;
import com.teb.kilimanjaro.mvp.view.AboutViewInf;

/**
 * Created by yangbin on 16/8/22.
 */
public class AboutPresenterImp implements OnFinishedListener{

    private AboutViewInf mAboutViewInf;
    private AboutModelImp mAboutModelImp;

    public AboutPresenterImp(AboutViewInf aboutViewInf) {
        mAboutViewInf = aboutViewInf;
        mAboutModelImp = new AboutModelImp();
    }

    public void getUpdateApp(String json) {
        mAboutModelImp.getUpdateApp(this, json);
    }

    @Override
    public void onFinished(Object object) {
        mAboutViewInf.sendVersionMessage((Message) object);
    }
}
