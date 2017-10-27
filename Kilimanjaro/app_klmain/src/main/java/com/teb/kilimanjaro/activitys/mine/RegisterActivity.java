package com.teb.kilimanjaro.activitys.mine;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teb.kilimanjaro.AppPrefs;
import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.activitys.BaseActivity;
import com.teb.kilimanjaro.configs.BroadcastConfig;
import com.teb.kilimanjaro.configs.HandlerConfig;
import com.teb.kilimanjaro.mvp.presenter.RegistPresenterImp;
import com.teb.kilimanjaro.mvp.view.RegistViewInf;
import com.teb.kilimanjaro.utils.AnimUtil;
import com.teb.kilimanjaro.utils.MyUtil;
import com.teb.kilimanjaro.utils.NetUtil;
import com.teb.kilimanjaro.utils.StringUtil;
import com.teb.kilimanjaro.views.dialogs.MyDialog;

import java.util.HashMap;

/**
 * Created by yangbin on 16/7/5.
 * 注册界面Activity
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener, RegistViewInf {

    private RegistPresenterImp mRegistPresenterImp;
    private Dialog mLoadingDialog;

    // 返回按钮
    private ImageView mBackIV;

    private int mCurrentStep = 0;// 当前步骤

    private View[] mProLinears = new View[4];// 进度线
    private TextView[] mProTVs = new TextView[4];// 进度数字
    private LinearLayout[] mLayouts = new LinearLayout[4];// 布局

    // 第一步
    private EditText mPhoneET;// 手机号码输入框
    private TextView mRegistBtn;// 注册按钮

    // 第二步
    private EditText mCaptchaET;// 手机验证码输入框
    private TextView mPhoneTV, mVerifyTimeTV;// 显示手机号码、验证码有效时间
    private TextView mSubmitBtn;// 提交按钮

    // 第三步
    private EditText mEmailET, mPwdET, mRePwdET, mPayPwdET, mRePayPwdET;// 邮箱、设置密码、确认密码、设置支付密码、确认支付密码
    private TextView mComplateBtn;// 完成注册按钮

    //第四步
    private TextView mEmailTV;
    private TextView mGotoLoginBtn;// 去登录

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
        mRegistPresenterImp = new RegistPresenterImp(this);
        mLoadingDialog = MyDialog.createLoadingDialog(this, false);
    }

    private void initViews() {
        // ******返回按钮
        mBackIV = (ImageView) findViewById(R.id.iv_back);
        mBackIV.setOnClickListener(this);

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
        mRegistBtn = (TextView) findViewById(R.id.btn_regist);
        mRegistBtn.setOnClickListener(this);

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
        mPayPwdET = (EditText) findViewById(R.id.et_pay_pwd);
        mRePayPwdET = (EditText) findViewById(R.id.et_repay_pwd);
        mComplateBtn = (TextView) findViewById(R.id.btn_complete);
        mComplateBtn.setOnClickListener(this);

        // 第四步
        mEmailTV = (TextView) findViewById(R.id.tv_my_email);
        mGotoLoginBtn = (TextView) findViewById(R.id.btn_goto_login);
        mGotoLoginBtn.setOnClickListener(this);

        mCurrentStep = 0;// 默认第一步
        changeLayout();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            toBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 返回
     */
    private void toBack() {
//        if (mCurrentStep == 0) {
        finish();
        AnimUtil.actFromLeftToRight(this);
//        } else {
//            mCurrentStep--;
//            changeLayout();
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:// 返回
                toBack();
                break;
            case R.id.btn_regist:// 注册
                registFirstStep();
                break;
            case R.id.btn_submit:// 提交
                if (mSubmitBtn.getText().toString().trim().equals(getResources().getString(R.string.re_send_sms))) {
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
                gotoLogin();
                break;
        }
    }

    /**
     * 注册第一步
     */
    private void registFirstStep() {
        mPhone = mPhoneET.getText().toString().trim();
        if (TextUtils.isEmpty(mPhone)) {
            MyUtil.showErrorDialog(this,getString(R.string.phone_cannot_null));
            return;
        }
        if (!StringUtil.isPhone(mPhone)) {
            MyUtil.showErrorDialog(this,getString(R.string.phone_is_error));
            return;
        }
        mLoadingDialog.show();
        HashMap<String, Object> hashMapSms = new HashMap<>();
        hashMapSms.put("phone", mPhone);
        mRegistPresenterImp.sendSMS(StringUtil.getJson(hashMapSms));
    }

    /**
     * 注册第二步
     */
    private void registSecondStep() {
        String captcha = mCaptchaET.getText().toString().trim();
        if (TextUtils.isEmpty(captcha)) {
            MyUtil.showErrorDialog(this,getString(R.string.virify_cannot_null));
            return;
        }
        mLoadingDialog.show();
        HashMap<String, Object> hashMapCapt = new HashMap<>();
        hashMapCapt.put("phone", mPhone);
        hashMapCapt.put("captcha", captcha);
        mRegistPresenterImp.sendCaptcha(StringUtil.getJson(hashMapCapt));
    }

    /**
     * 注册第三步
     */
    private void registThirdStep() {
        String email = mEmailET.getText().toString().trim();
        String pwd = mPwdET.getText().toString().trim();
        String rePwd = mRePwdET.getText().toString().trim();
        String payPwd = mPayPwdET.getText().toString().trim();
        String rePayPwd = mRePayPwdET.getText().toString().trim();
        if (TextUtils.isEmpty(email)
                || TextUtils.isEmpty(pwd)
                || TextUtils.isEmpty(rePwd)
                || TextUtils.isEmpty(payPwd)
                || TextUtils.isEmpty(rePayPwd)) {// 输入不能为空
            MyUtil.showErrorDialog(this, getString(R.string.input_cannot_null));
            return;
        }
        if (!StringUtil.isEmail(email)) {// 邮箱格式错误
            MyUtil.showErrorDialog(this, getString(R.string.email_is_error));
            return;
        }
        if (!email.contains("qq.com")) {// 必须是QQ邮箱
            MyUtil.showErrorDialog(this, getString(R.string.email_is_error));
            return;
        }
        if (pwd.length() < 6 || rePwd.length() < 6 || payPwd.length() < 6 || rePayPwd.length() < 6) {
            MyUtil.showErrorDialog(this, getString(R.string.pwd_lenth));
            return;
        }
        if (!pwd.equals(rePwd)) {// 两次登录密码不一样
            MyUtil.showErrorDialog(this, getString(R.string.loginpwd_and_repwd_not_same));
            return;
        }
        if (!payPwd.equals(rePayPwd)) {// 两次支付密码不一样
            MyUtil.showErrorDialog(this, getString(R.string.paypwd_and_repwd_not_same));
            return;
        }
//        if (pwd.equals(payPwd)) {// 登录密码和支付密码不能一样
//            ToastUtil.showToast(R.string.pwd_and_paypwd_cannot_same);
//            return;
//        }

        mLoadingDialog.show();
        HashMap<String, Object> hashMapRegist = new HashMap<>();
        hashMapRegist.put("phone", mPhone);
        hashMapRegist.put("email", email);
        hashMapRegist.put("captchaToken", mToken);
        hashMapRegist.put("loginPassword", NetUtil.MD5(pwd));
        hashMapRegist.put("payPassword", NetUtil.MD5(payPwd));
        mRegistPresenterImp.compaleRegist(StringUtil.getJson(hashMapRegist));
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
                mProTVs[i].setBackgroundResource(R.drawable.circle_red_bg);
                mLayouts[i].setVisibility(View.VISIBLE);
            } else {
                mProLinears[i].setBackgroundResource(R.color.black);
                mProTVs[i].setBackgroundResource(R.drawable.circle_black_bg);
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

    private void gotoLogin() {
        finish();
        MyUtil.startActivity(this, new Intent(this, LoginActivity.class));
    }

    // --------------------RegistViewInf method---------------------

    /**
     * 处理发送短信的数据
     *
     * @param msg
     */
    @Override
    public void sendSMSMessage(Message msg) {
        if (mLoadingDialog != null) mLoadingDialog.dismiss();
        switch (msg.what) {
            case HandlerConfig.WHAT_POST_SUCCESS:
                startTimer(msg.arg1);
                break;
            default:
                MyUtil.handMessage(this, msg, "RegisterActivity---SMS>>>");
                break;
        }
    }

    /**
     * 处理验证验证码的数据
     *
     * @param msg
     */
    @Override
    public void sendCaptchaMessage(Message msg) {
        if (mLoadingDialog != null) mLoadingDialog.dismiss();
        switch (msg.what) {
            case HandlerConfig.WHAT_POST_SUCCESS:
                mToken = (String) msg.obj;
                mCurrentStep = 2;
                changeLayout();
                break;
            default:
                MyUtil.handMessage(this, msg, "RegisterActivity---Captcha>>>");
                break;
        }
    }

    /**
     * 处理最终注册的数据
     *
     * @param msg
     */
    @Override
    public void sendRegistMessage(Message msg) {
        if (mLoadingDialog != null) mLoadingDialog.dismiss();
        switch (msg.what) {
            case HandlerConfig.WHAT_POST_SUCCESS:
                AppPrefs.getInstance().saveUserPhone(mPhone);
                mCurrentStep = 3;
                changeLayout();

                // 发送广播，关闭LoginActivity
                Intent intent = new Intent(BroadcastConfig.ACTION_REGISTER_COMLAPE);
                sendBroadcast(intent);
//                // 启动MainActivity
//                Intent intentAct = new Intent(this, MainActivity.class);
//                startActivity(intentAct);
//                AnimUtil.actFromRightToLeft(this);
//                finish();
                break;
            default:
                MyUtil.handMessage(this, msg, "RegisterActivity---Register>>>");
                break;
        }
    }
}
