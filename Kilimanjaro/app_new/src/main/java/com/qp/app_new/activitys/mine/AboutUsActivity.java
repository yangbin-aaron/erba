package com.qp.app_new.activitys.mine;

import android.app.Dialog;
import android.view.View;
import android.widget.TextView;

import com.qp.app_new.App;
import com.qp.app_new.AppPrefs;
import com.qp.app_new.R;
import com.qp.app_new.activitys.BaseActivity;
import com.qp.app_new.dialogs.DialogHelp;
import com.qp.app_new.httpnetworks.NetWorkManager;
import com.qp.app_new.interfaces.NetListener;
import com.qp.app_new.interfaces.NormalDialogListener1;
import com.qp.app_new.utils.AppUtils;

import org.json.JSONObject;

/**
 * Created by Aaron on 17/11/10.
 */

public class AboutUsActivity extends BaseActivity implements View.OnClickListener {
    @Override
    public int getContentView() {
        return R.layout.activity_aboutus;
    }

    @Override
    public void initView() {
        initActionBar();
        setTitle(R.string.about_str);
        setLeftIV(R.drawable.ic_back_btn);

        ((TextView) findViewById(R.id.tv_vision)).setText(getString(R.string.visition, AppUtils.getAppVersionName()));
        ((TextView) findViewById(R.id.tv_work_time)).setText(AppPrefs.getInstance().getSysInfoWorkTime());

        findViewById(R.id.rl_qq).setOnClickListener(this);
        findViewById(R.id.rl_wx).setOnClickListener(this);
        findViewById(R.id.rl_update).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_qq:
                final String kefu_qq_num_default = AppPrefs.getInstance().getSysInfoQQ();
                DialogHelp.showMessageDialog(this, getString(R.string.kefu_qq_, kefu_qq_num_default));
                break;
            case R.id.rl_phone:
                @Deprecated
                final String kefu_phone_default = AppPrefs.getInstance().getSysInfoPhone();
                DialogHelp.showMessageDialog(this, getString(R.string.kefu_phone_, kefu_phone_default));
                break;
            case R.id.rl_wx:
                final String kefu_wx_default = AppPrefs.getInstance().getSysInfoWX();
                DialogHelp.showMessageDialog(this, getString(R.string.kefu_wx_, kefu_wx_default));
                break;
            case R.id.rl_update:
                NetWorkManager.getInstance().updateApp(mLoadingDialog, new NetListener() {
                    @Override
                    public void onSuccessResponse(String msg, JSONObject jsonObject) {
                        super.onSuccessResponse(msg, jsonObject);
                        updateVersion(jsonObject);
                    }
                });
                break;
        }
    }

    private Dialog mDialogUpdate;

    private void updateVersion(final JSONObject jsonObject) {
        // 如果成功则继续获取其他信息(判断是否需要更新，再执行下面的操作)
        int status = jsonObject.optInt("versionStatus", 0);
        App app = (App) getApplication();
        app.haveNewVersion = status != 0;
        switch (status) {
            case 0:
                DialogHelp.showMessageDialog(this, getString(R.string.update_app_newest));
                break;
            case -1:
            case 1:
                app.haveNewVersion = true;// 有新版本
                if (mDialogUpdate == null) {
                    mDialogUpdate = DialogHelp.createOkDialog(this, getString(R.string.app_updateapp_), new NormalDialogListener1() {
                        @Override
                        public void onOkClickListener() {
                            AppUtils.startDownApk(AboutUsActivity.this, jsonObject.optString("url"));
                        }
                    });
                }
                mDialogUpdate.show();
                break;
        }
    }
}
