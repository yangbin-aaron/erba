package com.qp.app_new.activitys;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.qp.app_new.AppPrefs;
import com.qp.app_new.R;
import com.qp.app_new.dialogs.DialogHelp;
import com.qp.app_new.httpnetworks.NetWorkManager;
import com.qp.app_new.interfaces.NetListener;
import com.qp.app_new.utils.ActivityStartUtils;
import com.qp.app_new.utils.NetUtil;
import com.qp.app_new.utils.StringUtil;
import com.qp.app_new.utils.ToastUtil;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Aaron on 17/11/19.
 */

public class ModifyPwdActivity extends BaseActivity implements View.OnClickListener {
    @Override
    public int getContentView () {
        return R.layout.activity_modify_pwd;
    }

    private EditText mOldPwdET, mNewPwdET, mRePwdET;

    @Override
    public void initView () {
        initActionBar ();
        setLeftIV (R.drawable.ic_back_btn);
        setTitle (getString (R.string.modifypwd_str));

        mOldPwdET = (EditText) findViewById (R.id.et_login_old_pwd);
        mNewPwdET = (EditText) findViewById (R.id.et_login_new_pwd);
        mRePwdET = (EditText) findViewById (R.id.et_login_re_pwd);

        findViewById (R.id.btn_update_login_pwd).setOnClickListener (this);
    }

    @Override
    public void onClick (View v) {
        switch (v.getId ()) {
            case R.id.btn_update_login_pwd:
                doModifyPwd ();
                break;
        }
    }

    private void doModifyPwd () {
        String oldPwd = mOldPwdET.getText ().toString ().trim ();
        String newPwd = mNewPwdET.getText ().toString ().trim ();
        String rePwd = mRePwdET.getText ().toString ().trim ();
        if (TextUtils.isEmpty (oldPwd) ||
                TextUtils.isEmpty (newPwd) ||
                TextUtils.isEmpty (rePwd)) {
            DialogHelp.showMessageDialog (this, getString (R.string.input_cannot_null));
            return;
        }
        if (oldPwd.length () < 6 || newPwd.length () < 6) {
            DialogHelp.showMessageDialog (this, getString (R.string.pwd_lenth));
            return;
        }

        if (oldPwd.equals (newPwd)) {// 新旧密码不能一样
            DialogHelp.showMessageDialog (this, getString (R.string.old_and_newpwd_cannot_same));
            return;
        }

        if (!newPwd.equals (rePwd)) {
            DialogHelp.showMessageDialog (this, getString (R.string.pwd_and_repwd_not_same));
            return;
        }
        HashMap<String, Object> hashMap = new HashMap<> ();
        hashMap.put ("oldPassword", NetUtil.MD5 (oldPwd));
        hashMap.put ("newPassword", NetUtil.MD5 (newPwd));

        NetWorkManager.getInstance ().modifyLoginPwd (StringUtil.getJson (hashMap), mLoadingDialog, new NetListener () {
            @Override
            public void onSuccessResponse (JSONObject jsonObject) {
                super.onSuccessResponse (jsonObject);
                AppPrefs.getInstance ().saveUserJson (null);
                finish ();
                ToastUtil.showToast (R.string.update_loginpwd_success);
                ActivityStartUtils.startLoginActivity (ModifyPwdActivity.this);
            }
        });
    }
}
