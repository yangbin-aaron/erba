package com.teb.kilimanjaro.activitys.mine;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.teb.kilimanjaro.App;
import com.teb.kilimanjaro.AppPrefs;
import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.activitys.BaseActivity;
import com.teb.kilimanjaro.configs.HandlerConfig;
import com.teb.kilimanjaro.models.entry.mine.BasicInfoModel;
import com.teb.kilimanjaro.models.entry.mine.VersionStatusModel;
import com.teb.kilimanjaro.mvp.presenter.AboutPresenterImp;
import com.teb.kilimanjaro.mvp.view.AboutViewInf;
import com.teb.kilimanjaro.utils.AppUtils;
import com.teb.kilimanjaro.utils.MyUtil;
import com.teb.kilimanjaro.utils.StringUtil;
import com.teb.kilimanjaro.views.MyActionBar;
import com.teb.kilimanjaro.views.dialogs.DownApkDialog;
import com.teb.kilimanjaro.views.dialogs.MyDialog;


/**
 * Created by yangbin on 16/7/6.
 */
public class AboutActivity extends BaseActivity implements MyActionBar.OnActionBarClickListener, View.OnClickListener, AboutViewInf {

    private MyActionBar mMyActionBar;

    private RelativeLayout mKefuQQBtn, mKefuPhoneBtn, mUpdateAppBtn;
    private TextView mVisitionTV;

    private View mNewVersionView;// 新版本通知的红点
    private AboutPresenterImp mAboutPresenterImp;
    private Dialog mLoadingDialog;
    private BasicInfoModel mBasicInfoModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        mBasicInfoModel = AppPrefs.getInstance().getBasicInfoModel();
        if (mBasicInfoModel == null) {// 获取默认的号码
            mBasicInfoModel = new BasicInfoModel();
            mBasicInfoModel.setData(new BasicInfoModel.BasicInfoData());
        }
        initViews();
        mAboutPresenterImp = new AboutPresenterImp(this);
        mLoadingDialog = MyDialog.createLoadingDialog(this, true);
    }

    private void initViews() {
        // ******ActionBar
        mMyActionBar = (MyActionBar) findViewById(R.id.actionBar);
        mMyActionBar.setBarLeftImage(R.drawable.ic_back_btn);
        mMyActionBar.setBarBackGround(R.color.main_background);
        mMyActionBar.setBarTitleText(R.string.bar_about_us);
        mMyActionBar.setOnActionBarClickListener(this);

        // ******Btn
        mKefuQQBtn = (RelativeLayout) findViewById(R.id.rl_qq);
        mKefuQQBtn.setOnClickListener(this);
        mKefuPhoneBtn = (RelativeLayout) findViewById(R.id.rl_call);
        mKefuPhoneBtn.setOnClickListener(this);
        mKefuPhoneBtn.setVisibility(mBasicInfoModel.getData().hasPhone() ? View.VISIBLE : View.GONE);
        mUpdateAppBtn = (RelativeLayout) findViewById(R.id.rl_updateapp);
        mUpdateAppBtn.setOnClickListener(this);

        // ******Visition
        mVisitionTV = (TextView) findViewById(R.id.tv_visition);
        mVisitionTV.setText(R.string.visition);
        mVisitionTV.append(AppUtils.getAppVersionName());
        mNewVersionView = findViewById(R.id.view_have_newversion);
        if (((App) getApplication()).haveNewVersion) {
            mNewVersionView.setVisibility(View.VISIBLE);
        }
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
            case R.id.rl_qq:
                new MyDialog(this)
                        .setMessage(getString(R.string.kefu_qq) + "： " + mBasicInfoModel.getData().getQq())
                        .setRightText(getString(R.string.app_confirm))
                        .show();
                break;
            case R.id.rl_call:
                MyDialog.callKefu(this);
                break;
            case R.id.rl_updateapp:// 检测版本
                mAboutPresenterImp.getUpdateApp(StringUtil.getVersionJson());
                break;
        }
    }

    // --------------------MainViewInf method---------------------

    @Override
    public void sendVersionMessage(final Message msg) {
        switch (msg.what) {
            case HandlerConfig.WHAT_POST_SUCCESS:
                // 如果成功则继续获取其他信息(判断是否需要更新，再执行下面的操作)
                final VersionStatusModel.VersionStatusData data = (VersionStatusModel.VersionStatusData) msg.obj;
                ((App) getApplication()).haveNewVersion = (data.getVersionStatus() != 0);
                switch (data.getVersionStatus()) {
                    case 0:
                        new MyDialog(this)
                                .setRightText(getString(R.string.app_confirm))
                                .setMessage(getString(R.string.update_app_newest))
                                .show();
                        break;
                    case -1:
                    case 1:
                        final App app = (App) getApplication();
                        app.haveNewVersion = true;// 有新版本
                        if (mLoadingDialog != null) mLoadingDialog.dismiss();
                        MyDialog.createUpdateAppDialog(this, data.getVersionStatus() == -1)
                                .listener(new MyDialog.OnDialogClickListener() {
                                    @Override
                                    public void onLeftClicked() {
                                    }

                                    @Override
                                    public void onRightClicked() {
                                        MyUtil.downLoadApk(data.getUrl());
                                        new DownApkDialog(AboutActivity.this).show();
//                                        Uri uri = Uri.parse(data.getUrl());
//                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                                        startActivity(intent);
//                                        finish();
                                    }
                                }).show();
                        break;
                }
                break;
            default:
                if (mLoadingDialog != null) mLoadingDialog.dismiss();
                MyUtil.handMessage(this, msg, "MainActivity---Version>>>");
        }
    }

}
