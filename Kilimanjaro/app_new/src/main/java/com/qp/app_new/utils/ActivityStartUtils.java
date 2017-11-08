package com.qp.app_new.utils;

import android.app.Activity;
import android.content.Intent;

import com.qp.app_new.activitys.LoginActivity;

/**
 * Created by Aaron on 17/11/8.
 */

public class ActivityStartUtils {

    /**
     * 登录
     *
     * @param activity
     */
    public static void startLoginActivity (Activity activity) {
        Intent intent = new Intent (activity, LoginActivity.class);
        activity.startActivity (intent);
    }
}
