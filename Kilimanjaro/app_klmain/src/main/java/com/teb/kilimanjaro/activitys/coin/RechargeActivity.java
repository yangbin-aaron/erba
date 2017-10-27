package com.teb.kilimanjaro.activitys.coin;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.teb.kilimanjaro.App;
import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.activitys.BaseActivity;
import com.teb.kilimanjaro.configs.BroadcastConfig;
import com.teb.kilimanjaro.configs.HandlerConfig;
import com.teb.kilimanjaro.mvp.presenter.RechargePresenterImp;
import com.teb.kilimanjaro.mvp.view.RechargeViewInf;
import com.teb.kilimanjaro.utils.AppUtils;
import com.teb.kilimanjaro.utils.MyUtil;
import com.teb.kilimanjaro.utils.StringUtil;
import com.teb.kilimanjaro.utils.ToastUtil;
import com.teb.kilimanjaro.views.MyActionBar;
import com.teb.kilimanjaro.views.dialogs.MyDialog;
import com.wo.main.WP_SDK;
import com.wo.plugin.WP_Event;

import java.util.HashMap;

/**
 * Created by yangbin on 16/8/9.
 * 充值界面
 */
public class RechargeActivity extends BaseActivity implements RechargeViewInf {

    private RechargePresenterImp mRechargePresenterImp;
    private Dialog mLoadingDialog;

    private MyActionBar mMyActionBar;

    private TextView[] mRmbTVs;
    private static final int[] tvIds = {R.id.tv_rmb0, R.id.tv_rmb1, R.id.tv_rmb2, R.id.tv_rmb3};
    private static final int[] rmbs = {1000, 2000, 5000, 10000};
    private EditText mOtherRmbET;// 输入其他人民币金额
    private final static long MIN_RECHARGE_COIN = 10;// 充值最小金额(元)
    private TextView mDbxeTV;// 单笔限额
    private long mDbxeCoin = 10000;// 单笔限额金额

    private RelativeLayout[] mPayRls;
    private ImageView[] mSelectIVs;
    private static final int[] rlIds = {R.id.rl_pay_weixin, R.id.rl_pay_yinlian, R.id.rl_pay_shenzhou, R.id.rl_pay_qq};
    private static final int[] ivIds = {R.id.iv_selector_winxin, R.id.iv_selector_yl, R.id.iv_selector_sz, R.id.iv_selector_qq};

    private TextView mOnPayTV;
    private long mFeeLong = 0;
    private int mType = 0;
    private int mTypePostion = 0;
    private static final int PAY_TYPE_WX = 0;
    private static final int PAY_TYPE_YL = 1;
    private static final int PAY_TYPE_SZ = 2;
    private static final int PAY_TYPE_QQ = 3;
    private static final int[] types = {PAY_TYPE_WX, PAY_TYPE_YL, PAY_TYPE_SZ, PAY_TYPE_QQ};// 0微信   1银联   2神州   3QQ

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        initViews();

