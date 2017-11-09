package com.qp.app_new.httpnetworks;

import android.app.Dialog;
import android.os.Handler;

import com.qp.app_new.configs.ApiConfig;
import com.qp.app_new.interfaces.NetListener;
import com.qp.app_new.utils.StringUtil;

import java.util.HashMap;

/**
 * Created by yangbin on 16/7/4.
 * 访问网络集中管理类
 */
public class NetWorkManager {

    private static final String TAG = NetWorkManager.class.getSimpleName () + ">>>";

    private static NetWorkManager mNetWorkManager;

    public static NetWorkManager getInstance () {
        if (mNetWorkManager == null) {
            mNetWorkManager = new NetWorkManager ();
        }
        return mNetWorkManager;
    }

    public HashMap getSession () {
        HashMap session = new HashMap ();
//        LoginModel model = LoginModel.getUserInfo();
//        if (model != null) {
//            if (model.getToken() != null && !TextUtils.isEmpty(model.getToken())) {
//                session.put("token", model.getToken());
//            }
//            if (model.getData().getPhone() != null && !TextUtils.isEmpty(model.getData().getPhone())) {
//                session.put("phone", model.getData().getPhone());
//            }
//        }
        return session;
    }

    /**
     * 检查更新
     *
     * @param json
     * @param handler
     */
    public void updateApp (String json, final Handler handler) {
//        final String url = ApiConfig.getFullUrl(ApiConfig.MineApi.UPDATE_APP);
//        new OkHttpRequestBuilder()
//                .create(url, json, handler)
//                .callBack(new OkHttpRequestBuilder.OkHttpRequestBuilderCallBack() {
//                    @Override
//                    public void onRespone(String response) {
//                        VersionStatusModel result = new Gson().fromJson(response, VersionStatusModel.class);
//                        handler.obtainMessage(HandlerConfig.WHAT_POST_SUCCESS, result.getData()).sendToTarget();
//                    }
//                });
    }

    /**
     * 登录API
     *
     * @param json     手机号码和密码的json数据
     * @param listener
     */
    public void login (String json, Dialog dialog, NetListener listener) {
        final String url = ApiConfig.getFullUrl (ApiConfig.MineApi.LOGIN);
        new OkHttpRequestBuilder ().create (url, dialog, json, listener);
    }

    /**
     * 发送邮件激活账户
     *
     * @param json
     * @param handler
     */
    public void emailActiveAccount (String json, final Handler handler) {
//        final String url = ApiConfig.getFullUrl(ApiConfig.MineApi.EMAILACTIVEACCOUNT);
//        new OkHttpRequestBuilder()
//                .create(url, json, handler)
//                .callBack(new OkHttpRequestBuilder.OkHttpRequestBuilderCallBack() {
//                    @Override
//                    public void onRespone(String response) {
//                        handler.obtainMessage(HandlerConfig.WHAT_POST_SUCCESS).sendToTarget();
//                    }
//                });
    }

    /**
     * 发送邮件激活账户
     *
     * @param json
     * @param handler
     */
    public void emailResetPayPassword (String json, final Handler handler) {
//        final String url = ApiConfig.getFullUrl(ApiConfig.MineApi.EMAILRESETPAYPASSWORD);
//        new OkHttpRequestBuilder()
//                .create(url, json, handler)
//                .callBack(new OkHttpRequestBuilder.OkHttpRequestBuilderCallBack() {
//                    @Override
//                    public void onRespone(String response) {
//                        handler.obtainMessage(HandlerConfig.WHAT_POST_SUCCESS).sendToTarget();
//                    }
//                });
    }

    /**
     * 发送注册验证码
     *
     * @param json
     * @param handler
     */
    public void sendRegistSMS (String json, final Handler handler) {
//        String url = ApiConfig.getFullUrl(ApiConfig.MineApi.REGISTER_SEND_SMS);
//        new OkHttpRequestBuilder()
//                .create(url, json, handler)
//                .callBack(new OkHttpRequestBuilder.OkHttpRequestBuilderCallBack() {
//                    @Override
//                    public void onRespone(String response) {
//                        SendSMSModel result = new Gson().fromJson(response, SendSMSModel.class);
//                        handler.obtainMessage(HandlerConfig.WHAT_POST_SUCCESS, result.getData().getExpired(), 0).sendToTarget();
//                    }
//                });
    }

