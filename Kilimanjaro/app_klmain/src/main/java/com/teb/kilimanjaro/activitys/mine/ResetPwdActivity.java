package com.teb.kilimanjaro.activitys.mine;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.activitys.BaseActivity;
import com.teb.kilimanjaro.configs.HandlerConfig;
import com.teb.kilimanjaro.mvp.presenter.ResetPwdPresenterImp;
import com.teb.kilimanjaro.mvp.view.ResetPwdViewInf;
import com.teb.kilimanjaro.utils.MyUtil;
import com.teb.kilimanjaro.utils.NetUtil;
import com.teb.kilimanjaro.utils.StringUtil;
import com.teb.kilimanjaro.views.dialogs.MyDialog;

import java.util.HashMap;

/**
 * Created by yangbin on 16/7/7.
 * 重置密码Activity
 */
public class ResetPwdActivity extends BaseActivity implements View.OnClickListener, ResetPwdViewInf {

    private ResetPwdPresenterImp mResetPwdPresenterImp;
    private Dialog mLoadingDialog;

    // 返回按钮
    private ImageView mBackIV;

    private int mCurrentStep = 0;// 当前步骤

    private LinearLayout mProLayout;
    private View[] mProLinears = new View[2];// 进度线
    private TextView[] mProTVs = new TextView[2];// 进度数字
    private LinearLayout[] mLayouts = new LinearLayout[3];// 布局

    private EditText mPhoneET, mCaptchaET;// 电话输入，验证码输入

    private LinearLayout mInputCaptchaLL;
    private TextView mGetCaptchaBtn, mNextBtn, mCallBtn;// 获取验证码，下一步，致电客服

    private RelativeLayout mShowExpiredRL;
    private TextView mShowPhoneTV, mShowExpiredTV;// 显示手机号码，显示倒计时

    private EditText mNewPwdET, mReNewPwdET;// 新密码，确认新密码
    private TextView mSubmitBtn;// 提交

    private TextView mGoToLoginBtn;// 去登录
    private TextView mComProTV;// 完成进度提示

