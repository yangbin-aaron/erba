package com.teb.kilimanjaro.utils;

import android.app.Activity;

import com.teb.kilimanjaro.R;

/**
 * Created by yangbin on 16/7/5.
 * 动画工具类
 */
public class AnimUtil {

    /**
     * Activity左进右出动画(返回)
     *
     * @param activity
     */
    public static void actFromLeftToRight(Activity activity) {
        activity.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }

    /**
     * Activity右进左出动画
     *
     * @param activity
     */
    public static void actFromRightToLeft(Activity activity) {
        activity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    /**
     * 淡入浅出的效果 
     * @param activity
     */
    public static void actFade(Activity activity){
        activity.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }
}
