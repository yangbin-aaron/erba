package com.qp.app_new.contents;

import com.qp.app_new.AppPrefs;

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
}