    /**
     * 发送重置密码验证码
     *
     * @param json
     * @param handler
     */
    public void getCaptcha (String json, final Handler handler) {
//        String url = ApiConfig.getFullUrl(ApiConfig.MineApi.GET_CAPTCHA);
//        new OkHttpRequestBuilder()
//                .create(url, json, handler)
//                .callBack(new OkHttpRequestBuilder.OkHttpRequestBuilderCallBack() {
//                    @Override
//                    public void onRespone(String response) {
//                        SendSMSModel result = new Gson().fromJson(response, SendSMSModel.class);
//                        handler.obtainMessage(HandlerConfig.WHAT_POST_SUCCESS, result.getData().getExpired(), 0).sendToTarget();
//                    }
//                });
    }

    /**
     * 发送验证码进行验证
     *
     * @param json
     * @param handler
     */
    public void verityCaptcha (String json, final Handler handler) {
//        String url = ApiConfig.getFullUrl(ApiConfig.MineApi.VERITY_CAPTCHA);
//        new OkHttpRequestBuilder()
//                .create(url, json, handler)
//                .callBack(new OkHttpRequestBuilder.OkHttpRequestBuilderCallBack() {
//                    @Override
//                    public void onRespone(String response) {
//                        CaptchaModel result = new Gson().fromJson(response, CaptchaModel.class);
//                        //  发送token
//                        handler.obtainMessage(HandlerConfig.WHAT_POST_SUCCESS, result.getCaptchaToken()).sendToTarget();
//                    }
//                });
    }

    /**
     * 完成注册
     *
     * @param json
     * @param handler
     */
    public void compaleRegist (String json, final Handler handler) {
//        String url = ApiConfig.getFullUrl(ApiConfig.MineApi.REGISTER_COMLPAE);
//        new OkHttpRequestBuilder()
//                .create(url, json, handler)
//                .callBack(new OkHttpRequestBuilder.OkHttpRequestBuilderCallBack() {
//                    @Override
//                    public void onRespone(String response) {
//                        LoginModel result = new Gson().fromJson(response, LoginModel.class);
//                        handler.obtainMessage(HandlerConfig.WHAT_POST_SUCCESS).sendToTarget();
//                    }
//                });
    }

    /**
     * 修改登录密码
     *
     * @param json
     * @param handler
     */
    public void updateLoginPwd (String json, final Handler handler) {
//        String url = ApiConfig.getFullUrl(ApiConfig.MineApi.LOGIN_PWD_MODIFY);
//        new OkHttpRequestBuilder()
//                .create(url, json, handler)
//                .callBack(new OkHttpRequestBuilder.OkHttpRequestBuilderCallBack() {
//                    @Override
//                    public void onRespone(String response) {
//                        AppPrefs.getInstance().saveUserJson(null);// 清空登录信息
//                        handler.obtainMessage(HandlerConfig.WHAT_POST_SUCCESS).sendToTarget();
//                    }
//                });
    }

    /**
     * 修改支付密码
     *
     * @param json
     * @param handler
     */
    public void updatePayPwd (String json, final Handler handler) {
//        String url = ApiConfig.getFullUrl(ApiConfig.MineApi.PAY_PWD_MODIFY);
//        new OkHttpRequestBuilder()
//                .create(url, json, handler)
//                .callBack(new OkHttpRequestBuilder.OkHttpRequestBuilderCallBack() {
//                    @Override
//                    public void onRespone(String response) {
//                        handler.obtainMessage(HandlerConfig.WHAT_POST_SUCCESS).sendToTarget();
//                    }
//                });
    }

    /**
     * 重置登录密码
     *
     * @param json
     * @param handler
     */
    public void resetPwd (String json, final Handler handler) {
//        String url = ApiConfig.getFullUrl(ApiConfig.MineApi.LOGIN_PWD_RESET);
//        new OkHttpRequestBuilder()
//                .create(url, json, handler)
//                .callBack(new OkHttpRequestBuilder.OkHttpRequestBuilderCallBack() {
//                    @Override
//                    public void onRespone(String response) {
//                        AppPrefs.getInstance().saveUserJson(null);// 清空登录信息
//                        handler.obtainMessage(HandlerConfig.WHAT_POST_SUCCESS).sendToTarget();
//                    }
//                });
    }

