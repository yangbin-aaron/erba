package com.teb.kilimanjaro.activitys;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.WindowManager;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by yangbin on 16/7/1.
 * Activity基类
 */
public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature (Window.FEATURE_NO_TITLE);// 没有ActionBar,在style里面已经设置
        getWindow ().addFlags (WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 屏幕保持常亮
//        getWindow ().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 强制为竖屏
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("BaseActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("BaseActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
