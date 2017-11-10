package com.qp.app_new.fragments;

import android.app.Dialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.qp.app_new.AppPrefs;
import com.qp.app_new.R;
import com.qp.app_new.activitys.MainActivity;
import com.qp.app_new.contents.AppPrefsContent;
import com.qp.app_new.dialogs.DialogHelp;
import com.qp.app_new.interfaces.NormalDialogListener1;
import com.qp.app_new.utils.ActivityStartUtils;
import com.qp.app_new.views.CircleImageView;

/**
 * Created by Aaron on 2017/11/8.
 */

public class MineFragment extends BaseFragment implements View.OnClickListener {
    @Override
    public void setContentView() {
        layoutId = R.layout.fragment_mine;
    }

    private View mLogoutView;
    private CircleImageView mHeadCIV;
    private TextView mNickNameTV;

    private Dialog mDialogLogout;

    @Override
    public void initView() {
        initActionBar();
        setTitle(R.string.bottom_tab_mine);
        mHeadCIV = (CircleImageView) findViewById(R.id.iv_head);
        mNickNameTV = (TextView) findViewById(R.id.tv_nickName);
        mLogoutView = findViewById(R.id.btn_logout);
        mLogoutView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_logout:
                if (mDialogLogout == null) {
                    mDialogLogout = DialogHelp.createOkDialog(getActivity(), getString(R.string.hint_logout_str), new NormalDialogListener1() {
                        @Override
                        public void onOkClickListener() {
                            AppPrefs.getInstance().saveUserJson(null);
                            ((MainActivity) getActivity()).logoutCallback();
                        }
                    });
                }
                mDialogLogout.show();
                break;
            case R.id.tv_nickName:
                ActivityStartUtils.startLoginActivity(getActivity());
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setUserInfo();
    }

    public void setUserInfo() {
        if (AppPrefsContent.isLogined()) {
            mLogoutView.setVisibility(View.VISIBLE);
            String uri = AppPrefsContent.getUser().optString("avatar");
            if (TextUtils.isEmpty(uri)) {
                mHeadCIV.setImageResource(R.mipmap.ic_launcher);
            } else {
                ImageAware imageAware = new ImageViewAware(mHeadCIV, false);
                mHeadCIV.setTag(uri);
                ImageLoader.getInstance().displayImage(uri, imageAware);
            }
            mNickNameTV.setText(AppPrefsContent.getUser().optString("nickName"));
            mNickNameTV.setEnabled(false);
        } else {
            mLogoutView.setVisibility(View.GONE);
            mHeadCIV.setImageResource(R.mipmap.ic_launcher);
            mNickNameTV.setText(R.string.login_register);
            mNickNameTV.setEnabled(true);
            mNickNameTV.setOnClickListener(this);
        }
    }
}
