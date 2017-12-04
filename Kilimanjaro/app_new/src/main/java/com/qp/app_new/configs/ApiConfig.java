package com.qp.app_new.configs;

import com.qp.app_new.App;

/**
 * Created by yangbin on 16/7/4.
 * API管理类
 */
public class ApiConfig {


    private static final String SCHEME_HOST_DEV = "http://api28dev.chenfu3991.com";// dev域名
    private static final String SCHEME_HOST_PRO = "http://api28pro.chenfu3991.com";// pro

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
        String REGISTER_SEND_SMS = "/user/captcha/registerSend";// 发送注册验证码
        String GET_CAPTCHA = "/user/captcha/send";// 发送验证码
        String VERITY_CAPTCHA = "/user/captcha/auth";// 验证验证码
        String REGISTER_COMLPAE = "/user/register";// 完成注册
        String LOGIN_PWD_MODIFY = "/user/loginPasswordModify";// 修改登录密码
        String LOGIN_PWD_RESET = "/user/resetLoginPassword";// 重置登录密码
        String GAME_LIST = "/game/list";// 游戏列表（我的界面）
        String UPDATE_APP = "/user/auth/checkVersion";// 获取版本信息
        String BASIC_INFO = "/sysProperty/list";// 客服信息
    }

    /**
     * 大厅  相关
     */
    public interface HallApi {
        String GET_HISTORY_LOTTERY_LIST = "/lottery/hadLotteryList";// 游戏历史数据模块 - 历史数据列表
        String GET_NO_LOTTERY_LIST = "/lottery/noLotteryList";// 游戏历史数据模块 - 未开奖数据列表
        String REVENUE_PERIODS = "/revenue/periods";// 按期亏盈
        String REVENUE_DAYS = "/revenue/days";// 按日亏盈
        String REVENUE_GENERAL = "/revenue/general";// 亏盈概览
    }

    /**
     * 排行榜  相关
     */
    public interface RankingApi {
        String GET_RANKING = "/game/ranking";
    }

    public interface CoinApi {
        String GET_COIN = "/user/coin";// 获取积分
        String GET_ORDERS_LIST = "/user/orders";// 获取下注列表
        String GET_RECHARGES_LIST = "/user/reCharges";// 获取上下分列表
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