    /**
     * 获取游戏列表
     *
     * @param dialog
     * @param listener
     */
    public void getGameList (Dialog dialog, NetListener listener) {
        String url = ApiConfig.getFullUrl (ApiConfig.MineApi.GAME_LIST);
        String json = StringUtil.getNullJson ();
        new OkHttpRequestBuilder ().create (url, dialog, json, listener);
    }

    /**
     * 获取历史开奖数据
     *
     * @param json
     * @param handler
     */
    public void getHistoryLotteryList (String json, final Handler handler) {
//        String url = ApiConfig.getFullUrl(ApiConfig.HallApi.GET_HISTORY_LOTTERY_LIST);
//        new OkHttpRequestBuilder()
//                .create(url, json, handler)
//                .callBack(new OkHttpRequestBuilder.OkHttpRequestBuilderCallBack() {
//                    @Override
//                    public void onRespone(String response) {
//                        LotteryModel result = new Gson().fromJson(response, LotteryModel.class);
//                        handler.obtainMessage(HandlerConfig.WHAT_POST_SUCCESS, result.getData()).sendToTarget();
//                    }
//                });
    }

    /**
     * 获取未开奖数据
     *
     * @param json
     * @param handler
     */
    public void getNoLotteryList (String json, final Handler handler) {
//        String url = ApiConfig.getFullUrl(ApiConfig.HallApi.GET_NO_LOTTERY_LIST);
//        new OkHttpRequestBuilder()
//                .create(url, json, handler)
//                .callBack(new OkHttpRequestBuilder.OkHttpRequestBuilderCallBack() {
//                    @Override
//                    public void onRespone(String response) {
//                        LotteryModel result = new Gson().fromJson(response, LotteryModel.class);
//                        List<LotteryModel.LotteryData> lotteryDatas = result.getData();
//                        // 保存当前期数ID
//                        if (lotteryDatas.size() > 0) {
//                            AppPrefs.getInstance().saveCurrentBetInfo(lotteryDatas.get(lotteryDatas.size() - 1));
//                        }
//                        // 做标记
//                        for (int i = 0; i < lotteryDatas.size(); i++) {
//                            LotteryModel.LotteryData data = lotteryDatas.get(i);
//                            data.setNoLottery(true);
//                            lotteryDatas.set(i, data);
//                        }
//                        handler.obtainMessage(HandlerConfig.WHAT_POST_SUCCESS, lotteryDatas).sendToTarget();
//                    }
//                });
    }

    /**
     * 获取游戏趋势图
     *
     * @param json
     * @param handler
     * @param isBig   是否是大图
     */
    public void getChart (String json, boolean isBig, final Handler handler) {
//        Log.e("aaron", "----------------------getChart>>>" + json);
//        String url = ApiConfig.getFullUrl(ApiConfig.TrendApi.GET_BIG_CHART);
//        if (!isBig) {
//            url = ApiConfig.getFullUrl(ApiConfig.TrendApi.GET_SMALL_CHART);
//        }
//        new OkHttpRequest.Builder()
//                .url(url)
//                .content(json)
//                .post(new ResultCallback<String>() {
//                    @Override
//                    public void onBefore(Request request) {
//                        super.onBefore(request);
//                    }
//
//                    @Override
//                    public void onError(Request request, Exception e) {
//                        handler.obtainMessage(HandlerConfig.WHAT_NET_ERROR).sendToTarget();
//                    }
//
//                    @Override
//                    public void onResponse(String response) {
////                        Log.e("result", TAG + "getBigChart---" + response);
//                        handler.obtainMessage(HandlerConfig.WHAT_POST_SUCCESS, response).sendToTarget();
//                    }
//
//                    @Override
//                    public void onAfter() {
//                        super.onAfter();
//                    }
//                });
    }

