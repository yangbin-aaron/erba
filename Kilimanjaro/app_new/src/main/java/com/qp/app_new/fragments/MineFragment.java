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
import com.qp.app_new.utils.ToastUtil;
import com.qp.app_new.views.CircleImageView;

import org.json.JSONObject;

/**
 * Created by Aaron on 2017/11/8.
 */

public class MineFragment extends BaseFragment implements View.OnClickListener {
    @Override
    public void setContentView () {
        layoutId = R.layout.fragment_mine;
    }

    // 退出登录、交易明细、修改密码、关于我们
    private View mLogoutView, mTrandListView, mModifyPwdView, mAboutUsView;
    private CircleImageView mHeadCIV;
    private TextView mNickNameTV, mCoinTV;

    private Dialog mDialogLogout;

    @Override
    public void initView () {
        initActionBar ();
        setTitle (R.string.bottom_tab_mine);
        mHeadCIV = (CircleImageView) findViewById (R.id.iv_head);
        mNickNameTV = (TextView) findViewById (R.id.tv_nickName);
        mCoinTV = (TextView) findViewById (R.id.tv_coin);

        mTrandListView = findViewById (R.id.rl_trand_list);
        mTrandListView.setOnClickListener (this);
        mModifyPwdView = findViewById (R.id.rl_modify_pwd);
        mModifyPwdView.setOnClickListener (this);
        mAboutUsView = findViewById (R.id.rl_about);
        mAboutUsView.setOnClickListener (this);
        mLogoutView = findViewById (R.id.btn_logout);
        mLogoutView.setOnClickListener (this);
    }

    @Override
    public void onClick (View v) {
        switch (v.getId ()) {
            case R.id.btn_logout:
                if (mDialogLogout == null) {
                    mDialogLogout = DialogHelp.createOkDialog (getActivity (), getString (R.string.hint_logout_str), new NormalDialogListener1 () {
                        @Override
                        public void onOkClickListener () {
                            AppPrefs.getInstance ().saveUserJson (null);
                            ((MainActivity) getActivity ()).updateData ();
                        }
                    });
                }
                mDialogLogout.show ();
                break;
            case R.id.tv_nickName:
                ActivityStartUtils.startLoginActivity (getActivity ());
                break;
            case R.id.rl_modify_pwd:
                ToastUtil.showToast ("修改密码");
                break;
            case R.id.rl_about:
                ActivityStartUtils.startAboutUsActivity (getActivity ());
                break;
            case R.id.rl_trand_list:
                if (judgeLogin ()) {
                    ToastUtil.showToast ("交易明细");
                }
                break;
        }
    }

    @Override
    public void onResume () {
        super.onResume ();
        updateData ();
    }

    public void updateData () {
        if (AppPrefsContent.isLogined ()) {// 登录
            mLogoutView.setVisibility (View.VISIBLE);
            mModifyPwdView.setVisibility (View.VISIBLE);
            mCoinTV.setVisibility (View.VISIBLE);

            JSONObject userJsonObject = AppPrefsContent.getUser ();
            mCoinTV.setText (getString (R.string.gamecoin_str, userJsonObject.optString ("coin", "0")));
            String uri = userJsonObject.optString ("avatar");
            if (TextUtils.isEmpty (uri)) {
                mHeadCIV.setImageResource (R.mipmap.ic_launcher);
            } else {
                ImageAware imageAware = new ImageViewAware (mHeadCIV, false);
                mHeadCIV.setTag (uri);
                ImageLoader.getInstance ().displayImage (uri, imageAware);
            }
            mNickNameTV.setText (AppPrefsContent.getUser ().optString ("nickName"));
            mNickNameTV.setEnabled (false);
        } else {// 未登录
            mLogoutView.setVisibility (View.GONE);
            mModifyPwdView.setVisibility (View.GONE);
            mCoinTV.setVisibility (View.GONE);
            mHeadCIV.setImageResource (R.mipmap.ic_launcher);
            mNickNameTV.setText (R.string.login_register);
            mNickNameTV.setEnabled (true);
            mNickNameTV.setOnClickListener (this);
        }
    }
}
