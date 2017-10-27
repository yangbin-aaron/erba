package com.teb.kilimanjaro.mvp.model;

import android.os.Handler;
import android.os.Message;

import com.teb.kilimanjaro.httpnetwork.NetWorkManager;
import com.teb.kilimanjaro.mvp.OnFinishedListener;

import java.util.HashMap;

/**
 * Created by yangbin on 16/8/12.
 */
public class BillModelImp {

    private OnFinishedListener mOnFinishedListener;

    public static final String GET_RECHARGE_LIST = "get_recharge_list";
    public static final String GET_WITHDRAW_LIST = "get_withdraw_list";
    private Handler mHandlerR = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("type", GET_RECHARGE_LIST);
            hashMap.put("msg", msg);
            mOnFinishedListener.onFinished(hashMap);
        }
    };

    private Handler mHandlerW = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("type", GET_WITHDRAW_LIST);
            hashMap.put("msg", msg);
            mOnFinishedListener.onFinished(hashMap);
        }
    };

    public void getRechargeList(OnFinishedListener onFinishedListener, String json) {
        mOnFinishedListener = onFinishedListener;
        NetWorkManager.getInstance().getRechargeList(json, mHandlerR);
    }

    public void getWithdrawList(OnFinishedListener onFinishedListener, String json) {
        mOnFinishedListener = onFinishedListener;
        NetWorkManager.getInstance().getWithdrawList(json, mHandlerW);
    }
}