    /**
     * 获取游戏倍数
     *
     * @param json
     * @param handler
     */
    public void getOddsList (String json, final Handler handler) {
//        String url = ApiConfig.getFullUrl(ApiConfig.HallApi.GAME_ODDS);
//        new OkHttpRequestBuilder()
//                .create(url, json, handler)
//                .callBack(new OkHttpRequestBuilder.OkHttpRequestBuilderCallBack() {
//                    @Override
//                    public void onRespone(String response) {
//                        OddsListModel result = new Gson().fromJson(response, OddsListModel.class);
//                        handler.obtainMessage(HandlerConfig.WHAT_POST_SUCCESS, result.getData()).sendToTarget();
//                    }
//                });
    }

    /**
     * 下注
     *
     * @param json
     * @param handler
     */
    public void orderBet (String json, final Handler handler) {
//        String url = ApiConfig.getFullUrl(ApiConfig.HallApi.ORDER_BET);
//        new OkHttpRequestBuilder()
//                .create(url, json, handler)
//                .callBack(new OkHttpRequestBuilder.OkHttpRequestBuilderCallBack() {
//                    @Override
//                    public void onRespone(String response) {
//                        handler.obtainMessage(HandlerConfig.WHAT_POST_SUCCESS).sendToTarget();
//                    }
//                });
    }

    /**
     * 中奖名单
     *
     * @param json
     * @param handler
     */
    public void orderWinnersList (String json, final Handler handler) {
//        String url = ApiConfig.getFullUrl(ApiConfig.HallApi.ORDER_WINNERSLIST);
//        new OkHttpRequestBuilder()
//                .create(url, json, handler)
//                .callBack(new OkHttpRequestBuilder.OkHttpRequestBuilderCallBack() {
//                    @Override
//                    public void onRespone(String response) {
//                        WinnersListModel result = new Gson().fromJson(response, WinnersListModel.class);
//                        handler.obtainMessage(HandlerConfig.WHAT_POST_SUCCESS, result.getData()).sendToTarget();
//                    }
//                });
    }

    /**
     * 我的投注（已经开奖数据）
     *
     * @param json
     * @param handler
     */
    public void orderHadLotteryBets (String json, final Handler handler) {
//        String url = ApiConfig.getFullUrl(ApiConfig.HallApi.ORDER_HADLOTTERYBETS);
//        new OkHttpRequestBuilder()
//                .create(url, json, handler)
//                .callBack(new OkHttpRequestBuilder.OkHttpRequestBuilderCallBack() {
//                    @Override
//                    public void onRespone(String response) {
//                        OddsListModel result = new Gson().fromJson(response, OddsListModel.class);
//                        handler.obtainMessage(HandlerConfig.WHAT_POST_SUCCESS, result.getData()).sendToTarget();
//                    }
//                });
    }

    /**
     * 已经投注（未开奖数据）
     *
     * @param json
     * @param handler
     */
    public void orderNolotteryBets (String json, final Handler handler) {
//        String url = ApiConfig.getFullUrl(ApiConfig.HallApi.ORDER_NOLOTTERYBETS);
//        new OkHttpRequestBuilder()
//                .create(url, json, handler)
//                .callBack(new OkHttpRequestBuilder.OkHttpRequestBuilderCallBack() {
//                    @Override
//                    public void onRespone(String response) {
//                        OddsListModel result = new Gson().fromJson(response, OddsListModel.class);
//                        handler.obtainMessage(HandlerConfig.WHAT_POST_SUCCESS, result.getData()).sendToTarget();
//                    }
//                });
    }

    /**
     * 盈亏概览
     *
     * @param json
     * @param handler
     */
    public void getRevenueGeneral (String json, final Handler handler) {
//        String url = ApiConfig.getFullUrl(ApiConfig.HallApi.REVENUE_GENERAL);
//        new OkHttpRequestBuilder()
//                .create(url, json, handler)
//                .callBack(new OkHttpRequestBuilder.OkHttpRequestBuilderCallBack() {
//                    @Override
//                    public void onRespone(String response) {
//                        RevenueGeneralModel result = new Gson().fromJson(response, RevenueGeneralModel.class);
//                        handler.obtainMessage(HandlerConfig.WHAT_POST_SUCCESS, result.getData()).sendToTarget();
//                    }
//                });
    }