        mRechargePresenterImp = new RechargePresenterImp(this);
        mLoadingDialog = MyDialog.createLoadingDialog(this, true);
    }

    private void initViews() {
        // ******MyActionBar
        mMyActionBar = (MyActionBar) findViewById(R.id.actionBar);
        mMyActionBar.setBarBackGround(R.color.main_background);
        mMyActionBar.setBarLeftImage(R.drawable.ic_back_btn);
        mMyActionBar.setBarTitleText(R.string.recharge_bar);
        mMyActionBar.setOnActionBarClickListener(new MyActionBar.OnActionBarClickListener() {
            @Override
            public void onActionBarLeftClicked() {
                finish();
            }

            @Override
            public void onActionBarRightClicked() {

            }
        });

        // ******充值金额按钮
        mRmbTVs = new TextView[tvIds.length];
        for (int i = 0; i < tvIds.length; i++) {
            final int finalI = i;
            mRmbTVs[i] = (TextView) findViewById(tvIds[i]);
            mRmbTVs[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateRmb(finalI);
                }
            });
        }

        mOtherRmbET = (EditText) findViewById(R.id.et_rmb);
        mOtherRmbET.setText(MIN_RECHARGE_COIN + "");
        mOtherRmbET.setSelection(mOtherRmbET.getText().toString().trim().length());

        mDbxeTV = (TextView) findViewById(R.id.tv_dbxe);
        updatePayRL(0);

        // ******支付方式
        mPayRls = new RelativeLayout[rlIds.length];
        mSelectIVs = new ImageView[ivIds.length];
        for (int i = 0; i < rlIds.length; i++) {
            final int finalI = i;
            mSelectIVs[i] = (ImageView) findViewById(ivIds[i]);
            mPayRls[i] = (RelativeLayout) findViewById(rlIds[i]);
            mPayRls[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (finalI == 0) {
//                        MobapWechatPayUtil.mobaoWechatPay(new Handler(){
//                            @Override
//                            public void handleMessage(Message msg) {
//                                super.handleMessage(msg);
//                                String result = (String) msg.obj;
//                                switch (msg.what) {
//                                    case 200:
//                                        // 请求成功  
//                                        Log.e("mb", "返回参数===" + result);
//                                        break;
//                                    default:
//                                        // 请求失败  
//                                        Log.e("mb", "请求失败!");
//                                        break;
//                                }
//                            }
//                        });
                    }
                    updatePayRL(finalI);
                }
            });
        }

        mOnPayTV = (TextView) findViewById(R.id.tv_onpay);
        mOnPayTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkString();
            }
        });
    }

    /**
     * 刷新金额
     *
     * @param position
     */
    private void updateRmb(int position) {
        mOtherRmbET.setText(rmbs[position] + "");
        mOtherRmbET.setSelection(mOtherRmbET.getText().toString().trim().length());
    }

    private void checkString() {
        mType = types[mTypePostion];
        if (mType == PAY_TYPE_WX) {// 微信支付
            if (!AppUtils.isWeixinAvilible(this)) {
                ToastUtil.showToast(R.string.recharge_hasnot_weixin);
                return;
            }
        } else if (mType == PAY_TYPE_QQ) {// QQ支付
            if (!AppUtils.isQQClientAvailable(this)) {
                ToastUtil.showToast(R.string.recharge_hasnot_qq);
                return;
            }
        }

        String fee = mOtherRmbET.getText().toString().trim();// 最小单位为分
        if (TextUtils.isEmpty(fee)) {
            ToastUtil.showToast(R.string.recharge_rmb_cannot_null);
            return;
        }
        mFeeLong = Long.parseLong(fee) * 100;// 从 将元化为分
        if (mFeeLong < MIN_RECHARGE_COIN * 100) {
            ToastUtil.showToast(getResources().getString(R.string.recharge_min, MIN_RECHARGE_COIN));
            return;
        }

        if (mFeeLong > mDbxeCoin * 100) {
            ToastUtil.showToast(getResources().getString(R.string.recharge_dbxe, mDbxeCoin));
            return;
        }

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("payType", mType);
        hashMap.put("coinCash", mFeeLong);
        mRechargePresenterImp.getOrderNo(StringUtil.getJson(hashMap));
        mLoadingDialog.show();
    }

    /**
     * 支付
     *
     * @param payNo
     */
    private void onPay(String payNo) {
        try {
            String fee = mFeeLong + "";
            if (App.IS_RECHARGE_001) {
                fee = "1";
            }

            WP_SDK.on_Recharge(
                    RechargeActivity.this,// Activity实例
                    fee, // 计费金额
                    "游戏币", // 道具名称
                    (int) (mFeeLong / 100) + "游戏币", // 道具简介
                    payNo, // 订单号(将支付方式传给服务器)
                    mType, // 充值类型
                    new WP_Event() {
                        @Override
                        public void on_Result(int code, String value) {
                            // TODO Auto-generated method stub
                            if (code == 0) {// 充值成功
                                ToastUtil.showToast(R.string.recharge_success);
                                sendBroadcast(new Intent(BroadcastConfig.ACTION_UPDATE_NEW_GAMECOIN));
                                finish();
                            } else {// 充值失败
                                ToastUtil.showToast(R.string.recharge_fail);
                                Log.e("debug", code + ",value=" + value);
                            }
                        }
                    });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updatePayRL(int position) {
        if (types[position] == PAY_TYPE_QQ) {
            mDbxeCoin = 500;
        } else {
            mDbxeCoin = 10000;
        }
        mDbxeTV.setText(getResources().getString(R.string.recharge_dbxe, mDbxeCoin));
        if (position == mTypePostion) {
            return;
        }

        mPayRls[mTypePostion].setBackgroundResource(R.color.white);
        mSelectIVs[mTypePostion].setImageResource(R.drawable.ic_recharge_cbox_unselect);

        mTypePostion = position;
        mPayRls[mTypePostion].setBackgroundResource(R.drawable.radio_green_white_bg);
        mSelectIVs[mTypePostion].setImageResource(R.drawable.ic_recharge_cbox_select);
    }

    // --------------------RechargeViewInf method---------------------

    @Override
    public void sendOrderIdMessage(Message msg) {
        switch (msg.what) {
            case HandlerConfig.WHAT_POST_SUCCESS:
                String payNo = (String) msg.obj;
                Log.e("pay", payNo);
                onPay(payNo);
                break;
            default:
                MyUtil.handMessage(this, msg, "RechargeActivity---payNo>>>");
                break;
        }
        if (mLoadingDialog != null) mLoadingDialog.dismiss();
    }
}
