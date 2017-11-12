package com.qp.app_new.activitys;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.qp.app_new.R;
import com.qp.app_new.configs.BroadcastConfig;
import com.qp.app_new.contents.AppPrefsContent;
import com.qp.app_new.dialogs.DialogHelp;
import com.qp.app_new.httpnetworks.NetWorkManager;
import com.qp.app_new.interfaces.NetListener;
import com.qp.app_new.utils.NetUtil;
import com.qp.app_new.utils.StringUtil;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Aaron on 17/11/8.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    @Override
    public void setContentView () {
        layoutId = R.layout.activity_login;
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
        setActionBarBackgroundResource (R.color.translucent);
        setLeftIV (R.drawable.ic_back_btn);

        mPhoneET = (EditText) findViewById (R.id.et_phone);
        mPasswordET = (EditText) findViewById (R.id.et_password);

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
                break;
            case R.id.tv_register:
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
            sendBroadcast (new Intent (BroadcastConfig.ACTION_EXIT_APP));
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
