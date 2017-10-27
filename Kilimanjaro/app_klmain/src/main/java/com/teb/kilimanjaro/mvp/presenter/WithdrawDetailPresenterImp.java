package com.teb.kilimanjaro.mvp.presenter;

import android.os.Message;

import com.teb.kilimanjaro.mvp.OnFinishedListener;
import com.teb.kilimanjaro.mvp.model.WithdrawDetailModelImp;
import com.teb.kilimanjaro.mvp.view.WithdrawDetailViewInf;

/**
 * Created by yangbin on 16/8/18.
 */
public class WithdrawDetailPresenterImp implements OnFinishedListener {

    private WithdrawDetailModelImp mWithdrawDetailModelImp;
    private WithdrawDetailViewInf mWithdrawDetailViewInf;

    public WithdrawDetailPresenterImp(WithdrawDetailViewInf withdrawDetailViewInf) {
        mWithdrawDetailViewInf = withdrawDetailViewInf;
        mWithdrawDetailModelImp = new WithdrawDetailModelImp();
    }

    public void getWithdrawDetail(String json) {
        mWithdrawDetailModelImp.getWithdrawDetail(this, json);
    }

    @Override
    public void onFinished(Object object) {
        mWithdrawDetailViewInf.sendDetailMessage((Message) object);
    }

}
