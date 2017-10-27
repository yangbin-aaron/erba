package com.yc.ycclient.activitys;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.yc.ycclient.R;
import com.yc.ycclient.views.MyActionBar;

/**
 * Created by Aaron on 17/10/25.
 * Activity基类
 */

public abstract class AppBaseActivity extends Activity implements MyActionBar.OnActionBarClickListener {

    private LinearLayout ly;
    private MyActionBar mMyActionBar;
    private View mView;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_app_base);
        initBaseView ();
        onCreateYB ();
        onInitActionBar ();
    }

    private void initBaseView () {
        ly = (LinearLayout) findViewById (R.id.ly);
        mMyActionBar = (MyActionBar) findViewById (R.id.my_action_bar);
        mMyActionBar.setBarTitleText ("AppBaseActivity");
        mMyActionBar.setBarLeftImage (R.drawable.ic_back_btn);
        mMyActionBar.setOnActionBarClickListener (this);
        mMyActionBar.setBarBackGround (R.color.red);
    }

    public abstract void onCreateYB ();

    public abstract void onInitActionBar ();

    public void setContentViewYB (int layoutResID) {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        mView = layoutInflater.inflate (layoutResID, null);
        ly.addView (mView);
    }

    public void hideActionBar () {
        mMyActionBar.setVisibility (View.GONE);
    }

    public View getViewById (int id) {
        return mView.findViewById (id);
    }

    public void setActionBarTitle (String title) {
        mMyActionBar.setBarTitleText (title);
    }

    public void setActionBarTitle (int resId) {
        mMyActionBar.setBarTitleText (resId);
    }

    @Override
    public void onActionBarLeftClicked () {
        finish ();
    }

    @Override
    public void onActionBarRightClicked () {

    }
}
