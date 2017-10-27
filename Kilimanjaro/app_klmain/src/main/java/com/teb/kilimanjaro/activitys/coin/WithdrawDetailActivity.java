package com.teb.kilimanjaro.activitys.coin;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.activitys.BaseActivity;
import com.teb.kilimanjaro.adapters.WithdrawDetailAdapter;
import com.teb.kilimanjaro.configs.HandlerConfig;
import com.teb.kilimanjaro.models.entry.coin.WithdrawDetailModel;
import com.teb.kilimanjaro.mvp.presenter.WithdrawDetailPresenterImp;
import com.teb.kilimanjaro.mvp.view.WithdrawDetailViewInf;
import com.teb.kilimanjaro.utils.MyUtil;
import com.teb.kilimanjaro.utils.StringUtil;
import com.teb.kilimanjaro.views.MyActionBar;
import com.teb.kilimanjaro.views.dialogs.MyDialog;

import java.util.HashMap;
import java.util.List;


/**
 * Created by yangbin on 16/8/16.
 * 提现进度
 */
public class WithdrawDetailActivity extends BaseActivity implements WithdrawDetailViewInf {

    private WithdrawDetailPresenterImp mWithdrawDetailPresenterImp;
    private Dialog mLoadingDialog;
    private String mPayNo;
    private WithdrawDetailModel.WithdrawDetailData mData;

    private MyActionBar mMyActionBar;

    private TextView mPayNoTV;

    private ListView mDetailLV;
    private WithdrawDetailAdapter mAdapter;
    private List<WithdrawDetailModel.RecordsData> mList;

    private ImageView[] mStatusIVs;
    private TextView[] mStatusTVs;
    private static final int IV_IDS[] = {R.id.iv_status_2, R.id.iv_status_3};
    private static final int TV_IDS[] = {R.id.tv_status_2, R.id.tv_status_3};
    private static final int IMG_IDS[] = {R.drawable.ic_withdraw_ing, R.drawable.ic_withdraw_done};
    private static final int COLORS[] = {R.color.orange_text, R.color.green};

    private TextView mFailReasonTV;

    private TextView mCallKefuTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawpross);
        mPayNo = getIntent().getStringExtra("payNo");
        initViews();

        mWithdrawDetailPresenterImp = new WithdrawDetailPresenterImp(this);
        mLoadingDialog = MyDialog.createLoadingDialog(this, true);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("payNo", mPayNo);
        mWithdrawDetailPresenterImp.getWithdrawDetail(StringUtil.getJson(hashMap));
        mLoadingDialog.show();
    }

    private void initViews() {
        // ******MyActionBar
        mMyActionBar = (MyActionBar) findViewById(R.id.actionBar);
        mMyActionBar.setBarTitleText(R.string.withdetail_bar);
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

        mPayNoTV = (TextView) findViewById(R.id.tv_no);
        mPayNoTV.setText(getResources().getString(R.string.withdetail_no, mPayNo));

        // ****status
        mStatusIVs = new ImageView[IV_IDS.length];
        mStatusTVs = new TextView[TV_IDS.length];
        for (int i = 0; i < IV_IDS.length; i++) {
            mStatusIVs[i] = (ImageView) findViewById(IV_IDS[i]);
            mStatusTVs[i] = (TextView) findViewById(TV_IDS[i]);
        }

        // ******ListView
        mDetailLV = (ListView) findViewById(R.id.lv_detail);
        mAdapter = new WithdrawDetailAdapter();
        mDetailLV.setAdapter(mAdapter);
    }

    private void changeStatus(int index) {
        mStatusIVs[index].setImageResource(IMG_IDS[index]);
        mStatusTVs[index].setTextColor(getResources().getColor(COLORS[index]));
    }

    @Override
    public void sendDetailMessage(Message msg) {
        switch (msg.what) {
            case HandlerConfig.WHAT_POST_SUCCESS:
                mData = (WithdrawDetailModel.WithdrawDetailData) msg.obj;
                findViewById(R.id.ll_pross).setVisibility(View.VISIBLE);
                switch (mData.getStatus()) {
                    case -2:
                        changeStatus(0);
                        break;
                    case -3:
                    case -5:
                        findViewById(R.id.ll_failreason).setVisibility(View.VISIBLE);
                        mFailReasonTV = (TextView) findViewById(R.id.tv_failreason);
                        mFailReasonTV.setText(mData.getFailReason());
                        mCallKefuTV = (TextView) findViewById(R.id.tv_call_kefu);
                        mCallKefuTV.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MyDialog.callKefu(WithdrawDetailActivity.this);
                            }
                        });
                    case -4:
                    case -6:
                        changeStatus(0);
                        changeStatus(1);
                        break;
                }
                mList = mData.getRecords();
                mAdapter.setList(mList);
                break;
            default:
                MyUtil.handMessage(this, msg, "WithdrawDetailActivity---Detail>>>");
                finish();
        }
        if (mLoadingDialog != null) mLoadingDialog.dismiss();
    }
}
