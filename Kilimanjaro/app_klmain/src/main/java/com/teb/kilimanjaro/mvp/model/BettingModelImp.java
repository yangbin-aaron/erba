package com.teb.kilimanjaro.mvp.model;

import android.os.Handler;
import android.os.Message;

import com.teb.kilimanjaro.configs.HandlerConfig;
import com.teb.kilimanjaro.httpnetwork.NetWorkManager;
import com.teb.kilimanjaro.models.entry.hall.OddsListModel;
import com.teb.kilimanjaro.mvp.OnFinishedListener;

import java.util.HashMap;
import java.util.List;

/**
 * Created by yangbin on 16/7/14.
 */
public class BettingModelImp {

    private OnFinishedListener mOnFinishedListener;

    private String type = GET_ODDS_DATA;
    public static final String GET_ODDS_DATA = "odds";
    public static final String BET = "bet";
    
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("type", type);
            if (type.equals(GET_ODDS_DATA) && msg.what == HandlerConfig.WHAT_POST_SUCCESS) {
                List<OddsListModel.OddsData> oddsDatas = (List<OddsListModel.OddsData>) msg.obj;
                for (int i = 0; i < oddsDatas.size(); i++) {
                    OddsListModel.OddsData data = oddsDatas.get(i);
                    data.setBeted( data.getBetCoin() > 0);// 以后不允许修改
                    data.setSelected(data.getBetCoin() > 0);
                    oddsDatas.set(i, data);
                }
                msg.obj = oddsDatas;
            }
            hashMap.put("msg", msg);
            mOnFinishedListener.onFinished(hashMap);
        }
    };

    public void getMybetList(OnFinishedListener onFinishedListener, String json) {
        mOnFinishedListener = onFinishedListener;
        type = GET_ODDS_DATA;
        NetWorkManager.getInstance().orderNolotteryBets(json, mHandler);
    }

    public void getOddsList(OnFinishedListener onFinishedListener, String oddsJson) {
        mOnFinishedListener = onFinishedListener;
        type = GET_ODDS_DATA;
        NetWorkManager.getInstance().getOddsList(oddsJson, mHandler);
    }

    public void betOrder(OnFinishedListener onFinishedListener, String betJson) {
        mOnFinishedListener = onFinishedListener;
        type = BET;
        NetWorkManager.getInstance().orderBet(betJson, mHandler);
    }
}
