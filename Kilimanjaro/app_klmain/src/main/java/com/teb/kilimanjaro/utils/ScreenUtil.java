package com.teb.kilimanjaro.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

import com.teb.kilimanjaro.App;

/**
 * Created by yangbin on 16/7/1.
 * 屏幕适配相关工具类
 */
public class ScreenUtil {

    /**
     * 根据手机分辨率转换字体大小
     */
    public static int getFontSize(float textSize) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) App.getAppContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        int screenHeight = dm.heightPixels;
        int rate = (int) (textSize * (float) screenHeight / 1280);
        return rate;
    }

    /**
     * 获取屏幕宽度
     */
    public static int getScreenW() {
        DisplayMetrics dm = new DisplayMetrics();
        dm = App.getAppContext().getResources().getDisplayMetrics();
        int w = dm.widthPixels;
        return w;
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenH() {
        DisplayMetrics dm = new DisplayMetrics();
        dm = App.getAppContext().getResources().getDisplayMetrics();
        int h = dm.heightPixels;
        return h;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
//    public static int dip2px (Context context, float dpValue) {
//        final float scale = context.getResources ().getDisplayMetrics ().density;
//        return (int) (dpValue * scale + 0.5f);
//    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpValue, r.getDisplayMetrics());
        return (int) px;
    }
}
