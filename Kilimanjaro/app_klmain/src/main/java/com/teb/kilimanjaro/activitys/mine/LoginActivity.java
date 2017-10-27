package com.teb.kilimanjaro.activitys.mine;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.teb.kilimanjaro.AppPrefs;
import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.activitys.BaseActivity;
import com.teb.kilimanjaro.activitys.MainActivity;
import com.teb.kilimanjaro.configs.BroadcastConfig;
import com.teb.kilimanjaro.configs.HandlerConfig;
import com.teb.kilimanjaro.mvp.presenter.LoginPresenterImp;
import com.teb.kilimanjaro.mvp.view.LoginViewInf;
import com.teb.kilimanjaro.utils.MyUtil;
import com.teb.kilimanjaro.utils.NetUtil;
import com.teb.kilimanjaro.utils.StringUtil;
import com.teb.kilimanjaro.views.dialogs.MyDialog;

import java.util.HashMap;

/**
 * Created by yangbin on 16/7/4.
 * 登录界面Activity
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, LoginViewInf {

    // 登录按钮、访客登录按钮、忘记密码、去注册
    private TextView mLoginBtn, mVisitorLoginBtn, mForgetPwdBtn, mRegisterBtn;

    // 手机号码输入框、密码输入框
    private EditText mPhoneET, mPasswordET;
    // 清除账户和密码内容输入框
    private ImageView mClearPhoneIV, mClearPwdIV;

    // 退出程序
    private ImageView mExitTV;

    // mvp 登录 Presenter层
    private LoginPresenterImp mLoginPresenterImp;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BroadcastConfig.ACTION_REGISTER_COMLAPE)) {
                finish();
            }
        }
    };

    private Dialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginPresenterImp = new LoginPresenterImp(this);
        mLoadingDialog = MyDialog.createLoadingDialog(this, false);
        initViews();

        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastConfig.ACTION_REGISTER_COMLAPE);// 注册完成，需要关闭该页面
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    private void initViews() {
        mLoginBtn = (TextView) findViewById(R.id.btn_login);
        mLoginBtn.setOnClickListener(this);
        mVisitorLoginBtn = (TextView) findViewById(R.id.btn_visitorlogin);
        mVisitorLoginBtn.setOnClickListener(this);
        mForgetPwdBtn = (TextView) findViewById(R.id.tv_forgetpwd);
        mForgetPwdBtn.setOnClickListener(this);
        mRegisterBtn = (TextView) findViewById(R.id.tv_register);
        mRegisterBtn.setOnClickListener(this);

        mPhoneET = (EditText) findViewById(R.id.et_phone);
        mPasswordET = (EditText) findViewById(R.id.et_password);
        if (AppPrefs.getInstance().getUserPhone() != null) {
            mPhoneET.setText(AppPrefs.getInstance().getUserPhone());
            mPasswordET.setFocusable(true);
            mPasswordET.setFocusableInTouchMode(true);
            mPasswordET.requestFocus();
        }

        mClearPhoneIV = (ImageView) findViewById(R.id.iv_clear_phone);
        mClearPhoneIV.setOnClickListener(this);
        mClearPwdIV = (ImageView) findViewById(R.id.iv_clear_pwd);
        mClearPwdIV.setOnClickListener(this);

        mExitTV = (ImageView) findViewById(R.id.iv_exit);
        mExitTV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_clear_phone:// 清除输入框账户
                mPhoneET.getText().clear();
                break;
            case R.id.iv_clear_pwd:// 清除输入框密码
                mPasswordET.getText().clear();
                break;
            case R.id.btn_login:// 登录
                login();
                break;
            case R.id.btn_visitorlogin:// 访客登录
                //  book
                break;
            case R.id.tv_forgetpwd:// 忘记密码(重置密码)
                MyUtil.startActivity(LoginActivity.this, new Intent(LoginActivity.this, ResetPwdActivity.class));
                break;
            case R.id.tv_register:// 注册
                MyUtil.startActivity(LoginActivity.this, new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.iv_exit:// 退出程序
                finish();
                break;
        }
    }

    /**
     * 登录
     */
    private void login() {
        if (!NetUtil.checkNet()) {
            MyUtil.showErrorDialog(this, getString(R.string.have_not_net));
            return;
        }
        String phone = mPhoneET.getText().toString().trim();// 账号
        String pwd = mPasswordET.getText().toString().trim();// 密码
        if (phone.equals("") || pwd.equals("")) {
            MyUtil.showErrorDialog(this, getString(R.string.phone_pwd_cannot_null));
            return;
        }
        if (!StringUtil.isPhone(phone) && !StringUtil.isEmail(phone)) {
            MyUtil.showErrorDialog(this, getString(R.string.phone_email_is_error));
            return;
        }

        if (pwd.length() < 6) {
            MyUtil.showErrorDialog(this, getString(R.string.pwd_lenth));
            return;
        }

        mLoadingDialog.show();

        HashMap<String, Object> agrs = new HashMap<>();
        agrs.put("phone", phone);
        agrs.put("password", NetUtil.MD5(pwd));
        mLoginPresenterImp.login(StringUtil.getJson(agrs));
    }

    // --------------------LoginViewInf method---------------------

    /**
     * 处理登录事项
     *
     * @param msg
     */
    @Override
    public void sendLoginMessage(Message msg) {
        if (mLoadingDialog != null) mLoadingDialog.dismiss();
        switch (msg.what) {
            case HandlerConfig.WHAT_POST_SUCCESS:
                MyUtil.startActivity(this, new Intent(this, MainActivity.class));
                finish();
                break;
            case 10002:// 未激活
                new MyDialog(this)
                        .setMessage((String) msg.obj)
                        .setLeftText(getString(R.string.app_cancel))
                        .setRightText(getString(R.string.re_send_email))
                        .listener(new MyDialog.OnDialogClickListener() {
                            @Override
                            public void onLeftClicked() {

                            }

                            @Override
                            public void onRightClicked() {
                                HashMap<String, Object> hashMap = new HashMap<String, Object>();
                                hashMap.put("phone", mPhoneET.getText().toString().trim());
                                mLoginPresenterImp.emailActiveAccount(StringUtil.getJson(hashMap));
                                mLoadingDialog.show();
                            }
                        })
                        .show();
                break;
            default:
                MyUtil.handMessage(this, msg, "LoginActivity---login>>>");
                break;
        }
    }

    @Override
    public void sendActiveAccountMessage(Message msg) {
        if (mLoadingDialog != null) mLoadingDialog.dismiss();
        switch (msg.what) {
            case HandlerConfig.WHAT_POST_SUCCESS:
                new MyDialog(this)
                        .setMessage(getString(R.string.activeaccount_success))
                        .setRightText(getString(R.string.app_confirm))
                        .show();
                break;
            default:
                MyUtil.handMessage(this, msg, "LoginActivity---ActiveAccount>>>");
                break;
        }
    }
}
