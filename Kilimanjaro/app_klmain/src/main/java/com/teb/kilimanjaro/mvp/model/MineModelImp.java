package com.teb.kilimanjaro.mvp.model;

import android.os.Handler;
import android.os.Message;

import com.teb.kilimanjaro.AppPrefs;
import com.teb.kilimanjaro.httpnetwork.NetWorkManager;
import com.teb.kilimanjaro.mvp.OnFinishedListener;

import java.util.HashMap;

/**
 * Created by yangbin on 16/7/5.
 * 我的  model层方法类
 */
public class MineModelImp {

    private OnFinishedListener mOnFinishedListener;

    public static final String VERIFY_PAYPWD = "verifyPayPwd";
    public static final String GET_PREPAREINFO = "getPrepareInfo";
    private String type = "";
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

    /**
     * 退出登录
     */
    public void logout() {
        // 清空UserJson数据和GameListJson数据
        AppPrefs.getInstance().saveUserJson(null);
        AppPrefs.getInstance().saveGameListJson(null);
    }

    public void verifyPayPwd(OnFinishedListener onFinishedListener, String json) {
        mOnFinishedListener = onFinishedListener;
        type = VERIFY_PAYPWD;
        NetWorkManager.getInstance().verifyPayPwd(json, mHandler);
    }

    public void getPrepareInfo(OnFinishedListener onFinishedListener, String json) {
        this.mOnFinishedListener = onFinishedListener;
        type = GET_PREPAREINFO;
        NetWorkManager.getInstance().getPrepareInfo(json, mHandler);
    }
}
