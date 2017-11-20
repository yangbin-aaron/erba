package com.qp.app_new.activitys.mine;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qp.app_new.AppPrefs;
import com.qp.app_new.R;
import com.qp.app_new.activitys.BaseActivity;
import com.qp.app_new.configs.BroadcastConfig;
import com.qp.app_new.contents.AppPrefsContent;
import com.qp.app_new.dialogs.DialogHelp;
import com.qp.app_new.httpnetworks.NetWorkManager;
import com.qp.app_new.interfaces.NetListener;
import com.qp.app_new.interfaces.NormalDialogListener1;
import com.qp.app_new.utils.ActivityStartUtils;
import com.qp.app_new.utils.NetUtil;
import com.qp.app_new.utils.StringUtil;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Aaron on 17/11/19.
 */

public class ForgetPwdActivity extends BaseActivity implements View.OnClickListener {
    @Override
    public int getContentView () {
        return R.layout.activity_forget_loginpwd;
    }

    private int mCurrentStep = 0;// 当前步骤

    private LinearLayout mProLayout;
    private View[] mProLinears = new View[2];// 进度线
    private TextView[] mProTVs = new TextView[2];// 进度数字
    private LinearLayout[] mLayouts = new LinearLayout[3];// 布局

    private EditText mPhoneET, mCaptchaET;// 电话输入，验证码输入

    private LinearLayout mInputCaptchaLL;
    private View mGetCaptchaBtn;// 获取验证码，下一步，致电客服

    private RelativeLayout mShowExpiredRL;
    private TextView mShowPhoneTV, mShowExpiredTV;// 显示手机号码，显示倒计时

    private EditText mNewPwdET, mReNewPwdET;// 新密码，确认新密码

    private int mExpired = 0;
    private Handler mHandler = new Handler ();// 定时器(用作验证码倒计时)
    private Runnable mRunnable = new Runnable () {
        @Override
        public void run () {
            mShowPhoneTV.setText (mPhone);
            mInputCaptchaLL.setVisibility (View.VISIBLE);
            // 刷新数字
            if (mExpired != 0) {
                mShowExpiredRL.setVisibility (View.VISIBLE);
                mGetCaptchaBtn.setVisibility (View.GONE);
                mShowExpiredTV.setText (R.string.re_send_sms);
                mShowExpiredTV.append ("(" + mExpired + ")");
                mExpired--;
                mHandler.postDelayed (mRunnable, 1000);
            } else {
                mShowExpiredRL.setVisibility (View.GONE);
                mGetCaptchaBtn.setVisibility (View.VISIBLE);
            }
        }
    };

    private String mPhone;// 输入的电话
    private String mToken;// 验证验证码时传回来的token
    private String mPassword;// 密码

    @Override
    public void initView () {
        initActionBar ();
        setBackgroundResource (R.color.translucent);
        setActionBarBackgroundResource (R.color.translucent);
        setLeftIV (R.drawable.ic_back_btn);
        setTitle (R.string.forget_title);

        // ******三个步骤公共view
        mProLayout = (LinearLayout) findViewById (R.id.ll_pro);
        mLayouts[0] = (LinearLayout) findViewById (R.id.ll_1);
        mLayouts[1] = (LinearLayout) findViewById (R.id.ll_2);
        mLayouts[2] = (LinearLayout) findViewById (R.id.ll_3);
        mProLinears[0] = findViewById (R.id.view_1);
        mProLinears[1] = findViewById (R.id.view_2);
        mProTVs[0] = (TextView) findViewById (R.id.tv_1);
        mProTVs[1] = (TextView) findViewById (R.id.tv_2);

        // ******获取验证码部分
        mPhoneET = (EditText) findViewById (R.id.et_phone);
        mGetCaptchaBtn = findViewById (R.id.btn_get_captcha);
        mGetCaptchaBtn.setOnClickListener (this);
        mShowExpiredRL = (RelativeLayout) findViewById (R.id.rl_show_captcha);
        mShowPhoneTV = (TextView) findViewById (R.id.tv_phone);
        mShowExpiredTV = (TextView) findViewById (R.id.tv_expired);
        findViewById (R.id.tv_call_phone).setOnClickListener (this);

        // ******下一步
        mInputCaptchaLL = (LinearLayout) findViewById (R.id.ll_input_captcha);
        mCaptchaET = (EditText) findViewById (R.id.et_captcha);
        findViewById (R.id.btn_next).setOnClickListener (this);

        // ******输入新密码
        mNewPwdET = (EditText) findViewById (R.id.et_new_pwd);
        mReNewPwdET = (EditText) findViewById (R.id.et_re_new_pwd);
        findViewById (R.id.btn_submit).setOnClickListener (this);

        // ******完成设置
        findViewById (R.id.btn_goto_login).setOnClickListener (this);
    }

