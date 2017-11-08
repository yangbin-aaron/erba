package com.qp.app_new;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Aaron on 17/11/8.
 */

public class AppPrefs {
    private static final String SHARED_PREFERENCES_NAME = "p_prefs";

    private interface Key {
        String USER_PHONE = "user_phone";// 登录账户
        String USER_JSON = "user_json";// 登录完成保持json数据
        String GAME_LIST_JSON = "game_list_json";// 游戏列表的json数据
        String GAME_SELECTED_ID = "game_selected_id";// 被选中的游戏ID
        String BET_MODE = "bet_mode";// 习惯的投注模式（极速还是普通）
        String TOKEN_WRONG = "token_wrong";// 是否自动下线
        String GAME_COIN = "game_coin";// 游戏金豆
        String GAME_SCORE = "game_score";// 游戏积分
        String LAST_BET = "last_bet";// 上次投注的记录
        String LAST_BET_QQ = "last_bet_qq";// 上次投注的记录
        String CURR_BET_INFO = "curr_bet_info";// 当前投注信息
        String NEW_BET_INFO = "new_bet_info";// 最新的开奖信息
        String START_ISSUE = "start_issue";// 开始期号
        String MODE_LIST = "mode_list";// 自动投注的模式列表
        String AFTER_WIN_CONTINUE = "after_win_continue";// 自动投注  赢了后继续
        String MODE_START = "mode_start";// 自动投注的模式列表
        //        String MODE_END = "mode_end";// 自动投注的模式列表
        String BET_AUTO_MAX_COIN = "bet_auto_max_coin";// 余额达到金额
        String BET_AUTO_MIN_COIN = "bet_auto_min_coin";// 余额不足金额
        String BET_AUTO_COUNT = "bet_auto_count";// 自动投注的期数
        String BET_AUTO_IS_STARTED = "bet_auto_is_started";// 自动投注是否开始
        String BASIC_INFO = "basic_info";// 客服信息
    }

    private static AppPrefs sInstance;

    private SharedPreferences mPrefs;

    public static AppPrefs getInstance () {
        if (sInstance == null) {
            sInstance = new AppPrefs ();
        }
        return sInstance;
    }

    private AppPrefs () {
        mPrefs = App.mContext.getSharedPreferences (SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    private SharedPreferences.Editor getEditor () {
        return mPrefs.edit ();
    }

    // *******************************************
    public void saveUserPhone (String phone) {
        getEditor ().putString (Key.USER_PHONE, phone).commit ();
    }

    public String getUserPhone () {
        return mPrefs.getString (Key.USER_PHONE, null);
    }

    /**
     * /**
     * 保存登录返回的json数据
     *
     * @param userJson
     */
    public void saveUserJson (String userJson) {
        getEditor ().putString (Key.USER_JSON, userJson).commit ();
    }

    /**
     * 获取登录json
     *
     * @return
     */
    public String getUserJson () {
        return mPrefs.getString (Key.USER_JSON, null);
    }
}
