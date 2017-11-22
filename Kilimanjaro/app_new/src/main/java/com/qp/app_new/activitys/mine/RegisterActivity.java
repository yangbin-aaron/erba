package com.qp.app_new.activitys.mine;

import android.app.Dialog;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qp.app_new.AppPrefs;
import com.qp.app_new.R;
import com.qp.app_new.activitys.BaseActivity;
import com.qp.app_new.dialogs.DialogHelp;
import com.qp.app_new.httpnetworks.NetWorkManager;
import com.qp.app_new.interfaces.NetListener;
import com.qp.app_new.interfaces.NormalDialogListener1;
import com.qp.app_new.utils.NetUtil;
import com.qp.app_new.utils.StringUtil;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Aaron on 17/11/18.
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    @Override
    public int getContentView() {
        return R.layout.activity_register;
    }

    private int mCurrentStep = 0;// 当前步骤

    private View[] mProLinears = new View[4];// 进度线
    private TextView[] mProTVs = new TextView[4];// 进度数字
    private LinearLayout[] mLayouts = new LinearLayout[4];// 布局

    // 第一步
    private EditText mPhoneET;// 手机号码输入框

    // 第二步
    private EditText mCaptchaET;// 手机验证码输入框
    private TextView mPhoneTV, mVerifyTimeTV;// 显示手机号码、验证码有效时间
    private TextView mSubmitBtn;// 提交按钮

    // 第三步
    private EditText mEmailET, mPwdET, mRePwdET;// 邮箱、设置密码、确认密码

    //第四步
    private TextView mEmailTV;

    private int mExpired = 0;
    private Handler mHandler = new Handler();// 定时器(用作验证码倒计时)
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            // 刷新数字
            if (mExpired != 0) {
                mVerifyTimeTV.setText(R.string.re_send_sms);
                mVerifyTimeTV.append("(" + mExpired + ")");
                mExpired--;
                mHandler.postDelayed(mRunnable, 1000);
            } else {
                mVerifyTimeTV.setText(R.string.re_send_sms);
                mSubmitBtn.setText(R.string.re_send_sms);
            }
        }
    };

    private String mPhone;// 输入的电话
    private String mToken;// 验证验证码时传回来的token

    @Override
    public void initView() {
        initActionBar();
        setBackgroundResource(R.color.translucent);
        setActionBarBackgroundResource(R.color.translucent);
        setLeftIV(R.drawable.ic_back_btn);
        setTitle(R.string.regist_btn);

        // ******三个步骤公共view
        mLayouts[0] = (LinearLayout) findViewById(R.id.ll_1);
        mLayouts[1] = (LinearLayout) findViewById(R.id.ll_2);
        mLayouts[2] = (LinearLayout) findViewById(R.id.ll_3);
        mLayouts[3] = (LinearLayout) findViewById(R.id.ll_4);
        mProLinears[0] = findViewById(R.id.view_1);
        mProLinears[1] = findViewById(R.id.view_2);
        mProLinears[2] = findViewById(R.id.view_3);
        mProLinears[3] = findViewById(R.id.view_4);
        mProTVs[0] = (TextView) findViewById(R.id.tv_1);
        mProTVs[1] = (TextView) findViewById(R.id.tv_2);
        mProTVs[2] = (TextView) findViewById(R.id.tv_3);
        mProTVs[3] = (TextView) findViewById(R.id.tv_4);

        // 第一步View
        mPhoneET = (EditText) findViewById(R.id.et_phone);
        findViewById(R.id.btn_regist).setOnClickListener(this);
        findViewById(R.id.iv_clear_phone).setOnClickListener(this);

        // 第二步View
        mPhoneTV = (TextView) findViewById(R.id.tv_phone);
        mCaptchaET = (EditText) findViewById(R.id.et_verificode);
        mVerifyTimeTV = (TextView) findViewById(R.id.tv_resend_sms);
        mSubmitBtn = (TextView) findViewById(R.id.btn_submit);
        mSubmitBtn.setOnClickListener(this);


        // 第三步View
        mEmailET = (EditText) findViewById(R.id.et_emal);
        mPwdET = (EditText) findViewById(R.id.et_pwd);
        mRePwdET = (EditText) findViewById(R.id.et_repwd);
        findViewById(R.id.btn_complete).setOnClickListener(this);

        // 第四步
        mEmailTV = (TextView) findViewById(R.id.tv_my_email);
        findViewById(R.id.btn_goto_login).setOnClickListener(this);

        mCurrentStep = 0;// 默认第一步
        changeLayout();
    }

    /**
     * 根据进度刷新View
     */
    private void changeLayout() {
        if (mCurrentStep != 1 && mExpired > 0) {
            mHandler.removeCallbacks(mRunnable);
            mExpired = 0;
        } else {
            mSubmitBtn.setText(R.string.submit_btn);
        }
        mPhoneTV.setText("(+86)" + mPhone);
        mEmailTV.setText(mEmailET.getText().toString().trim());
        for (int i = 0; i < 4; i++) {
            if (i == mCurrentStep) {
                mProLinears[i].setBackgroundResource(R.color.main_background);
                mProTVs[i].setBackgroundResource(R.drawable.shape_circle_main_str0);
                mLayouts[i].setVisibility(View.VISIBLE);
            } else {
                mProLinears[i].setBackgroundResource(R.color.gray_xx);
                mProTVs[i].setBackgroundResource(R.drawable.shape_circle_grayxx_str0);
                mLayouts[i].setVisibility(View.GONE);
            }
        }
    }

    /**
     * 开启定时器
     *
     * @param expired
     */
    private void startTimer(int expired) {
        mExpired = expired;
        Log.e("aaron", "expired = " + mExpired);
        mHandler.post(mRunnable);
        mCurrentStep = 1;
        changeLayout();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mExpired > 0) {
            mHandler.removeCallbacks(mRunnable);
        }
    }

    private Dialog mDialogBack;

    /**
     * 退出注册
     */
    private void toBack() {
        if (mDialogBack == null) {
            mDialogBack = DialogHelp.createOkDialog(this, getString(R.string.finish_register), new NormalDialogListener1() {
                @Override
                public void onOkClickListener() {
                    finish();
                }
            });
        }
        mDialogBack.show();
    }

    @Override
    public void onLeftClick(View v) {
        toBack();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            toBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_regist:// 注册
                registFirstStep();
                break;
            case R.id.iv_clear_phone:
                mPhoneET.getText().clear();
                break;
            case R.id.btn_submit:// 提交验证码
                if (mSubmitBtn.getText().toString().trim().equals(getString(R.string.re_send_sms))) {
                    // 重新发送
                    registFirstStep();
                    return;
                }
                registSecondStep();
                break;
            case R.id.btn_complete:// 完成
                registThirdStep();
                break;
            case R.id.btn_goto_login:
                finish();
                break;
        }
    }

    /**
     * 注册第一步
     */
    private void registFirstStep() {
        mPhone = mPhoneET.getText().toString().trim();
        if (TextUtils.isEmpty(mPhone)) {
            DialogHelp.showMessageDialog(this, getString(R.string.phone_cannot_null));
            return;
        }
        if (!StringUtil.isPhone(mPhone)) {
            DialogHelp.showMessageDialog(this, getString(R.string.phone_is_error));
            return;
        }
        HashMap<String, Object> hashMapSms = new HashMap<>();
        hashMapSms.put("phone", mPhone);
        NetWorkManager.getInstance().sendRegistSMS(StringUtil.getJson(hashMapSms), mLoadingDialog, new NetListener() {
            @Override
            public void onSuccessResponse(String msg, JSONObject jsonObject) {
                super.onSuccessResponse(msg, jsonObject);
                startTimer(jsonObject.optInt("expired"));
            }
        });
    }

    /**
     * 注册第二步
     */
    private void registSecondStep() {
        String captcha = mCaptchaET.getText().toString().trim();
        if (TextUtils.isEmpty(captcha)) {
            DialogHelp.showMessageDialog(this, getString(R.string.virify_cannot_null));
            return;
        }
        mLoadingDialog.show();
        HashMap<String, Object> hashMapCapt = new HashMap<>();
        hashMapCapt.put("phone", mPhone);
        hashMapCapt.put("captcha", captcha);
        NetWorkManager.getInstance().verityCaptcha(StringUtil.getJson(hashMapCapt), mLoadingDialog, new NetListener() {
            @Override
            public void onSuccessResponse(JSONObject jsonObject) {
                super.onSuccessResponse(jsonObject);
                mToken = jsonObject.optString("captchaToken");
                mCurrentStep = 2;
                changeLayout();
            }
        });
    }

    /**
     * 注册第三步
     */
    private void registThirdStep() {
        String email = mEmailET.getText().toString().trim();
        String pwd = mPwdET.getText().toString().trim();
        String rePwd = mRePwdET.getText().toString().trim();
        if (TextUtils.isEmpty(email)
                || TextUtils.isEmpty(pwd)
                || TextUtils.isEmpty(rePwd)) {// 输入不能为空
            DialogHelp.showMessageDialog(this, getString(R.string.input_cannot_null));
            return;
        }
        if (!StringUtil.isEmail(email)) {// 邮箱格式错误
            DialogHelp.showMessageDialog(this, getString(R.string.email_is_error));
            return;
        }
        if (!email.contains("qq.com")) {// 必须是QQ邮箱
            DialogHelp.showMessageDialog(this, getString(R.string.email_is_error));
            return;
        }
        if (pwd.length() < 6 || rePwd.length() < 6) {
            DialogHelp.showMessageDialog(this, getString(R.string.pwd_lenth));
            return;
        }
        if (!pwd.equals(rePwd)) {// 两次登录密码不一样
            DialogHelp.showMessageDialog(this, getString(R.string.loginpwd_and_repwd_not_same));
            return;
        }

        mLoadingDialog.show();
        HashMap<String, Object> hashMapRegist = new HashMap<>();
        hashMapRegist.put("phone", mPhone);
        hashMapRegist.put("email", email);
        hashMapRegist.put("captchaToken", mToken);
        hashMapRegist.put("loginPassword", NetUtil.MD5(pwd));
        hashMapRegist.put("payPassword", NetUtil.MD5(pwd));//默认
        NetWorkManager.getInstance().compaleRegist(StringUtil.getJson(hashMapRegist), mLoadingDialog, new NetListener() {
            @Override
            public void onSuccessResponse(JSONObject jsonObject) {
                super.onSuccessResponse(jsonObject);
                AppPrefs.getInstance().saveUserPhone(mPhone);
                mCurrentStep = 3;
                changeLayout();
            }
        });
    }
}