    @Override
    public void onClick (View v) {
        switch (v.getId ()) {
            case R.id.btn_get_captcha:// 获取验证码
                step1 ();
                break;
            case R.id.tv_call_phone:// 致电客服
                DialogHelp.showKefuQQDialog (this);
                break;
            case R.id.btn_next:// 下一步
                step2 ();
                break;
            case R.id.btn_submit:// 提交密码
                step3 ();
                break;
            case R.id.btn_goto_login:
                finish ();
                break;
        }
    }

    private void step1 () {
        mPhone = mPhoneET.getText ().toString ().trim ();
        if (TextUtils.isEmpty (mPhone)) {
            DialogHelp.showMessageDialog (this, getString (R.string.phone_cannot_null));
            return;
        }
        if (!StringUtil.isPhone (mPhone)) {
            DialogHelp.showMessageDialog (this, getString (R.string.phone_is_error));
            return;
        }
        mLoadingDialog.show ();

        HashMap<String, Object> hashMapSms = new HashMap<> ();
        hashMapSms.put ("phone", mPhone);
        NetWorkManager.getInstance ().sendResetSMS (StringUtil.getJson (hashMapSms), mLoadingDialog, new NetListener () {
            @Override
            public void onSuccessResponse (String msg, JSONObject jsonObject) {
                super.onSuccessResponse (msg, jsonObject);
                startTimer (jsonObject.optInt ("expired"));
            }
        });
    }

    private void step2 () {
        String captcha = mCaptchaET.getText ().toString ().trim ();
        if (TextUtils.isEmpty (captcha)) {
            DialogHelp.showMessageDialog (this, getString (R.string.virify_cannot_null));
            return;
        }
        mLoadingDialog.show ();
        HashMap<String, Object> hashMapCapt = new HashMap<> ();
        hashMapCapt.put ("phone", mPhone);
        hashMapCapt.put ("captcha", captcha);
        NetWorkManager.getInstance ().verityCaptcha (StringUtil.getJson (hashMapCapt), mLoadingDialog, new NetListener () {
            @Override
            public void onSuccessResponse (JSONObject jsonObject) {
                super.onSuccessResponse (jsonObject);
                mToken = jsonObject.optString ("captchaToken");
                mCurrentStep = 1;
                changeLayout ();
            }
        });
    }

    private void step3 () {
        String pwd = mNewPwdET.getText ().toString ().trim ();
        String rePwd = mReNewPwdET.getText ().toString ().trim ();
        if (TextUtils.isEmpty (pwd) || TextUtils.isEmpty (rePwd)) {
            DialogHelp.showMessageDialog (this, getString (R.string.input_cannot_null));
            return;
        }

        if (pwd.length () < 6 || rePwd.length () < 6) {
            DialogHelp.showMessageDialog (this, getString (R.string.pwd_lenth));
            return;
        }

        if (!pwd.equals (rePwd)) {
            DialogHelp.showMessageDialog (this, getString (R.string.pwd_and_repwd_not_same));
            return;
        }

        mPassword = pwd;
        mLoadingDialog.show ();
        HashMap<String, Object> hashMapReset = new HashMap<> ();
        hashMapReset.put ("phone", mPhone);
        hashMapReset.put ("password", NetUtil.MD5 (mPassword));
        hashMapReset.put ("captchaToken", mToken);
        NetWorkManager.getInstance ().resetPwd (StringUtil.getJson (hashMapReset), mLoadingDialog, new NetListener () {
            @Override
            public void onSuccessResponse (JSONObject jsonObject) {
                super.onSuccessResponse (jsonObject);
                AppPrefs.getInstance ().saveUserJson (null);
                mCurrentStep = 2;
                changeLayout ();
            }
        });
    }

    /**
     * 开启定时器
     *
     * @param expired
     */
    private void startTimer (int expired) {
        mExpired = expired;
        Log.e ("aaron", "expired = " + mExpired);
        mHandler.post (mRunnable);
    }

    @Override
    protected void onDestroy () {
        super.onDestroy ();
        if (mExpired > 0) {
            mHandler.removeCallbacks (mRunnable);
        }
    }

    /**
     * 根据进度刷新View
     */
    private void changeLayout () {
        if (mCurrentStep != 1 && mExpired > 0) {
            mHandler.removeCallbacks (mRunnable);
            mExpired = 0;
        }
        if (mCurrentStep == 2) {
            mProLayout.setVisibility (View.INVISIBLE);
            mLayouts[0].setVisibility (View.GONE);
            mLayouts[1].setVisibility (View.GONE);
            mLayouts[2].setVisibility (View.VISIBLE);
            return;
        }
        for (int i = 0; i < 2; i++) {
            if (i == mCurrentStep) {
                mProLinears[i].setBackgroundResource (R.color.main_background);
                mProTVs[i].setBackgroundResource (R.drawable.shape_circle_main_str0);
                mLayouts[i].setVisibility (View.VISIBLE);
            } else {
                mProLinears[i].setBackgroundResource (R.color.gray_xx);
                mProTVs[i].setBackgroundResource (R.drawable.shape_circle_grayxx_str0);
                mLayouts[i].setVisibility (View.GONE);
            }
        }
    }

