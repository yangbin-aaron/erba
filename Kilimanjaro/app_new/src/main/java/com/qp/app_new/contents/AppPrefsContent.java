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

    /**
     * 处理登录数据
     *
     * @param jsonObject
     */
    public static void handlerLoginData (JSONObject jsonObject) {

    }
}
