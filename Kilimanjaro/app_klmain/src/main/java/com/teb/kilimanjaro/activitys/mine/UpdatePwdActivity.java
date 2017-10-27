package com.teb.kilimanjaro.activitys.mine;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.activitys.BaseActivity;
import com.teb.kilimanjaro.configs.BroadcastConfig;
import com.teb.kilimanjaro.configs.HandlerConfig;
import com.teb.kilimanjaro.mvp.presenter.UpdatePwdPresenterImp;
import com.teb.kilimanjaro.utils.MyUtil;
import com.teb.kilimanjaro.views.dialogs.MyDialog;
import com.teb.kilimanjaro.mvp.view.UpdatePwdViewInf;
import com.teb.kilimanjaro.utils.NetUtil;
import com.teb.kilimanjaro.utils.StringUtil;
import com.teb.kilimanjaro.utils.ToastUtil;
import com.teb.kilimanjaro.views.MyActionBar;

import java.util.HashMap;

/**
 * Created by yangbin on 16/7/6.
 */
public class UpdatePwdActivity extends BaseActivity implements MyActionBar.OnActionBarClickListener, View.OnClickListener, UpdatePwdViewInf {

    private MyActionBar mMyActionBar;

    // 登录密码
    private EditText[] mLoginPwdETs = new EditText[3];
    private TextView mUpdateLoginPwdBtn;// 修改登录密码按钮

    // 支付密码
    private EditText[] mPayPwdETs = new EditText[3];
    private TextView mUpdatePayPwdBtn;// 修改支付密码按钮

    private TextView mForgetPayPwdTV;// 忘记提现密码

