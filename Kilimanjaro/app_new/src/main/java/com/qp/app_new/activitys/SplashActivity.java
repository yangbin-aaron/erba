package com.qp.app_new.activitys;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;

import com.qp.app_new.R;
import com.qp.app_new.httpnetworks.NetWorkManager;
import com.qp.app_new.utils.ActivityStartUtils;

/**
 * Created by yangbin on 16/7/1.
 * 欢迎界面（闪屏界面）
 */
public class SplashActivity extends Activity {

    private final Long SPLASH_DELAY = 2 * 1000L;// 欢迎界面停留时间

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 强制为竖屏
        // 全屏
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_splash);
        NetWorkManager.getInstance().getSysInfo();
        skipActivity();
    }

    private void skipActivity() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                ActivityStartUtils.startMainActivity(SplashActivity.this);
                finish();
            }
        }, SPLASH_DELAY);
    }

    /**
     * 欢迎界面不允许退出
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

//    @Override
//    protected void onResume () {
//        super.onResume ();
//        MobclickAgent.onPageStart ("SplashActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
//        MobclickAgent.onResume (this);          //统计时长
//    }
//
//    @Override
//    protected void onPause () {
//        super.onPause ();
//        MobclickAgent.onPageEnd ("SplashActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause
//        // 中会保存信息。"SplashScreen"为页面名称，可自定义
//        MobclickAgent.onPause (this);
//    }
}
