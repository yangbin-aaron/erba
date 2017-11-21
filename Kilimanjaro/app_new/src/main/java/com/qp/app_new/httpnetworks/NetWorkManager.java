package com.qp.app_new.httpnetworks;

import android.app.Dialog;
import android.text.TextUtils;

import com.qp.app_new.AppPrefs;
import com.qp.app_new.configs.ApiConfig;
import com.qp.app_new.contents.AppPrefsContent;
import com.qp.app_new.interfaces.NetListener;
import com.qp.app_new.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by yangbin on 16/7/4.
 * 访问网络集中管理类
 */
public class NetWorkManager {

    private static final String TAG = NetWorkManager.class.getSimpleName() + ">>>";

    private static NetWorkManager mNetWorkManager;
    private OkHttpRequestBuilder mOkHttpRequestBuilder, mOkHttpRequestBuilderList;

    public static NetWorkManager getInstance() {
        if (mNetWorkManager == null) {
            mNetWorkManager = new NetWorkManager();
        }
        return mNetWorkManager;
    }

    public HashMap getSession() {
        HashMap session = new HashMap();
        JSONObject jsonObject = AppPrefsContent.getUser();
        if (jsonObject != null) {
            String token = jsonObject.optString("token");
            String phone = jsonObject.optString("phone");
            if (!TextUtils.isEmpty(token)) {
                session.put("token", token);
            }
            if (!TextUtils.isEmpty(phone)) {
                session.put("phone", phone);
            }
        }
        return session;
    }

    private void createOKHttpBuilder(String url, String json, Dialog dialog, NetListener listener) {
        if (mOkHttpRequestBuilder == null) {
            mOkHttpRequestBuilder = new OkHttpRequestBuilder();
        }
        mOkHttpRequestBuilder.create(url, dialog, json, listener);
    }

    private void createOKHttpBuilderList(String url, String json, Dialog dialog, NetListener listener) {
        if (mOkHttpRequestBuilderList == null) {
            mOkHttpRequestBuilderList = new OkHttpRequestBuilder();
        }
        mOkHttpRequestBuilderList.create(url, dialog, json, true, listener);
    }

    /**
     * 根据提现ID获取进度详情
     */
    public void getSysInfo() {
        String url = ApiConfig.getFullUrl(ApiConfig.MineApi.BASIC_INFO);
        createOKHttpBuilderList(url, StringUtil.getNullJson(), null, new NetListener() {
            @Override
            public void onSuccessResponse(String msg, JSONArray jsonArray) {
                super.onSuccessResponse(msg, jsonArray);
                AppPrefs.getInstance().saveSysInfo(jsonArray);
            }
        });
    }

    /**
     * 检查更新
     *
     * @param dialog
     * @param listener
     */
    public void updateApp(Dialog dialog, NetListener listener) {
        final String url = ApiConfig.getFullUrl(ApiConfig.MineApi.UPDATE_APP);
        String json = StringUtil.getVersionJson();
        createOKHttpBuilder(url, json, dialog, listener);
    }

    /**
     * 登录API
     *
     * @param json     手机号码和密码的json数据
     * @param listener
     */
    public void login(String json, Dialog dialog, NetListener listener) {
        final String url = ApiConfig.getFullUrl(ApiConfig.MineApi.LOGIN);
        createOKHttpBuilder(url, json, dialog, listener);
    }

    /**
     * 发送邮件激活账户
     *
     * @param json
     * @param listener
     */
    public void emailActiveAccount(String json, Dialog dialog, NetListener listener) {
        final String url = ApiConfig.getFullUrl(ApiConfig.MineApi.EMAILACTIVEACCOUNT);
        createOKHttpBuilder(url, json, dialog, listener);
    }

    /**
     * 重置登录密码
     *
     * @param json
     * @param listener
     */
    public void resetPwd(String json, Dialog dialog, NetListener listener) {
        String url = ApiConfig.getFullUrl(ApiConfig.MineApi.LOGIN_PWD_RESET);
        createOKHttpBuilder(url, json, dialog, listener);
    }

    /**
     * 发送注册验证码
     *
     * @param json
     * @param listener
     */
    public void sendRegistSMS(String json, Dialog dialog, NetListener listener) {
        String url = ApiConfig.getFullUrl(ApiConfig.MineApi.REGISTER_SEND_SMS);
        createOKHttpBuilder(url, json, dialog, listener);
    }

    /**
     * 发送重置密码验证码
     *
     * @param json
     * @param listener
     */
    public void sendResetSMS(String json, Dialog dialog, NetListener listener) {
        String url = ApiConfig.getFullUrl(ApiConfig.MineApi.GET_CAPTCHA);
        createOKHttpBuilder(url, json, dialog, listener);
    }

    /**
     * 发送验证码进行验证
     *
     * @param json
     * @param listener
     */
    public void verityCaptcha(String json, Dialog dialog, NetListener listener) {
        String url = ApiConfig.getFullUrl(ApiConfig.MineApi.VERITY_CAPTCHA);
        createOKHttpBuilder(url, json, dialog, listener);
    }

    /**
     * 修改登录密码
     *
     * @param json
     * @param listener
     */
    public void modifyLoginPwd(String json, Dialog dialog, NetListener listener) {
        String url = ApiConfig.getFullUrl(ApiConfig.MineApi.LOGIN_PWD_MODIFY);
        createOKHttpBuilder(url, json, dialog, listener);
    }


