package com.qp.app_new.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qp.app_new.R;
import com.qp.app_new.contents.AppPrefsContent;
import com.qp.app_new.dialogs.DialogHelp;

/**
 * Created by Aaron on 17/11/7.
 */

public abstract class BaseFragment extends Fragment {

    public Dialog mLoadingDialog;// 加载对话框
    private Dialog mDialogLogin;
    private View mView, mAddView;
    private LinearLayout mLy;

    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate (R.layout.fragment_base, null);
        mLy = (LinearLayout) mView.findViewById (R.id.ly);
        int layoutId = getContentView ();
        if (layoutId != 0) {
            mAddView = inflater.inflate (layoutId, null);
            mLy.addView (mAddView);
        }
        mLoadingDialog = DialogHelp.createLoadingDialog (getActivity (), true);
        initView ();
        return mView;
    }

    public View findViewById (int resId) {
        return mView.findViewById (resId);
    }

    public abstract int getContentView ();

    public abstract void initView ();

    /**
     * 判断是否登录
     */
    public boolean judgeLogin () {
        if (!AppPrefsContent.isLogined ()) {
            // 去登陆
            if (mDialogLogin == null) {
                mDialogLogin = DialogHelp.createToLoginDialog (getActivity ());
            }
            mDialogLogin.show ();
            return false;
        }
        return true;
    }

    /**
     * ActionBar   BEGIN
     */

    public View mActionBar, mLeftView, mRightView;
    public TextView mTitleTV, mRightTV;
    public ImageView mBackIV, mRightIV;

    public void initActionBar () {
        if (mView == null) return;
        mActionBar = mView.findViewById (R.id.action_bar);
        mActionBar.setVisibility (View.VISIBLE);

        mTitleTV = (TextView) mView.findViewById (R.id.title_tv);
        mRightTV = (TextView) mView.findViewById (R.id.right_tv);
        mBackIV = (ImageView) mView.findViewById (R.id.back_iv);
        mRightIV = (ImageView) mView.findViewById (R.id.right_iv);

        mLeftView = mView.findViewById (R.id.back_btn);
        mLeftView.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                onLeftClick (v);
            }
        });
        mRightView = mView.findViewById (R.id.right_btn);
        mRightView.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                onRightClick (v);
            }
        });
    }

    public void onLeftClick (View v) {
        Log.e ("BaseFragment", "onLeftClick");
    }

    public void onRightClick (View v) {
        Log.e ("BaseFragment", "onRightClick");
    }

    public void setTitle (String title) {
        mTitleTV.setVisibility (View.VISIBLE);
        mTitleTV.setText (title);
    }

    public void setTitle (int textId) {
        mTitleTV.setVisibility (View.VISIBLE);
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

    public void setActionBarBackgroundResource (int colorId) {
        mActionBar.setBackgroundResource (colorId);
    }

    /**
     * ActionBar   END
     */
}
