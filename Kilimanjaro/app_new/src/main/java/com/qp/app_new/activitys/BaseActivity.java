package com.qp.app_new.activitys;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qp.app_new.R;
import com.qp.app_new.configs.BroadcastConfig;
import com.qp.app_new.contents.AppPrefsContent;
import com.qp.app_new.dialogs.DialogHelp;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Aaron on 17/11/8.
 */

public abstract class BaseActivity extends Activity {
    public Dialog mLoadingDialog;// 加载对话框
    private Dialog mDialogLogin;
    private View mView;
    private LinearLayout mLy;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            handlerBroadcastReceiver(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 屏幕保持常亮
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 强制为竖屏
        getIntentData();
        setContentView(R.layout.activity_base);
        mLy = (LinearLayout) findViewById(R.id.ly);
        LayoutInflater inflater = LayoutInflater.from(this);
        int layoutId = getContentView();
        if (layoutId != 0) {
            mView = inflater.inflate(getContentView(), null);
            mLy.addView(mView);
        }
        mLoadingDialog = DialogHelp.createLoadingDialog(this, true);
        initView();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastConfig.ACTION_EXIT_APP);
        addFilterAction(filter);
        registerReceiver(mReceiver, filter);
    }

    public void addFilterAction(IntentFilter filter) {

    }

    public void handlerBroadcastReceiver(Intent intent) {
        if (intent.getAction().equals(BroadcastConfig.ACTION_EXIT_APP)) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    public abstract int getContentView();

    public abstract void initView();

    public void getIntentData() {
    }

    /**
     * 判断是否登录
     */
    public boolean judgeLogin() {
        if (!AppPrefsContent.isLogined()) {
            // 去登陆
            if (mDialogLogin == null) {
                mDialogLogin = DialogHelp.createToLoginDialog(this);
            }
            mDialogLogin.show();
            return false;
        }
        return true;
    }

    /**
     * ActionBar   BEGIN
     */

    private View mActionBar, mLeftView, mRightView;
    private TextView mTitleTV, mRightTV;
    private ImageView mBackIV, mRightIV;

    public void initActionBar() {
        mActionBar = findViewById(R.id.action_bar);
        mActionBar.setVisibility(View.VISIBLE);

        mTitleTV = (TextView) findViewById(R.id.title_tv);
        mRightTV = (TextView) findViewById(R.id.right_tv);
        mBackIV = (ImageView) findViewById(R.id.back_iv);
        mRightIV = (ImageView) findViewById(R.id.right_iv);

        mLeftView = findViewById(R.id.back_btn);
        mLeftView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLeftClick(v);
            }
        });
        mRightView = findViewById(R.id.right_btn);
        mRightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRightClick(v);
            }
        });
    }

    public void onLeftClick(View v) {
        Log.e("BaseActivity", "onLeftClick");
        finish();
    }

    public void onRightClick(View v) {
        Log.e("BaseActivity", "onRightClick");
    }

    public void setTitle(String title) {
        if (mActionBar != null) mTitleTV.setText(title);
    }

    public void setTitle(int textId) {
        if (mActionBar != null) mTitleTV.setText(textId);
    }

    public void setLeftIV(int resId) {
        if (mActionBar != null) {
            mLeftView.setVisibility(View.VISIBLE);
            mBackIV.setVisibility(View.VISIBLE);
            mBackIV.setImageResource(resId);
        }
    }

    public void setRightIV(int resId) {
        if (mActionBar != null) {
            mRightView.setVisibility(View.VISIBLE);
            mRightIV.setVisibility(View.VISIBLE);
            mRightIV.setImageResource(resId);
        }
    }

    public void setRightTV(int resId) {
        if (mActionBar != null) {
            mRightView.setVisibility(View.VISIBLE);
            mRightTV.setVisibility(View.VISIBLE);
            mRightTV.setText(resId);
        }
    }

    public void setActionBarBackgroundResource(int colorId) {
        if (mActionBar != null) mActionBar.setBackgroundResource(colorId);
    }

    public void setBackgroundResource(int colorId) {
        mLy.setBackgroundResource(colorId);
    }

    /**
     * ActionBar   END
     */


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
