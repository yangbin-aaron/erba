package com.qp.app_new.utils;

import android.app.Activity;
import android.content.Intent;

import com.qp.app_new.activitys.AboutUsActivity;
import com.qp.app_new.activitys.GameActivity;
import com.qp.app_new.activitys.LoginActivity;
import com.qp.app_new.activitys.LotteryDetailActivity;
import com.qp.app_new.activitys.MainActivity;

import org.json.JSONObject;

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
     * @param isExit 能否关闭登陆界面
     */
    public static void startLoginActivity (Activity activity, boolean isExit) {
        Intent intent = new Intent (activity, LoginActivity.class);
        intent.putExtra ("is_exit", isExit);
        activity.startActivity (intent);
    }

    public static void startLoginActivity (Activity activity) {
        Intent intent = new Intent (activity, LoginActivity.class);
        intent.putExtra ("is_exit", false);
        activity.startActivity (intent);
    }

    public static void startAboutUsActivity (Activity activity) {
        Intent intent = new Intent (activity, AboutUsActivity.class);
        activity.startActivity (intent);
    }

    public static void startGameActivity (Activity activity, JSONObject gameObject) {
        Intent intent = new Intent (activity, GameActivity.class);
        intent.putExtra ("gameJson", gameObject.toString ());
        activity.startActivity (intent);
    }

    public static void startLotteryDetailActivity (Activity activity, JSONObject gameObject, JSONObject lotteryObject) {
        Intent intent = new Intent (activity, LotteryDetailActivity.class);
        intent.putExtra ("gameJson", gameObject.toString ());
        intent.putExtra ("lotteryJson", lotteryObject.toString ());
        activity.startActivity (intent);
    }
}
