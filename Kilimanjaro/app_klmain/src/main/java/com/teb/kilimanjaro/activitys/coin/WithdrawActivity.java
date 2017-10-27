package com.teb.kilimanjaro.activitys.coin;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.activitys.BaseActivity;
import com.teb.kilimanjaro.configs.BroadcastConfig;
import com.teb.kilimanjaro.configs.HandlerConfig;
import com.teb.kilimanjaro.models.entry.coin.PrepareInfoModel;
import com.teb.kilimanjaro.mvp.presenter.WithdrawPresenterImp;
import com.teb.kilimanjaro.mvp.view.WithdrawViewInf;
import com.teb.kilimanjaro.utils.MyUtil;
import com.teb.kilimanjaro.utils.StringUtil;
import com.teb.kilimanjaro.utils.ToastUtil;
import com.teb.kilimanjaro.views.MyActionBar;
import com.teb.kilimanjaro.views.dialogs.MyDialog;

import java.util.HashMap;

/**
 * Created by yangbin on 16/8/16.
 * 提现界面
 */
public class WithdrawActivity extends BaseActivity implements View.OnClickListener, WithdrawViewInf {

    private PrepareInfoModel.PrepareInfoData mPrepareInfoData;

    private WithdrawPresenterImp mWithdrawPresenterImp;
    private Dialog mLoadingDialog;

    private MyActionBar mMyActionBar;

    // 银行卡号,联系客服，可提现金额,全部提现
    private TextView mCardNoTV, mCallKefuTV, mCanWithAmtTV, mWithAllTV;
    private long mCanDrawAmt;

    private EditText mWihdrawAmountET;// 提现金额编辑框

    private EditText mCaptchaET;
    private TextView mGetCaptchaTV;// 获取验证码
    private TextView mPhoneTV;

    private TextView mSubmitWithTV;// 提交按钮

    private int mExpired = 0;
    private Handler mHandler = new Handler();// 定时器(用作验证码倒计时)
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            // 刷新数字
            if (mExpired != 0) {
                mGetCaptchaTV.setText(R.string.re_send_sms);
                mGetCaptchaTV.append("(" + mExpired + ")");
                mExpired--;
                mHandler.postDelayed(mRunnable, 1000);
            } else {
                mGetCaptchaTV.setText(R.string.re_send_sms);
                mGetCaptchaTV.setText(R.string.re_send_sms);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        mPrepareInfoData = (PrepareInfoModel.PrepareInfoData) getIntent().getSerializableExtra("data");
        initViews();
        if (mPrepareInfoData != null) {
            setData();
        }

        mWithdrawPresenterImp = new WithdrawPresenterImp(this);
        mLoadingDialog = MyDialog.createLoadingDialog(this, true);
    }

    private void initViews() {
        // ******MyActionBar
        mMyActionBar = (MyActionBar) findViewById(R.id.actionBar);
        mMyActionBar.setBarTitleText(R.string.withdraw_bar);
        mMyActionBar.setBarLeftImage(R.drawable.ic_back_btn);
        mMyActionBar.setBarBackGround(R.color.main_background);
        mMyActionBar.setOnActionBarClickListener(new MyActionBar.OnActionBarClickListener() {
            @Override
            public void onActionBarLeftClicked() {
                finish();
            }

            @Override
            public void onActionBarRightClicked() {

            }
        });

        // ******tv_card,tv_call_kefu
        mCardNoTV = (TextView) findViewById(R.id.tv_card);
        mCallKefuTV = (TextView) findViewById(R.id.tv_call_kefu);
        mCallKefuTV.setOnClickListener(this);

        mWihdrawAmountET = (EditText) findViewById(R.id.et_with_amount);
        mCanWithAmtTV = (TextView) findViewById(R.id.tv_can_with_amount);
        mWithAllTV = (TextView) findViewById(R.id.tv_with_all);
        mWithAllTV.setOnClickListener(this);

        // ******验证码
        mCaptchaET = (EditText) findViewById(R.id.et_captcha);
        mGetCaptchaTV = (TextView) findViewById(R.id.tv_get_captcha);
        mGetCaptchaTV.setOnClickListener(this);
        mPhoneTV = (TextView) findViewById(R.id.tv_show_phone);

        // ******提交
        mSubmitWithTV = (TextView) findViewById(R.id.tv_submit_with);
        mSubmitWithTV.setOnClickListener(this);
    }