    private UpdatePwdPresenterImp mUpdatePwdPresenterImp;
    private Dialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatepwd);
        initViews();
        mUpdatePwdPresenterImp = new UpdatePwdPresenterImp(this);
        mLoadingDialog = MyDialog.createLoadingDialog(this, false);
    }

    private void initViews() {
        // ******MyActionBar
        mMyActionBar = (MyActionBar) findViewById(R.id.actionBar);
        mMyActionBar.setBarBackGround(R.color.main_background);
        mMyActionBar.setBarLeftImage(R.drawable.ic_back_btn);
        mMyActionBar.setBarTitleText(R.string.bar_updatepwd);
        mMyActionBar.setOnActionBarClickListener(this);

        // ******修改登录密码
        mLoginPwdETs[0] = (EditText) findViewById(R.id.et_login_old_pwd);
        mLoginPwdETs[1] = (EditText) findViewById(R.id.et_login_new_pwd);
        mLoginPwdETs[2] = (EditText) findViewById(R.id.et_login_re_pwd);
        mUpdateLoginPwdBtn = (TextView) findViewById(R.id.btn_update_login_pwd);
        mUpdateLoginPwdBtn.setOnClickListener(this);

        // ******修改支付密码
        mPayPwdETs[0] = (EditText) findViewById(R.id.et_pay_old_pwd);
        mPayPwdETs[1] = (EditText) findViewById(R.id.et_pay_new_pwd);
        mPayPwdETs[2] = (EditText) findViewById(R.id.et_pay_re_pwd);
        mUpdatePayPwdBtn = (TextView) findViewById(R.id.btn_update_pay_pwd);
        mUpdatePayPwdBtn.setOnClickListener(this);

        mForgetPayPwdTV = (TextView) findViewById(R.id.tv_forget_paypwd);
        mForgetPayPwdTV.setOnClickListener(this);
    }

    @Override
    public void onActionBarLeftClicked() {
        finish();
    }

    @Override
    public void onActionBarRightClicked() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update_login_pwd:
                String oldPwd = mLoginPwdETs[0].getText().toString().trim();
                String newPwd = mLoginPwdETs[1].getText().toString().trim();
                String rePwd = mLoginPwdETs[2].getText().toString().trim();
                String loginPwdJson = virifyPwd(oldPwd, newPwd, rePwd);
                if (loginPwdJson == null) return;
                mLoadingDialog.show();
                mUpdatePwdPresenterImp.updateLoginPwd(loginPwdJson);
                break;
            case R.id.btn_update_pay_pwd:
                String oldPwd1 = mPayPwdETs[0].getText().toString().trim();
                String newPwd1 = mPayPwdETs[1].getText().toString().trim();
                String rePwd1 = mPayPwdETs[2].getText().toString().trim();
                String payPwdJson = virifyPwd(oldPwd1, newPwd1, rePwd1);
                if (payPwdJson == null) return;
                mLoadingDialog.show();
                mUpdatePwdPresenterImp.updatePayPwd(payPwdJson);
                break;
            case R.id.tv_forget_paypwd:
                new MyDialog(UpdatePwdActivity.this)
                        .setDefaultBtnText()
                        .setMessage(getString(R.string.send_new_pay_pwd))
                        .listener(new MyDialog.OnDialogClickListener() {
                            @Override
                            public void onLeftClicked() {

                            }

                            @Override
                            public void onRightClicked() {
                                mUpdatePwdPresenterImp.emailResetPayPassword(StringUtil.getNullJson());
                                mLoadingDialog.show();
                            }
                        })
                        .show();
                break;
        }
    }

    /**
     * 验证输入内容
     *
     * @param oldPwd
     * @param newPwd
     * @param rePwd
     */
    private String virifyPwd(String oldPwd, String newPwd, String rePwd) {
        if (TextUtils.isEmpty(oldPwd) ||
                TextUtils.isEmpty(newPwd) ||
                TextUtils.isEmpty(rePwd)) {
            MyUtil.showErrorDialog(this,getString(R.string.input_cannot_null));
            return null;
        }
        if (oldPwd.length()<6||newPwd.length()<6){
            MyUtil.showErrorDialog(this,getString(R.string.pwd_lenth));
            return null;
        }
        
        if (oldPwd.equals(newPwd)) {// 新旧密码不能一样
            MyUtil.showErrorDialog(this,getString(R.string.old_and_newpwd_cannot_same));
            return null;
        }

        if (!newPwd.equals(rePwd)) {
            MyUtil.showErrorDialog(this,getString(R.string.pwd_and_repwd_not_same));
            return null;
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("oldPassword", NetUtil.MD5(oldPwd));
        hashMap.put("newPassword", NetUtil.MD5(newPwd));
        return StringUtil.getJson(hashMap);
    }

    // --------------------UpdatePwdViewInf method---------------------

    @Override
    public void sendUpdateLoginPwdMessage(Message msg) {
        if (mLoadingDialog != null) mLoadingDialog.dismiss();
        switch (msg.what) {
            case HandlerConfig.WHAT_POST_SUCCESS:
                ToastUtil.showToast(R.string.update_loginpwd_success);
                finish();
                sendBroadcast(new Intent(BroadcastConfig.ACTION_LOGOUT));
                break;
            default:
                MyUtil.handMessage(this, msg, "UpdatePwdActivity---updateLoginPwd>>>");
                break;
        }
    }

    @Override
    public void sendUpdatePayPwdMessage(Message msg) {
        if (mLoadingDialog != null) mLoadingDialog.dismiss();
        switch (msg.what) {
            case HandlerConfig.WHAT_POST_SUCCESS:
                ToastUtil.showToast(R.string.update_paypwd_success);
                break;
            default:
                MyUtil.handMessage(this, msg, "UpdatePwdActivity---updatePayPwd>>>");
                break;
        }
    }

    @Override
    public void sendResetPayPwdMessage(Message msg) {
        if (mLoadingDialog != null) mLoadingDialog.dismiss();
        switch (msg.what) {
            case HandlerConfig.WHAT_POST_SUCCESS:
                new MyDialog(this)
                        .setMessage(getString(R.string.resetpaypwd_success))
                        .setRightText(getString(R.string.app_confirm))
                        .show();
                break;
            default:
                MyUtil.handMessage(this, msg, "UpdatePwdActivity---ResetPayPwd>>>");
        }
    }
}
