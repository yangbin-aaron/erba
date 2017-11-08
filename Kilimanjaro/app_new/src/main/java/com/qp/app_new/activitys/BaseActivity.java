package com.qp.app_new.activitys;

import android.app.Activity;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.qp.app_new.R;
import com.qp.app_new.dialogs.DialogHelp;

/**
 * Created by Aaron on 17/11/8.
 */

public abstract class BaseActivity extends Activity {
    public Dialog mLoadingDialog;// 加载对话框
    public int layoutId = R.layout.activity_main;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
//        requestWindowFeature (Window.FEATURE_NO_TITLE);// 没有ActionBar,在style里面已经设置
        getWindow ().addFlags (WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 屏幕保持常亮
//        getWindow ().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 强制为竖屏
        setContentView ();
        setContentView (layoutId);
        mLoadingDialog = DialogHelp.createLoadingDialog (this, true);
        initView ();
    }

    public abstract void setContentView ();

    public abstract void initView ();

    /**
     * ActionBar   BEGIN
     */

    private View mActionBar, mLeftView, mRightView;
    private TextView mTitleTV, mRightTV;
    private ImageView mBackIV, mRightIV;

    public void initActionBar () {
        mActionBar = findViewById (R.id.action_bar);
        mActionBar.setVisibility (View.VISIBLE);

        mTitleTV = (TextView) findViewById (R.id.title_tv);
        mRightTV = (TextView) findViewById (R.id.right_tv);
        mBackIV = (ImageView) findViewById (R.id.back_iv);
        mRightIV = (ImageView) findViewById (R.id.right_iv);

        mLeftView = findViewById (R.id.back_btn);
        mLeftView.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                onLeftClick ();
            }
        });
        mRightView = findViewById (R.id.right_btn);
        mRightView.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                onRightClick ();
            }
        });
    }

    public void onLeftClick () {
        Log.e ("BaseActivity", "onLeftClick");
    }

    public void onRightClick () {
        Log.e ("BaseActivity", "onRightClick");
    }

    public void setTitle (String title) {
        mTitleTV.setText (title);
    }

    public void setTitle (int textId) {
        mTitleTV.setText (textId);
    }

    public void setLeftIV (int resId) {
        mLeftView.setVisibility (View.VISIBLE);
        mBackIV.setVisibility (View.VISIBLE);
        mBackIV.setImageResource (resId);
    }

    public void setRightIV (int resId) {
        mRightView.setVisibility (View.VISIBLE);
        mRightIV.setVisibility (View.VISIBLE);
        mRightIV.setImageResource (resId);
    }

    public void setRightTV (int resId) {
        mRightView.setVisibility (View.VISIBLE);
        mRightTV.setVisibility (View.VISIBLE);
        mRightTV.setText (resId);
    }

    /**
     * ActionBar   END
     */
}
