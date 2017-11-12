package com.qp.app_new.interfaces;

import android.app.Activity;

import com.qp.app_new.AppPrefs;
import com.qp.app_new.R;
import com.qp.app_new.configs.NetStatusConfig;
import com.qp.app_new.dialogs.DialogHelp;
import com.qp.app_new.utils.ActivityStartUtils;
import com.qp.app_new.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Aaron on 2017/11/8.
 */

public class NetListener {
    private Activity mActivity;
    private boolean mCloseActivity = true;// 登陆异常时，是否需要关闭Activity

    public NetListener () {
    }

    public NetListener (Activity activity) {
        mActivity = activity;
    }

    /**
     * @param activity
     * @param closeActivity 默认为true
     */
    public NetListener (Activity activity, boolean closeActivity) {
        mActivity = activity;
        mCloseActivity = closeActivity;
    }


    public void onResponse (String json) {

    }

    public void onSuccessResponse (String msg, JSONObject jsonObject) {

    }

    public void onSuccessResponse (String msg, JSONArray jsonArray) {
    }

    public void onErrorResponse (int errorWhat, String message) {
        switch (errorWhat) {
            case NetStatusConfig.STATUS_DATA_WRONG:
            case NetStatusConfig.STATUS_POST_FAIL:
            case NetStatusConfig.STATUS_NET_ERROR:
            case NetStatusConfig.STATUS_HAVE_NO_DATA:
                ToastUtil.showToast (message);
                break;
            case NetStatusConfig.STATUS_TOKEN_IS_UPDATED:
                if (mActivity != null) {
                    if (AppPrefs.getInstance ().getTokenState ()) {
                        AppPrefs.getInstance ().saveUserJson (null);
                        AppPrefs.getInstance ().saveTokenState (false);// 不能够访问
                        DialogHelp.createMustDialog (mActivity, mActivity.getString (R.string.have_login_wrong), new NormalDialogListener1 () {
                            @Override
                            public void onOkClickListener () {
                                if (mCloseActivity) mActivity.finish ();
                                ActivityStartUtils.startLoginActivity (mActivity, true);
                            }
                        }).show ();
                    }
                    break;
                }
        }
    }
}
