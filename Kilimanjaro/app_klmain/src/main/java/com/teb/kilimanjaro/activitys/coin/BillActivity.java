package com.teb.kilimanjaro.activitys.coin;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.activitys.BaseActivity;
import com.teb.kilimanjaro.adapters.RechargeListViewAdapter;
import com.teb.kilimanjaro.adapters.WithdrawListViewAdapter;
import com.teb.kilimanjaro.configs.HandlerConfig;
import com.teb.kilimanjaro.models.entry.coin.RechargeListModel;
import com.teb.kilimanjaro.models.entry.coin.WithdrawListModel;
import com.teb.kilimanjaro.mvp.presenter.BillPresenterImp;
import com.teb.kilimanjaro.mvp.view.BillViewInf;
import com.teb.kilimanjaro.utils.MyUtil;
import com.teb.kilimanjaro.utils.StringUtil;
import com.teb.kilimanjaro.views.MyActionBar;
import com.teb.kilimanjaro.views.dialogs.MyDialog;

import java.util.HashMap;
import java.util.List;

/**
 * Created by yangbin on 16/8/12.
 * 充值账单
 */
public class BillActivity extends BaseActivity implements BillViewInf, View.OnClickListener {

    private BillPresenterImp mBillPresenterImp;
    private Dialog mLoadingDialog;

    private MyActionBar mMyActionBar;

    private TextView mRechargeTV, mWithdrawTV;
    private LinearLayout mRechargeLL, mWithdrawLL;
    private PullToRefreshListView mRechargeLV, mWithdrawLV;
    private RechargeListViewAdapter mRechargeAdapter;
    private WithdrawListViewAdapter mWithdrawAdapter;
    private List<RechargeListModel.RechargeData> mRechargeList;
    private List<WithdrawListModel.WithdrawData> mWithdrawList;

    private int mPageNumR = 1;
    private int mPageNumW = 1;
    private int mPageSize = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        mBillPresenterImp = new BillPresenterImp(this);
        mLoadingDialog = MyDialog.createLoadingDialog(this, true);
        initViews();
    }

    private void getRechargeData() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("pageNum", mPageNumR);
        hashMap.put("pageSize", mPageSize);
        mBillPresenterImp.getRechargeList(StringUtil.getJson(hashMap));
        mLoadingDialog.show();
    }

    private void getWithdrawData() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("pageNum", mPageNumW);
        hashMap.put("pageSize", mPageSize);
        mBillPresenterImp.getWithdrawList(StringUtil.getJson(hashMap));
        mLoadingDialog.show();
    }

    private void initViews() {
        // ******MyActionBar
        mMyActionBar = (MyActionBar) findViewById(R.id.actionBar);
        mMyActionBar.setBarTitleText(R.string.bill_bar);
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

        // ******TV
        mRechargeTV = (TextView) findViewById(R.id.tv_recharge);
        mWithdrawTV = (TextView) findViewById(R.id.tv_withdraw);
        mRechargeTV.setOnClickListener(this);
        mWithdrawTV.setOnClickListener(this);

        // ******LL
        mRechargeLL = (LinearLayout) findViewById(R.id.ll_recharge);
        mWithdrawLL = (LinearLayout) findViewById(R.id.ll_withdraw);
        changeListView(0);

        // ******ListView
        mRechargeLV = (PullToRefreshListView) findViewById(R.id.lv_recharge);
        mRechargeLV.setMode(PullToRefreshBase.Mode.BOTH);
        mRechargeAdapter = new RechargeListViewAdapter();
        mRechargeLV.setAdapter(mRechargeAdapter);
        mRechargeLV.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPageNumR = 1;
                getRechargeData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getRechargeData();
            }
        });

        mWithdrawLV = (PullToRefreshListView) findViewById(R.id.lv_withdraw);
        mWithdrawLV.setMode(PullToRefreshBase.Mode.BOTH);
        mWithdrawAdapter = new WithdrawListViewAdapter();
        mWithdrawLV.setAdapter(mWithdrawAdapter);
        mWithdrawLV.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPageNumW = 1;
                getWithdrawData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getWithdrawData();
            }
        });
        mWithdrawLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gotoWithdrawDetailActivity(position - 1);
            }
        });
    }

    private void gotoWithdrawDetailActivity(int position) {
        Intent intent = new Intent(BillActivity.this, WithdrawDetailActivity.class);
        intent.putExtra("payNo", mWithdrawList.get(position).getPayNo());
        MyUtil.startActivity(BillActivity.this, intent);
    }

    /**
     * 选择的按钮
     *
     * @param selector
     */
    private void changeListView(int selector) {
        switch (selector) {
            case 0:
                mRechargeTV.setTextColor(getResources().getColor(R.color.white));
                mRechargeTV.setBackgroundResource(R.drawable.radio_red_left_bg);
                mWithdrawTV.setTextColor(getResources().getColor(R.color.main_background));
                mWithdrawTV.setBackgroundResource(R.color.transparent);
                mRechargeLL.setVisibility(View.VISIBLE);
                mWithdrawLL.setVisibility(View.GONE);
                if (mRechargeList == null) {
                    getRechargeData();
                }
                break;
            case 1:
                mWithdrawTV.setTextColor(getResources().getColor(R.color.white));
                mWithdrawTV.setBackgroundResource(R.drawable.radio_red_right_bg);
                mRechargeTV.setTextColor(getResources().getColor(R.color.main_background));
                mRechargeTV.setBackgroundResource(R.color.transparent);
                mWithdrawLL.setVisibility(View.VISIBLE);
                mRechargeLL.setVisibility(View.GONE);
                if (mWithdrawList == null) {
                    getWithdrawData();
                }
                break;
        }
    }

    //  // --------------------BillViewInf method---------------------

    @Override
    public void sendRechargeListMessage(Message msg) {
        switch (msg.what) {
            case HandlerConfig.WHAT_POST_SUCCESS:
                List<RechargeListModel.RechargeData> tempList = (List<RechargeListModel.RechargeData>) msg.obj;
                if (mPageNumR == 1) {
                    mRechargeList = tempList;
                } else {
                    mRechargeList.addAll(tempList);
                }
                if (tempList.size() >= mPageSize) {
                    mPageNumR++;
                }
                mRechargeAdapter.setList(mRechargeList);
                break;
            default:
                MyUtil.handMessage(this, msg, "BillActivity---RechargeList>>>");
                break;
        }
        mRechargeLV.onRefreshComplete();
        if (mLoadingDialog != null) mLoadingDialog.dismiss();
    }

    @Override
    public void sendWithdrawListMessage(Message msg) {
        switch (msg.what) {
            case HandlerConfig.WHAT_POST_SUCCESS:
                List<WithdrawListModel.WithdrawData> tempList = (List<WithdrawListModel.WithdrawData>) msg.obj;
                if (mPageNumW == 1) {
                    mWithdrawList = tempList;
                } else {
                    mWithdrawList.addAll(tempList);
                }
                if (tempList.size() >= mPageSize) {
                    mPageNumW++;
                }
                mWithdrawAdapter.setList(mWithdrawList);
                break;
            default:
                MyUtil.handMessage(this, msg, "BillActivity---WithdrawList>>>");
        }
        mWithdrawLV.onRefreshComplete();
        if (mLoadingDialog != null) mLoadingDialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_recharge:
                changeListView(0);
                break;
            case R.id.tv_withdraw:
                changeListView(1);
                break;
        }
    }
}
