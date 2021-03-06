package com.qp.app_new.contents;

import com.qp.app_new.AppPrefs;

import org.json.JSONObject;

/**
 * Created by Aaron on 17/11/8.
 */

public class AppPrefsContent {

    /**
     * 是否已经登录
     *
     * @return
     */
    public static boolean isLogined () {
        return AppPrefs.getInstance ().getUserJson () == null ? false : true;
    }

    public static JSONObject getUser () {
        JSONObject userJson = AppPrefs.getInstance ().getUserJson ();
        if (userJson == null) return null;
        try {
            userJson.put ("token", AppPrefs.getInstance ().getToken ());
        } catch (Exception e) {
            e.printStackTrace ();
        }
        return userJson;
    }

    /**
     * 处理登录数据
     *
     * @param jsonObject
     */
    public static void handlerLoginData (JSONObject jsonObject) {
        AppPrefs.getInstance ().saveUserJson (jsonObject.toString ());
        AppPrefs.getInstance ().saveUserPhone (jsonObject.optString ("phone"));
        AppPrefs.getInstance ().saveTokenState (true);// 能够访问
    }
}