    private int mExpired = 0;
    private Handler mHandler = new Handler();// 定时器(用作验证码倒计时)
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mShowPhoneTV.setText(mPhone);
            mInputCaptchaLL.setVisibility(View.VISIBLE);
            // 刷新数字
            if (mExpired != 0) {
                mShowExpiredRL.setVisibility(View.VISIBLE);
                mGetCaptchaBtn.setVisibility(View.GONE);
                mShowExpiredTV.setText(R.string.re_send_sms);
                mShowExpiredTV.append("(" + mExpired + ")");
                mExpired--;
                mHandler.postDelayed(mRunnable, 1000);
            } else {
                mShowExpiredRL.setVisibility(View.GONE);
                mGetCaptchaBtn.setVisibility(View.VISIBLE);
            }
        }
    };

    private String mPhone;// 输入的电话
    private String mToken;// 验证验证码时传回来的token
    private String mPassword;// 密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_loginpwd);
        initViews();
        mResetPwdPresenterImp = new ResetPwdPresenterImp(this);
        mLoadingDialog = MyDialog.createLoadingDialog(this, false);
    }

    private void initViews() {
        // ******返回按钮
        mBackIV = (ImageView) findViewById(R.id.iv_back);
        mBackIV.setOnClickListener(this);

        // ******三个步骤公共view
        mProLayout = (LinearLayout) findViewById(R.id.ll_pro);
        mLayouts[0] = (LinearLayout) findViewById(R.id.ll_1);
        mLayouts[1] = (LinearLayout) findViewById(R.id.ll_2);
        mLayouts[2] = (LinearLayout) findViewById(R.id.ll_3);
        mProLinears[0] = findViewById(R.id.view_1);
        mProLinears[1] = findViewById(R.id.view_2);
        mProTVs[0] = (TextView) findViewById(R.id.tv_1);
        mProTVs[1] = (TextView) findViewById(R.id.tv_2);

        // ******获取验证码部分
        mPhoneET = (EditText) findViewById(R.id.et_phone);
        mGetCaptchaBtn = (TextView) findViewById(R.id.btn_get_captcha);
        mGetCaptchaBtn.setOnClickListener(this);
        mShowExpiredRL = (RelativeLayout) findViewById(R.id.rl_show_captcha);
        mShowPhoneTV = (TextView) findViewById(R.id.tv_phone);
        mShowExpiredTV = (TextView) findViewById(R.id.tv_expired);

        mCallBtn = (TextView) findViewById(R.id.tv_call_phone);
        mCallBtn.setOnClickListener(this);

        // ******下一步
        mInputCaptchaLL = (LinearLayout) findViewById(R.id.ll_input_captcha);
        mCaptchaET = (EditText) findViewById(R.id.et_captcha);
        mNextBtn = (TextView) findViewById(R.id.btn_next);
        mNextBtn.setOnClickListener(this);

        // ******输入新密码
        mNewPwdET = (EditText) findViewById(R.id.et_new_pwd);
        mReNewPwdET = (EditText) findViewById(R.id.et_re_new_pwd);
        mSubmitBtn = (TextView) findViewById(R.id.btn_submit);
        mSubmitBtn.setOnClickListener(this);

        // ******完成设置
        mComProTV = (TextView) findViewById(R.id.tv_reset_pro_hint);
        mGoToLoginBtn = (TextView) findViewById(R.id.btn_goto_login);
        mGoToLoginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:// 返回
                finish();
                break;
            case R.id.btn_get_captcha:// 获取验证码
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
                mResetPwdPresenterImp.sendSMS(StringUtil.getJson(hashMapSms));
                break;
            case R.id.tv_call_phone:// 致电客服
                MyDialog.callKefu(this);
                break;
            case R.id.btn_next:// 下一步
                String captcha = mCaptchaET.getText().toString().trim();
                if (TextUtils.isEmpty(captcha)) {
                    MyUtil.showErrorDialog(this,getString(R.string.virify_cannot_null));
                    return;
                }
                mLoadingDialog.show();
                HashMap<String, Object> hashMapCapt = new HashMap<>();
                hashMapCapt.put("phone", mPhone);
                hashMapCapt.put("captcha", captcha);
                mResetPwdPresenterImp.sendCaptcha(StringUtil.getJson(hashMapCapt));
                break;
            case R.id.btn_submit:// 提交密码
                String pwd = mNewPwdET.getText().toString().trim();
                String rePwd = mReNewPwdET.getText().toString().trim();
                if (TextUtils.isEmpty(pwd) || TextUtils.isEmpty(rePwd)) {
                    MyUtil.showErrorDialog(this,getString(R.string.input_cannot_null));
                    return;
                }

                if (pwd.length()<6||rePwd.length()<6){
                    MyUtil.showErrorDialog(this,getString(R.string.pwd_lenth));
                    return;
                }
                
                if (!pwd.equals(rePwd)) {
                    MyUtil.showErrorDialog(this,getString(R.string.pwd_and_repwd_not_same));
                    return;
                }

                mPassword = pwd;
                mLoadingDialog.show();
                HashMap<String, Object> hashMapReset = new HashMap<>();
                hashMapReset.put("phone", mPhone);
                hashMapReset.put("password", NetUtil.MD5(mPassword));
                hashMapReset.put("captchaToken", mToken);
                mResetPwdPresenterImp.resetPwd(StringUtil.getJson(hashMapReset));
                break;
            case R.id.btn_goto_login:
                finish();
                break;
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mExpired > 0) {
            mHandler.removeCallbacks(mRunnable);
        }
    }

    /**
     * 根据进度刷新View
     */
    private void changeLayout() {
        if (mCurrentStep != 1 && mExpired > 0) {
            mHandler.removeCallbacks(mRunnable);
            mExpired = 0;
        }
        if (mCurrentStep == 2) {
            mComProTV.setText(R.string.pwd_reset_success);
            mProLayout.setVisibility(View.INVISIBLE);
            mLayouts[0].setVisibility(View.GONE);
            mLayouts[1].setVisibility(View.GONE);
            mLayouts[2].setVisibility(View.VISIBLE);
            return;
        }
        for (int i = 0; i < 2; i++) {
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

    // --------------------ResetPwdViewInf method---------------------

    @Override
    public void sendSMSMessage(Message msg) {
        if (mLoadingDialog != null) mLoadingDialog.dismiss();
        switch (msg.what) {
            case HandlerConfig.WHAT_POST_SUCCESS:
                startTimer(msg.arg1);
                break;
            default:
                MyUtil.handMessage(this, msg, "ResetPwdActivity---SMS>>>");
                break;
        }
    }

    @Override
    public void sendCaptchaMessage(Message msg) {
        if (mLoadingDialog != null) mLoadingDialog.dismiss();
        switch (msg.what) {
            case HandlerConfig.WHAT_POST_SUCCESS:
                mToken = (String) msg.obj;
                mCurrentStep = 1;
                changeLayout();
                break;
            default:
                MyUtil.handMessage(this, msg, "RegisterActivity---Captcha>>>");
                break;
        }
    }

    @Override
    public void sendResetMessage(Message msg) {
        if (mLoadingDialog != null) mLoadingDialog.dismiss();
        switch (msg.what) {
            case HandlerConfig.WHAT_POST_SUCCESS:
                mCurrentStep = 2;
                changeLayout();
                break;
            default:
                MyUtil.handMessage(this, msg, "RegisterActivity---Reset>>>");
                break;
        }
    }
}
