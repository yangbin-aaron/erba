package com.teb.kilimanjaro.mvp.model;

import android.os.Handler;
import android.os.Message;

import com.teb.kilimanjaro.httpnetwork.NetWorkManager;
import com.teb.kilimanjaro.mvp.OnFinishedListener;

import java.util.HashMap;

/**
 * Created by yangbin on 16/8/3.
 */
public class BettingModelImpQQ  {
    private OnFinishedListener mOnFinishedListener;

    public static final String BET = "bet";// 投注
    public static final String GET_MODE_LIST = "get_mode_list";// 获取模式List

    private String type = GET_MODE_LIST;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("type", type);
            hashMap.put("msg", msg);
            mOnFinishedListener.onFinished(hashMap);
        }
    };

    public void getBetPatters(OnFinishedListener onFinishedListener, String json) {
        mOnFinishedListener = onFinishedListener;
        type = GET_MODE_LIST;
        NetWorkManager.getInstance().getBetPatternsListQQ(json, mHandler);
    }

    public void orderBet(OnFinishedListener onFinishedListener, String json) {
        mOnFinishedListener = onFinishedListener;
        type = BET;
        NetWorkManager.getInstance().orderBetQQ(json, mHandler);
    }
}
