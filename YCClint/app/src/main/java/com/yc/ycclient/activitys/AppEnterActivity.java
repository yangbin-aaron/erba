package com.yc.ycclient.activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.yc.ycclient.R;

/**
 * 入口Activity
 */
public class AppEnterActivity extends FragmentActivity {

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_app_enter);
    }
}