    /**
     * 按日盈亏
     *
     * @param json
     * @param handler
     */
    public void getRevenueByDays (String json, final Handler handler) {
//        String url = ApiConfig.getFullUrl(ApiConfig.HallApi.REVENUE_DAYS);
//        new OkHttpRequestBuilder()
//                .create(url, json, handler)
//                .callBack(new OkHttpRequestBuilder.OkHttpRequestBuilderCallBack() {
//                    @Override
//                    public void onRespone(String response) {
//                        RevenueByDaysModel result = new Gson().fromJson(response, RevenueByDaysModel.class);
//                        handler.obtainMessage(HandlerConfig.WHAT_POST_SUCCESS, result.getData()).sendToTarget();
//                    }
//                });
    }

    /**
     * 按期盈亏
     *
     * @param json
     * @param handler
     */
    public void getRevenueByPeriods (String json, final Handler handler) {
//        String url = ApiConfig.getFullUrl(ApiConfig.HallApi.REVENUE_PERIODS);
//        new OkHttpRequestBuilder()
//                .create(url, json, handler)
//                .callBack(new OkHttpRequestBuilder.OkHttpRequestBuilderCallBack() {
//                    @Override
//                    public void onRespone(String response) {
//                        RevenueByPeriodsModel result = new Gson().fromJson(response, RevenueByPeriodsModel.class);
//                        handler.obtainMessage(HandlerConfig.WHAT_POST_SUCCESS, result.getData()).sendToTarget();
//                    }
//                });
    }

    // **********************QQ群玩法相关API*************START************

    /**
     * 获取未开奖的订单列表
     *
     * @param json
     * @param handler
     */
    public void getBettingListOfNoLotteryQQ (String json, final Handler handler) {
//        String url = ApiConfig.getFullUrl(ApiConfig.BetApiQQ.ORDER_NOLOTTERYBETS_QQ);
//        new OkHttpRequestBuilder()
//                .create(url, json, handler)
//                .callBack(new OkHttpRequestBuilder.OkHttpRequestBuilderCallBack() {
//                    @Override
//                    public void onRespone(String response) {
//                        BettingListModelQQ result = new Gson().fromJson(response, BettingListModelQQ.class);
//                        handler.obtainMessage(HandlerConfig.WHAT_POST_SUCCESS, result.getData()).sendToTarget();
//                    }
//                });
    }

    /**
     * 获取历史订单列表(已经开奖)
     *
     * @param json
     * @param handler
     */
    public void getBettingListOfHadLotteryQQ (String json, final Handler handler) {
//        String url = ApiConfig.getFullUrl(ApiConfig.BetApiQQ.ORDER_HADLOTTERYBETS_QQ);
//        new OkHttpRequestBuilder()
//                .create(url, json, handler)
//                .callBack(new OkHttpRequestBuilder.OkHttpRequestBuilderCallBack() {
//                    @Override
//                    public void onRespone(String response) {
//                        BettingListModelQQ result = new Gson().fromJson(response, BettingListModelQQ.class);
//                        handler.obtainMessage(HandlerConfig.WHAT_POST_SUCCESS, result.getData()).sendToTarget();
//                    }
//                });
    }

    /**
     * 投注
     *
     * @param json
     * @param handler
     */
    public void orderBetQQ (String json, final Handler handler) {
//        String url = ApiConfig.getFullUrl(ApiConfig.BetApiQQ.ORDER_BET_QQ);
//        new OkHttpRequestBuilder()
//                .create(url, json, handler)
//                .callBack(new OkHttpRequestBuilder.OkHttpRequestBuilderCallBack() {
//                    @Override
//                    public void onRespone(String response) {
//                        handler.obtainMessage(HandlerConfig.WHAT_POST_SUCCESS).sendToTarget();
//                    }
//                });
    }

    /**
     * 获取投注模式列表
     *
     * @param json
     * @param handler
     */
    public void getBetPatternsListQQ (String json, final Handler handler) {
//        String url = ApiConfig.getFullUrl(ApiConfig.BetApiQQ.GAME_BETPATTERNS_QQ);
//        new OkHttpRequestBuilder()
//                .create(url, json, handler)
//                .callBack(new OkHttpRequestBuilder.OkHttpRequestBuilderCallBack() {
//                    @Override
//                    public void onRespone(String response) {
//                        BetPatternsModelQQ result = new Gson().fromJson(response, BetPatternsModelQQ.class);
//                        handler.obtainMessage(HandlerConfig.WHAT_POST_SUCCESS, result.getData()).sendToTarget();
//                    }
//                });
    }
    // **********************QQ群玩法相关API*************END************