    private Dialog mDialogBack;

    /**
     * 退出注册
     */
    private void toBack () {
        if (mDialogBack == null) {
            mDialogBack = DialogHelp.createOkDialog (this, getString (R.string.finish_register), new NormalDialogListener1 () {
                @Override
                public void onOkClickListener () {
                    finish ();
                }
            });
        }
        mDialogBack.show ();
    }

    @Override
    public void onLeftClick (View v) {
        toBack ();
    }

    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            toBack ();
            return true;
        }
        return super.onKeyDown (keyCode, event);
    }

    /**
     * Created by Aaron on 17/11/8.
     */

    public static class LoginActivity extends BaseActivity implements View.OnClickListener {
        @Override
        public int getContentView () {
            return R.layout.activity_login;
        }

        // 手机号码输入框、密码输入框
        private EditText mPhoneET, mPasswordET;
        private boolean mIsExit;// 异常登陆时   ，如果按返回，退到主页

        @Override
        public void getIntentData () {
            super.getIntentData ();
            mIsExit = getIntent ().getBooleanExtra ("is_exit", false);
        }

        @Override
        public void initView () {
            initActionBar ();
            setBackgroundResource (R.color.translucent);
            setActionBarBackgroundResource (R.color.translucent);
            setLeftIV (R.drawable.ic_back_btn);
            setTitle (R.string.login_btn);

            mPhoneET = (EditText) findViewById (R.id.et_phone);
            mPasswordET = (EditText) findViewById (R.id.et_password);
            if (AppPrefs.getInstance ().getUserPhone () != null) {
                mPhoneET.setText (AppPrefs.getInstance ().getUserPhone ());
                mPasswordET.setFocusable (true);
                mPasswordET.setFocusableInTouchMode (true);
                mPasswordET.requestFocus ();
            }

            findViewById (R.id.iv_clear_phone).setOnClickListener (this);
            findViewById (R.id.iv_clear_pwd).setOnClickListener (this);
            findViewById (R.id.btn_login).setOnClickListener (this);
            findViewById (R.id.tv_forgetpwd).setOnClickListener (this);
            findViewById (R.id.tv_register).setOnClickListener (this);
        }

        @Override
        public void onClick (View v) {
            switch (v.getId ()) {
                case R.id.iv_clear_phone:
                    mPhoneET.getText ().clear ();
                    break;
                case R.id.iv_clear_pwd:
                    mPasswordET.getText ().clear ();
                    break;
                case R.id.btn_login:
                    doLogin ();
                    break;
                case R.id.tv_forgetpwd:
                    ActivityStartUtils.startForgetPwdActivity (this);
                    break;
                case R.id.tv_register:
                    ActivityStartUtils.startRegisterActivity (this);
                    break;
            }
        }

        /**
         * 登录
         */
        private void doLogin () {
            if (!NetUtil.checkNet ()) {
                DialogHelp.showMessageDialog (this, getString (R.string.have_not_net));
                return;
            }
            String phone = mPhoneET.getText ().toString ().trim ();// 账号
            String pwd = mPasswordET.getText ().toString ().trim ();// 密码
            if (phone.equals ("") || pwd.equals ("")) {
                DialogHelp.showMessageDialog (this, getString (R.string.phone_pwd_cannot_null));
                return;
            }
            if (!StringUtil.isPhone (phone) && !StringUtil.isEmail (phone)) {
                DialogHelp.showMessageDialog (this, getString (R.string.phone_email_is_error));
                return;
            }

            if (pwd.length () < 6) {
                DialogHelp.showMessageDialog (this, getString (R.string.pwd_lenth));
                return;
            }

            HashMap<String, Object> agrs = new HashMap<> ();
            agrs.put ("phone", phone);
            agrs.put ("password", NetUtil.MD5 (pwd));
            NetWorkManager.getInstance ().login (StringUtil.getJson (agrs), mLoadingDialog, new NetListener () {
                @Override
                public void onSuccessResponse (String msg, JSONObject jsonObject) {
                    super.onSuccessResponse (msg, jsonObject);
                    AppPrefsContent.handlerLoginData (jsonObject);
                    finish ();
                }

                @Override
                public void onErrorResponse (int errorWhat, String message) {
                    DialogHelp.showMessageDialog (LoginActivity.this, message);
                }
            });
        }

        @Override
        public void onLeftClick (View v) {
            if (mIsExit) {
                sendBroadcast (new Intent(BroadcastConfig.ACTION_EXIT_APP));
            }
            finish ();
        }

        @Override
        public boolean onKeyDown (int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (mIsExit) {
                    sendBroadcast (new Intent (BroadcastConfig.ACTION_EXIT_APP));
                }
                finish ();
                return true;
            }
            return super.onKeyDown (keyCode, event);
        }
    }
}