    /**
     * 完成注册
     *
     * @param json
     * @param listener
     */
    public void compaleRegist(String json, Dialog dialog, NetListener listener) {
        String url = ApiConfig.getFullUrl(ApiConfig.MineApi.REGISTER_COMLPAE);
        createOKHttpBuilder(url, json, dialog, listener);
    }

    /**
     * 获取游戏列表
     *
     * @param dialog
     * @param listener
     */
    public void getGameList(Dialog dialog, NetListener listener) {
        String url = ApiConfig.getFullUrl(ApiConfig.MineApi.GAME_LIST);
        String json = StringUtil.getNullJson();
        createOKHttpBuilderList(url, json, dialog, listener);
    }

    /**
     * 获取历史开奖数据
     *
     * @param json
     * @param listener
     */
    public void getHistoryLotteryList(String json, Dialog dialog, NetListener listener) {
        String url = ApiConfig.getFullUrl(ApiConfig.HallApi.GET_HISTORY_LOTTERY_LIST);
        createOKHttpBuilderList(url, json, dialog, listener);
    }

    /**
     * 获取未开奖数据
     *
     * @param json
     * @param listener
     */
    public void getNoLotteryList(String json, NetListener listener) {
        String url = ApiConfig.getFullUrl(ApiConfig.HallApi.GET_NO_LOTTERY_LIST);
        createOKHttpBuilderList(url, json, null, listener);
    }

    /**
     * 获取排行榜
     *
     * @param json
     * @param listener
     */
    public void getGameRankingList(String json, Dialog dialog, NetListener listener) {
        String url = ApiConfig.getFullUrl(ApiConfig.RankingApi.GET_RANKING);
        createOKHttpBuilderList(url, json, dialog, listener);
    }

    /**
     * 获取历史订单列表(已经开奖)
     *
     * @param json
     * @param dialog
     */
    public void getBettingListOfHadLottery(String json, Dialog dialog, NetListener listener) {
        String url = ApiConfig.getFullUrl(ApiConfig.BetApiQQ.ORDER_HADLOTTERYBETS_QQ);
        createOKHttpBuilderList(url, json, dialog, listener);
    }

    /**
     * 获取投注模式列表
     *
     * @param dialog
     * @param listener
     */
    public void getBetModeList(Dialog dialog, NetListener listener) {
        String url = ApiConfig.getFullUrl(ApiConfig.BetApiQQ.GAME_BETPATTERNS_QQ);
        createOKHttpBuilderList(url, StringUtil.getNullJson(), dialog, listener);
    }

    /**
     * 获取积分
     *
     * @param listener
     */
    public void getGameCoin(NetListener listener) {
        String url = ApiConfig.getFullUrl(ApiConfig.CoinApi.GET_COIN);
        createOKHttpBuilder(url, StringUtil.getNullJson(), null, listener);
    }


    /**
     * 投注
     *
     * @param json
     * @param listener
     */
    public void orderBet(String json, Dialog dialog, NetListener listener) {
        String url = ApiConfig.getFullUrl(ApiConfig.BetApiQQ.ORDER_BET_QQ);
        createOKHttpBuilder(url, json, dialog, listener);
    }

    /**
     * 获取未开奖的订单列表
     *
     * @param json
     * @param listener
     */
    public void getBettingListOfNoLottery(String json, Dialog dialog, NetListener listener) {
        String url = ApiConfig.getFullUrl(ApiConfig.BetApiQQ.ORDER_NOLOTTERYBETS_QQ);
        createOKHttpBuilderList(url, json, dialog, listener);
    }


    /**
     * 盈亏概览
     *
     * @param json
     * @param listener
     */
    public void getRevenueGeneral(String json, NetListener listener) {
        String url = ApiConfig.getFullUrl(ApiConfig.HallApi.REVENUE_GENERAL);
        createOKHttpBuilder(url, json, null, listener);
    }

    /**
     * 按日盈亏
     *
     * @param json
     * @param listener
     */
    public void getRevenueByDays(String json, NetListener listener) {
        String url = ApiConfig.getFullUrl(ApiConfig.HallApi.REVENUE_DAYS);
        createOKHttpBuilderList(url, json, null, listener);
    }

    /**
     * 按期盈亏
     *
     * @param json
     * @param listener
     */
    public void getRevenueByPeriods(String json, Dialog dialog, NetListener listener) {
        String url = ApiConfig.getFullUrl(ApiConfig.HallApi.REVENUE_PERIODS);
        createOKHttpBuilderList(url, json, dialog, listener);
    }

    /**
     * 获取充值信息列表
     *
     * @param json
     * @param dialog
     * @param listener
     */
    public void getReChargesList(String json, Dialog dialog, NetListener listener) {
        String url = ApiConfig.getFullUrl(ApiConfig.CoinApi.GET_RECHARGES_LIST);
        createOKHttpBuilderList(url, json, dialog, listener);
    }

    /**
     * 获取下注信息列表
     *
     * @param json
     * @param dialog
     * @param listener
     */
    public void getOrdersList(String json, Dialog dialog, NetListener listener) {
        String url = ApiConfig.getFullUrl(ApiConfig.CoinApi.GET_ORDERS_LIST);
        createOKHttpBuilderList(url, json, dialog, listener);
    }
}