    // **********************游戏币充值提现相关API*************START************

    /**
     * 获取充值账单明细
     *
     * @param json
     * @param handler
     */
    public void getRechargeList (String json, final Handler handler) {
//        String url = ApiConfig.getFullUrl(ApiConfig.CoinApi.RECHARGE_LIST);
//        new OkHttpRequestBuilder()
//                .create(url, json, handler)
//                .callBack(new OkHttpRequestBuilder.OkHttpRequestBuilderCallBack() {
//                    @Override
//                    public void onRespone(String response) {
//                        RechargeListModel result = new Gson().fromJson(response, RechargeListModel.class);
//                        handler.obtainMessage(HandlerConfig.WHAT_POST_SUCCESS, result.getData()).sendToTarget();
//                    }
//                });
    }

    /**
     * 验证提现密码
     *
     * @param json
     * @param handler
     */
    public void verifyPayPwd (String json, final Handler handler) {
//        String url = ApiConfig.getFullUrl(ApiConfig.CoinApi.VERIFY_PAYPWD);
//        new OkHttpRequestBuilder()
//                .create(url, json, handler)
//                .callBack(new OkHttpRequestBuilder.OkHttpRequestBuilderCallBack() {
//                    @Override
//                    public void onRespone(String response) {
//                        handler.obtainMessage(HandlerConfig.WHAT_POST_SUCCESS).sendToTarget();
//                    }
//                });
    }

    /**
     * 获取提现列表
     *
     * @param json
     * @param handler
     */
    public void getWithdrawList (String json, final Handler handler) {
//        String url = ApiConfig.getFullUrl(ApiConfig.CoinApi.WITHDRAW_LIST);
//        new OkHttpRequestBuilder()
//                .create(url, json, handler)
//                .callBack(new OkHttpRequestBuilder.OkHttpRequestBuilderCallBack() {
//                    @Override
//                    public void onRespone(String response) {
//                        WithdrawListModel result = new Gson().fromJson(response, WithdrawListModel.class);
//                        handler.obtainMessage(HandlerConfig.WHAT_POST_SUCCESS, result.getData()).sendToTarget();
//                    }
//                });
    }

    /**
     * 根据提现ID获取进度详情
     *
     * @param json
     * @param handler
     */
    public void getWithdrawDetail (String json, final Handler handler) {
//        String url = ApiConfig.getFullUrl(ApiConfig.CoinApi.WITHDRAW_DETAIL);
//        new OkHttpRequestBuilder()
//                .create(url, json, handler)
//                .callBack(new OkHttpRequestBuilder.OkHttpRequestBuilderCallBack() {
//                    @Override
//                    public void onRespone(String response) {
//                        WithdrawDetailModel result = new Gson().fromJson(response, WithdrawDetailModel.class);
//                        handler.obtainMessage(HandlerConfig.WHAT_POST_SUCCESS, result.getData()).sendToTarget();
//                    }
//                });
    }

    // **********************游戏币充值提现相关API*************END************

    /**
     * 根据提现ID获取进度详情
     *
     * @param json
     */
    public void getBasicInfo (String json) {
//        String url = ApiConfig.getFullUrl(ApiConfig.MineApi.BASIC_INFO);
//        new OkHttpRequestBuilder()
//                .create(url, json, new Handler())
//                .callBack(new OkHttpRequestBuilder.OkHttpRequestBuilderCallBack() {
//                    @Override
//                    public void onRespone(String response) {
//                        if (!TextUtils.isEmpty(response)) {
//                            AppPrefs.getInstance().saveBasicInfo(response);
//                        }
////                        BasicInfoModel result = new Gson().fromJson(response, BasicInfoModel.class);
////                        handler.obtainMessage(HandlerConfig.WHAT_POST_SUCCESS, result.getData()).sendToTarget();
//                    }
//                });
    }

}
