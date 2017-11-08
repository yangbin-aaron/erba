package com.qp.app_new.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;

import com.qp.app_new.R;
import com.qp.app_new.contents.AppPrefsContent;
import com.qp.app_new.httpnetworks.NetWorkManager;
import com.qp.app_new.interfaces.NetListener;
import com.qp.app_new.utils.ActivityStartUtils;
import com.qp.app_new.utils.StringUtil;

import org.json.JSONObject;

/**
 * Created by yangbin on 16/7/1.
 * 欢迎界面（闪屏界面）
 */
public class SplashActivity extends Activity {

    private final Long SPLASH_DELAY = 2 * 1000L;// 欢迎界面停留时间

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        // 全屏
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView (R.layout.activity_splash);
        NetWorkManager.getInstance ().getBasicInfo (StringUtil.getNullJson ());
        skipActivity ();
    }

    private void skipActivity () {
        new Handler ().postDelayed (new Runnable () {

            @Override
            public void run () {
                // 是否登录
                if (!AppPrefsContent.isLogined ()) {// 未登录跳到登录界面
                    // 调登录接口，先登录
                    NetWorkManager.getInstance ().login ("", null, new NetListener () {
                        @Override
                        public void onSuccessResponse (String msg, JSONObject jsonObject) {
                            super.onSuccessResponse (msg, jsonObject);
                            // 处理登录数据
                            AppPrefsContent.handlerLoginData (jsonObject);
                            ActivityStartUtils.startMainActivity (SplashActivity.this);
                        }

                        @Override
                        public void onErrorResponse (int errorWhat, String message) {
                            // 登录失败
                            ActivityStartUtils.startMainActivity (SplashActivity.this);
                        }
                    });
                } else {// 已经登录跳到主界面
                    ActivityStartUtils.startMainActivity (SplashActivity.this);
                }
                finish ();
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
    public boolean onKeyDown (int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown (keyCode, event);
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
