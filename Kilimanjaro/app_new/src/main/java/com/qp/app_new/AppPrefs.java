package com.qp.app_new;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.qp.app_new.utils.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aaron on 17/11/8.
 */

public class AppPrefs {
    private static final String SHARED_PREFERENCES_NAME = "p_prefs";

    private interface Key {
        String USER_PHONE = "user_phone";// 登录账户
        String USER_JSON = "user_json";// 登录完成保持json数据
        String TOKEN = "token";// token
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
        String SYS_INFO_QQ = "customerQQ";// 系统信息  qq
        String SYS_INFO_PHONE = "customerPhone";// 系统信息
        String SYS_INFO_WX = "customerWeixin";// 系统信息
        String SYS_INFO_WORKTIME = "workTime";// 系统信息  工作时间
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
    public JSONObject getUserJson () {
        String info = mPrefs.getString (Key.USER_JSON, null);
        JSONObject jsonObject = null;
        if (!TextUtils.isEmpty (info)) {
            try {
                jsonObject = new JSONObject (info);
            } catch (JSONException e) {
                e.printStackTrace ();
            }
        }
        return jsonObject;
    }

    public void saveToken (String token) {
        getEditor ().putString (Key.TOKEN, token).commit ();
    }

    public String getToken () {
        return mPrefs.getString (Key.TOKEN, "");
    }

    /**
     * 修改 自动下线的 状态
     *
     * @param tokenState
     */
    public void saveTokenState (boolean tokenState) {
        getEditor ().putBoolean (Key.TOKEN_WRONG, tokenState).commit ();
    }

    /**
     * 获取  自动下线的 状态
     *
     * @return
     */
    public boolean getTokenState () {
        return mPrefs.getBoolean (Key.TOKEN_WRONG, false);
    }

    public void saveSysInfo (JSONArray sysInfo) {
        for (int i = 0; i < sysInfo.length (); i++) {
            JSONObject jsonObject = sysInfo.optJSONObject (i);
            getEditor ().putString (jsonObject.optString ("key"), jsonObject.optString ("value")).commit ();
        }
    }

    public String getSysInfoQQ () {
        return mPrefs.getString (Key.SYS_INFO_QQ, App.mContext.getString (R.string.kefu_qq_num_default));
    }

    public String getSysInfoPhone () {
        return mPrefs.getString (Key.SYS_INFO_PHONE, App.mContext.getString (R.string.kefu_phone_default));
    }

    public String getSysInfoWX () {
        return mPrefs.getString (Key.SYS_INFO_WX, App.mContext.getString (R.string.kefu_wx_default));
    }

    public String getSysInfoWorkTime () {
        return mPrefs.getString (Key.SYS_INFO_WORKTIME, App.mContext.getString (R.string.kefu_worktime_default));
    }

    /**
     * 保存本次投注的信息
     */
    public void saveLastBetMode (JSONObject modeJsonObject) {
        LogUtil.e ("save = " + Key.LAST_BET_QQ + App.currentGameJsonObject.optString ("id") + "    " + modeJsonObject.toString ());
        getEditor ().putString (Key.LAST_BET_QQ + App.currentGameJsonObject.optString ("id"), modeJsonObject.toString ()).commit ();
    }

    /**
     * 获取  QQ群玩法的上次投注
     *
     * @return
     */
    public JSONObject getLastBetMode () {
        String json = mPrefs.getString (Key.LAST_BET_QQ + App.currentGameJsonObject.optString ("id"), null);
        LogUtil.e ("get = " + Key.LAST_BET_QQ + App.currentGameJsonObject.optString ("id") + "    " + json);
        JSONObject jsonObject = null;
        if (!TextUtils.isEmpty (json)) {
            try {
                jsonObject = new JSONObject (json);
            } catch (JSONException e) {
                e.printStackTrace ();
            }
        }
        return jsonObject;
    }

    /**
     * 积分
     *
     * @param coin
     */
    public void saveGameCoin (long coin) {
        getEditor ().putLong (Key.GAME_COIN, coin).commit ();
    }

    /**
     * 积分
     *
     * @return
     */
    public long getGameCoin () {
        return mPrefs.getLong (Key.GAME_COIN, 0);
    }
}
