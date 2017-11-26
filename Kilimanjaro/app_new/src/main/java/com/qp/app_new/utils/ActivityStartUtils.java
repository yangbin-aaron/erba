package com.qp.app_new.utils;

import android.app.Activity;
import android.content.Intent;

import com.qp.app_new.activitys.MainActivity;
import com.qp.app_new.activitys.home.ActualOddsActivity;
import com.qp.app_new.activitys.home.BetActivity;
import com.qp.app_new.activitys.home.BetDandianActivity;
import com.qp.app_new.activitys.home.BetedListActivity;
import com.qp.app_new.activitys.home.GameActivity;
import com.qp.app_new.activitys.home.LotteryDetailActivity;
import com.qp.app_new.activitys.home.PayingMethodActivity;
import com.qp.app_new.activitys.home.PayingMethodActivity1;
import com.qp.app_new.activitys.home.RevenueActivity;
import com.qp.app_new.activitys.mine.AboutUsActivity;
import com.qp.app_new.activitys.mine.ForgetPwdActivity;
import com.qp.app_new.activitys.mine.LoginActivity;
import com.qp.app_new.activitys.mine.ModifyPwdActivity;
import com.qp.app_new.activitys.mine.RegisterActivity;
import com.qp.app_new.activitys.mine.TrandDetailActivity;

import org.json.JSONObject;

/**
 * Created by Aaron on 17/11/8.
 */

public class ActivityStartUtils {

    public static void startMainActivity(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 登录
     *
     * @param activity
     * @param isExit   能否关闭登陆界面
     */
    public static void startLoginActivity(Activity activity, boolean isExit) {
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.putExtra("is_exit", isExit);
        activity.startActivity(intent);
    }

    public static void startLoginActivity(Activity activity) {
        startLoginActivity(activity, false);
    }

    public static void startRegisterActivity(Activity activity) {
        Intent intent = new Intent(activity, RegisterActivity.class);
        activity.startActivity(intent);
    }

    public static void startModifyPwdActivity(Activity activity) {
        Intent intent = new Intent(activity, ModifyPwdActivity.class);
        activity.startActivity(intent);
    }

    public static void startForgetPwdActivity(Activity activity) {
        Intent intent = new Intent(activity, ForgetPwdActivity.class);
        activity.startActivity(intent);
    }

    public static void startAboutUsActivity(Activity activity) {
        Intent intent = new Intent(activity, AboutUsActivity.class);
        activity.startActivity(intent);
    }

    public static void startGameActivity(Activity activity, JSONObject gameObject) {
        Intent intent = new Intent(activity, GameActivity.class);
        intent.putExtra("gameJson", gameObject.toString());
        activity.startActivity(intent);
    }

    public static void startLotteryDetailActivity(Activity activity, JSONObject gameObject, JSONObject lotteryObject) {
        Intent intent = new Intent(activity, LotteryDetailActivity.class);
        intent.putExtra("gameJson", gameObject.toString());
        intent.putExtra("lotteryJson", lotteryObject.toString());
        activity.startActivity(intent);
    }

    public static void startBetActivity(Activity activity, JSONObject gameJsonObject, JSONObject lotteryJsonObject) {
        Intent intent = new Intent(activity, BetActivity.class);
        intent.putExtra("gameJsonObject", gameJsonObject.toString());
        intent.putExtra("lotteryJsonObject", lotteryJsonObject.toString());
        activity.startActivity(intent);
    }

    public static void startBetDandianActivity(Activity activity, JSONObject gameJsonObject, JSONObject lotteryJsonObject, int ddId) {
        Intent intent = new Intent(activity, BetDandianActivity.class);
        intent.putExtra("gameJsonObject", gameJsonObject.toString());
        intent.putExtra("lotteryJsonObject", lotteryJsonObject.toString());
        intent.putExtra("ddId", ddId);
        activity.finish();
        activity.startActivity(intent);
    }

    public static void startBetedListActivity(Activity activity, JSONObject gameJsonObject, JSONObject lotteryJsonObject) {
        Intent intent = new Intent(activity, BetedListActivity.class);
        intent.putExtra("gameJsonObject", gameJsonObject.toString());
        intent.putExtra("lotteryJsonObject", lotteryJsonObject.toString());
        activity.startActivity(intent);
    }

    public static void startPayingMethodActivity(Activity activity, JSONObject gameJsonObject) {
        Intent intent = new Intent(activity, PayingMethodActivity.class);
        intent.putExtra("gameJsonObject", gameJsonObject.toString());
        activity.startActivity(intent);
    }

    public static void startPayingMethodActivity1(Activity activity, JSONObject gameJsonObject) {
        Intent intent = new Intent(activity, PayingMethodActivity1.class);
        intent.putExtra("gameJsonObject", gameJsonObject.toString());
        activity.startActivity(intent);
    }

    public static void startActualOddsActivity(Activity activity, JSONObject gameJsonObject) {
        Intent intent = new Intent(activity, ActualOddsActivity.class);
        intent.putExtra("gameJsonObject", gameJsonObject.toString());
        activity.startActivity(intent);
    }

    public static void startRevenueActivity(Activity activity, JSONObject gameJsonObject) {
        Intent intent = new Intent(activity, RevenueActivity.class);
        intent.putExtra("gameJsonObject", gameJsonObject.toString());
        activity.startActivity(intent);
    }

    public static void startTrandDetailActivity(Activity activity) {
        Intent intent = new Intent(activity, TrandDetailActivity.class);
        activity.startActivity(intent);
    }
}
