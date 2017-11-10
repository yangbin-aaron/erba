package com.qp.app_new.utils;

import android.app.Activity;
import android.content.Intent;

import com.qp.app_new.activitys.AboutUsActivity;
import com.qp.app_new.activitys.GameActivity;
import com.qp.app_new.activitys.LoginActivity;
import com.qp.app_new.activitys.MainActivity;

/**
 * Created by Aaron on 17/11/8.
 */

public class ActivityStartUtils {

    public static void startMainActivity (Activity activity) {
        Intent intent = new Intent (activity, MainActivity.class);
        activity.startActivity (intent);
    }

    /**
     * 登录
     *
     * @param activity
     */
    public static void startLoginActivity (Activity activity) {
        Intent intent = new Intent (activity, LoginActivity.class);
        activity.startActivity (intent);
    }

    public static void startAboutUsActivity (Activity activity) {
        Intent intent = new Intent (activity, AboutUsActivity.class);
        activity.startActivity (intent);
    }

    public static void startGameActivity (Activity activity, String gameJson) {
        Intent intent = new Intent (activity, GameActivity.class);
        intent.putExtra ("gameJson", gameJson);
        activity.startActivity (intent);
    }
}