    private void setData() {
        mCanDrawAmt = mPrepareInfoData.getCanDrawAmt();
        String cardNo = mPrepareInfoData.getBankNum().substring(0, 4)
                + " **** **** **** "
                + mPrepareInfoData.getBankNum().substring(mPrepareInfoData.getBankNum().length() - 4);
        mCardNoTV.setText(cardNo);
        mCanWithAmtTV.setText(getString(R.string.withdraw_can_amount, mCanDrawAmt));
        mPhoneTV.setText(getString(R.string.withdraw_mobie_hint, mPrepareInfoData.getBindPhone()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_call_kefu:// 联系客服
                MyDialog.callKefu(this);
                break;
            case R.id.tv_with_all:// 提现全部
                mWihdrawAmountET.setText(mCanDrawAmt + "");
                mWihdrawAmountET.setSelection(mWihdrawAmountET.getText().toString().trim().length());
                break;
            case R.id.tv_get_captcha:// 获取验证码
                HashMap<String, Object> hashMapG = new HashMap<>();
                hashMapG.put("phone", mPrepareInfoData.getBindPhone());
                mWithdrawPresenterImp.getCaptcha(StringUtil.getJson(hashMapG));
                mLoadingDialog.show();
                break;
            case R.id.tv_submit_with:// 提交申请
                String captcha = mCaptchaET.getText().toString().trim();
                if (TextUtils.isEmpty(captcha)) {
                    ToastUtil.showToast(R.string.virify_cannot_null);
                    return;
                }
                String drawAmt = mWihdrawAmountET.getText().toString().trim();
                if (TextUtils.isEmpty(drawAmt)) return;
                if (Long.parseLong(drawAmt) == 0) return;
                if (Long.parseLong(drawAmt) > mPrepareInfoData.getCanDrawAmt()) {
                    ToastUtil.showToast(getString(R.string.withdraw_can_amount, mCanDrawAmt));
                    mWihdrawAmountET.setText(mCanDrawAmt + "");
                    mWihdrawAmountET.setSelection(mWihdrawAmountET.getText().toString().trim().length());
                    return;
                }
                HashMap<String, Object> hashMapV = new HashMap<>();
                hashMapV.put("phone", mPrepareInfoData.getBindPhone());
                hashMapV.put("captcha", captcha);
                mWithdrawPresenterImp.verityCaptcha(StringUtil.getJson(hashMapV));
                mLoadingDialog.show();
                break;
        }
    }

    // --------------------WithdrawViewInf method---------------------


    @Override
    public void sendGetCaptchaMessage(Message msg) {
        if (mLoadingDialog != null) mLoadingDialog.dismiss();
        switch (msg.what) {
            case HandlerConfig.WHAT_POST_SUCCESS:
                mExpired = msg.arg1;
                mHandler.post(mRunnable);
                break;
            default:
                MyUtil.handMessage(this, msg, "WithdrawActivity---GetCaptcha>>>");
        }
    }

    @Override
    public void sendVerityCaptchaMessage(Message msg) {
        switch (msg.what) {
            case HandlerConfig.WHAT_POST_SUCCESS:
                // 验证成功，提交
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("drawAmt", mWihdrawAmountET.getText().toString().trim());
                mWithdrawPresenterImp.withdraw(StringUtil.getJson(hashMap));
                break;
            default:
                if (mLoadingDialog != null) mLoadingDialog.dismiss();
                MyUtil.handMessage(this, msg, "WithdrawActivity---VerityCaptcha>>>");
        }
    }

    @Override
    public void sendWithdrawMessage(Message msg) {
        if (mLoadingDialog != null) mLoadingDialog.dismiss();
        switch (msg.what) {
            case HandlerConfig.WHAT_POST_SUCCESS:
                ToastUtil.showToast(R.string.withdraw_success);
                sendBroadcast(new Intent(BroadcastConfig.ACTION_UPDATE_NEW_GAMECOIN));
                finish();
                break;
            default:
                MyUtil.handMessage(this, msg, "WithdrawActivity---Withdraw>>>");
        }
    }
}
