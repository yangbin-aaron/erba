package com.teb.kilimanjaro.activitys.coin;

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
import com.teb.kilimanjaro.configs.HandlerConfig;
import com.teb.kilimanjaro.models.entry.coin.PrepareInfoModel;
import com.teb.kilimanjaro.models.entry.mine.LoginModel;
import com.teb.kilimanjaro.mvp.presenter.TieOnCardPresenterImp;
import com.teb.kilimanjaro.mvp.view.TieOnCardViewInf;
import com.teb.kilimanjaro.utils.IdCardUtil;
import com.teb.kilimanjaro.utils.MyUtil;
import com.teb.kilimanjaro.utils.StringUtil;
import com.teb.kilimanjaro.utils.ToastUtil;
import com.teb.kilimanjaro.views.MyActionBar;
import com.teb.kilimanjaro.views.dialogs.MyDialog;

import java.util.HashMap;

/**
 * Created by yangbin on 16/8/16.
 * 绑卡
 */
public class TieOnCardActivity extends BaseActivity implements View.OnClickListener, TieOnCardViewInf {

    private TieOnCardPresenterImp mTieOnCardPresenterImp;
    private Dialog mLoadingDialog;

    private MyActionBar mMyActionBar;

    private TextView mQQTV, mPhoneTV;
    private EditText mNameET, mIdNoET, mCardNoET;// 姓名，身份证号，银行卡号

    private TextView mTieOnCardTV;// 绑卡

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tieoncard);
        initViews();

        mTieOnCardPresenterImp = new TieOnCardPresenterImp(this);
        mLoadingDialog = MyDialog.createLoadingDialog(this, true);
    }

    private void initViews() {
        // ******MyActionBar
        mMyActionBar = (MyActionBar) findViewById(R.id.actionBar);
        mMyActionBar.setBarTitleText(R.string.tieoncard_bar);
        mMyActionBar.setBarBackGround(R.color.main_background);
        mMyActionBar.setBarLeftImage(R.drawable.ic_back_btn);
        mMyActionBar.setOnActionBarClickListener(new MyActionBar.OnActionBarClickListener() {
            @Override
            public void onActionBarLeftClicked() {
                finish();
            }

            @Override
            public void onActionBarRightClicked() {
            }
        });

        // ******QQ和手机号码
        mQQTV = (TextView) findViewById(R.id.tv_qq);
        mPhoneTV = (TextView) findViewById(R.id.tv_phone);
        LoginModel model = LoginModel.getUserInfo();
        if (model != null) {
            String email = model.getData().getEmail();
            if (!TextUtils.isEmpty(email)) {
                String[] emails = email.split("@");
                mQQTV.setText(emails[0]);
            }
            mPhoneTV.setText(model.getData().getPhone());
        }

        // ******编辑框
        mNameET = (EditText) findViewById(R.id.et_name);
        mIdNoET = (EditText) findViewById(R.id.et_idno);
        mCardNoET = (EditText) findViewById(R.id.et_card);

        // ******按钮
        mTieOnCardTV = (TextView) findViewById(R.id.tv_tie_on_card);
        mTieOnCardTV.setOnClickListener(this);
    }

    /**
     * 绑定银行卡
     */
    private void tieOnCard() {
        String name = mNameET.getText().toString().trim();
        String idNo = mIdNoET.getText().toString().trim();
        String cardNo = mCardNoET.getText().toString().trim();
        String phone = mPhoneTV.getText().toString().trim();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(idNo) || TextUtils.isEmpty(cardNo)) {
            ToastUtil.showToast(R.string.input_cannot_null);
            return;
        }

        IdCardUtil idCardUtil = new IdCardUtil();
        if (!idCardUtil.verify(idNo)) {
            ToastUtil.showToast(idCardUtil.getCodeError());
            return;
        }

        if (cardNo.length() < 16 || cardNo.length() > 19) {
            ToastUtil.showToast(R.string.withdraw_cardno_error);
            return;
        }

        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("realPhone", phone);
        hashMap.put("realName", name);
        hashMap.put("idCardNum", idNo);
        hashMap.put("bankNum", cardNo);
        mTieOnCardPresenterImp.tieOnCard(StringUtil.getJson(hashMap));
        mLoadingDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_tie_on_card:
                tieOnCard();
                break;
        }
    }

    @Override
    public void sendTieOnCardMessage(Message msg) {
        switch (msg.what) {
            case HandlerConfig.WHAT_POST_SUCCESS:
                PrepareInfoModel.PrepareInfoData infoData = (PrepareInfoModel.PrepareInfoData) msg.obj;
                // 绑定完成
                ToastUtil.showToast(R.string.tieoncard_success);
                finish();
                Intent intent = new Intent(this, WithdrawActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", infoData);
                intent.putExtras(bundle);
                MyUtil.startActivity(this, intent);
                break;
            default:
                MyUtil.handMessage(this, msg, "TieOnCardActivity---TieOnCard>>>");
        }
        if (mLoadingDialog != null) mLoadingDialog.dismiss();
    }
}
