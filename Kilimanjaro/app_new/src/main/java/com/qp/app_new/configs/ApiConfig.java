package com.qp.app_new.configs;

import com.qp.app_new.App;

/**
 * Created by yangbin on 16/7/4.
 * API管理类
 */
public class ApiConfig {


    private static final String SCHEME_HOST_DEV = "http://iplay28.chenfu3991.com/spouting";// dev域名
//    private static final String SCHEME_HOST_PRO = "http://www.iplay28.com/spouting/1.0";// pro
    private static final String SCHEME_HOST_PRO = "http://iplay28.chenfu3991.com/spouting";// pro

    public static String getFullUrl(String url) {
        if (App.IS_DEV) {
            return SCHEME_HOST_DEV + url;
        } else {
            return SCHEME_HOST_PRO + url;
        }
    }

    /**
     * 我的  相关
     */
    public interface MineApi {
        String LOGIN = "/user/login";// 登录
        String EMAILACTIVEACCOUNT = "/user/emailActiveAccount";// 发送邮件激活账户
        String EMAILRESETPAYPASSWORD = "/user/emailResetPayPassword";// 发送邮件重置支付密码
        String REGISTER_SEND_SMS = "/user/captcha/registerSend";// 发送注册验证码
        String GET_CAPTCHA = "/user/captcha/send";// 发送验证码
        String VERITY_CAPTCHA = "/user/captcha/auth";// 验证验证码
        String REGISTER_COMLPAE = "/user/register";// 完成注册
        String LOGIN_PWD_MODIFY = "/user/loginPasswordModify";// 修改登录密码
        String LOGIN_PWD_RESET = "/user/resetLoginPassword";// 重置登录密码
        String PAY_PWD_MODIFY = "/user/payPasswordModify";// 修改支付密码
        String GAME_LIST = "/game/list";// 游戏列表（我的界面）
        String UPDATE_APP = "/user/auth/checkVersion";// 获取版本信息
        String BASIC_INFO = "/basicInfo";// 客服信息
    }

    /**
     * 大厅  相关
     */
    public interface HallApi {
        String GET_HISTORY_LOTTERY_LIST = "/lottery/hadLotteryList";// 游戏历史数据模块 - 历史数据列表
        String GET_NO_LOTTERY_LIST = "/lottery/noLotteryList";// 游戏历史数据模块 - 未开奖数据列表
        String ORDER_BET = "/order/bet";// 下注
        String ORDER_NOLOTTERYBETS = "/order/noLotteryBets";// 游戏下注信息(未开奖)    ----  已经投注的列表
        String ORDER_HADLOTTERYBETS = "/order/hadLotteryBets";// 游戏下注信息(已开奖)    ----  我的投注
        String ORDER_WINNERSLIST = "/order/winnersList";// 中奖名单
        String GAME_ODDS = "/game/odds";// 游戏倍数
        String REVENUE_PERIODS = "/revenue/periods";// 按期亏盈
        String REVENUE_DAYS = "/revenue/days";// 按日亏盈
        String REVENUE_GENERAL = "/revenue/general";// 亏盈概览
    }

    /**
     * 趋势  相关
     */
    public interface TrendApi {
        String GET_BIG_CHART = "/game/bChart";// 游戏趋势图大图
        String GET_SMALL_CHART = "/game/sChart";// 游戏趋势图小图
    }

    public interface CoinApi {
        String GET_COIN = "/user/coin";// 获取积分
        String PAY_NO = "/payment/prepareInfo";// 获取支付订单ID
        String RECHARGE_LIST = "/payment/records";// 获取充值账单明细
        String VERIFY_PAYPWD = "/user/auth/payPassword";// 验证提现密码
        String GET_PREPAREINFO = "/drawModule/prepareInfo";// 获取绑卡信息
        String BIND_BANKCARD = "/user/auth/bindBankCard";// 绑卡银行卡
        String WITHDRAW = "/drawModule/drawAmt";// 提现申请
        String WITHDRAW_LIST = "/drawModule/list";// 提现列表
        String WITHDRAW_DETAIL = "/drawModule/detail";// 提现详情
    }

    /**
     * QQ群玩法 相关
     */
    public interface BetApiQQ {
        String ORDER_BET_QQ = "/order/bet";// 投注
        String ORDER_NOLOTTERYBETS_QQ = "/order/noLotteryBets";// 未开奖数据
        String ORDER_HADLOTTERYBETS_QQ = "/order/hadLotteryBets";// 已开奖数据
        String GAME_BETPATTERNS_QQ = "/game/betPatterns";// 获取下注模式
    }
}
