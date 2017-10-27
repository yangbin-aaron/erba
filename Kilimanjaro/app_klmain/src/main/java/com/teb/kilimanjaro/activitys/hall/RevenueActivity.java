package com.teb.kilimanjaro.activitys.hall;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.teb.kilimanjaro.AppPrefs;
import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.activitys.BaseActivity;
import com.teb.kilimanjaro.adapters.RevenueByDaysAdapter;
import com.teb.kilimanjaro.adapters.RevenueByPeriodsAdapter;
import com.teb.kilimanjaro.configs.HandlerConfig;
import com.teb.kilimanjaro.models.entry.hall.GameListModel;
import com.teb.kilimanjaro.models.entry.hall.RevenueByDaysModel;
import com.teb.kilimanjaro.models.entry.hall.RevenueByPeriodsModel;
import com.teb.kilimanjaro.models.entry.hall.RevenueGeneralModel;
import com.teb.kilimanjaro.mvp.presenter.RevenuePresenterImp;
import com.teb.kilimanjaro.mvp.view.RevenueViewInf;
import com.teb.kilimanjaro.utils.MyUtil;
import com.teb.kilimanjaro.utils.StringUtil;
import com.teb.kilimanjaro.utils.ToastUtil;
import com.teb.kilimanjaro.views.MyActionBar;
import com.teb.kilimanjaro.views.dialogs.MyDialog;

import java.util.HashMap;
import java.util.List;

/**
 * Created by yangbin on 16/7/19.
 * 我的亏盈
 */
public class RevenueActivity extends BaseActivity implements View.OnClickListener, RevenueViewInf {

    private MyActionBar mMyActionBar;

    // 期数数量    胜率   今天    昨天   本周    上周   本月   上月    今年    去年
    private TextView mIssueCountTV, mWinTV, mTodayTV, mYesterdayTV, mThisWeekTV, mLastWeekTV, mThisMonthTV, mLastMonthTV, mThisYearTV, mLastYearTV;

    private TextView mByIdTV, mByDayTV;// 按期亏盈按钮，按日亏盈按钮
    private LinearLayout mByIdLL, mByDayLL;// 按期亏盈列表，按日亏盈列表
    private ListView mByDayLV;// 按日亏盈列表
    private PullToRefreshListView mByIdLV;// 按期亏盈列表
    private RevenueByPeriodsAdapter mPeriodsAdapter;
    private RevenueByDaysAdapter mDaysAdapter;
    private List<RevenueByPeriodsModel.RevenueByPeriodsData> mPeriodsDataList;
    private List<RevenueByDaysModel.RevenueByDaysData> mDaysDataList;

    private RevenuePresenterImp mRevenuePresenterImp;
    private Dialog mLoadingDialog;
    private int mRequestCount; // 访问网络的次数,用于关闭加载对话框
    private int mLotteryId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenue);
        mRevenuePresenterImp = new RevenuePresenterImp(this);
        mLoadingDialog = MyDialog.createLoadingDialog(this, true);
        initViews();
        requestData();
    }

    /**
     * 访问网络数据
     */
    private void requestData() {
        mLoadingDialog.show();
        mRequestCount = 3;
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("gameId", AppPrefs.getInstance().getSelectedGame().getId());
        mRevenuePresenterImp.getRevenueGeneral(StringUtil.getJson(hashMap));
        mRevenuePresenterImp.getRevenueByDays(StringUtil.getJson(hashMap));
        requestPeriodsData();
    }

    private void requestPeriodsData() {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("gameId", AppPrefs.getInstance().getSelectedGame().getId());
        hashMap.put("lotteryId", mLotteryId);
        hashMap.put("pageSize", 10);
        mRevenuePresenterImp.getRevenueByPeriods(StringUtil.getJson(hashMap));

    }

    private void initViews() {
        // ******MyActionBar
        mMyActionBar = (MyActionBar) findViewById(R.id.actionBar);
        GameListModel.GameListData data = AppPrefs.getInstance().getSelectedGame();
        mMyActionBar.setBarTitleText(data.getGameName() + getResources().getString(R.string.revenue_bar));
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

        // ******数据
        mIssueCountTV = (TextView) findViewById(R.id.tv_issue_count);
        mWinTV = (TextView) findViewById(R.id.tv_win);
        mTodayTV = (TextView) findViewById(R.id.tv_today);
        mYesterdayTV = (TextView) findViewById(R.id.tv_yesterday);
        mThisWeekTV = (TextView) findViewById(R.id.tv_this_week);
        mLastWeekTV = (TextView) findViewById(R.id.tv_last_week);
        mThisMonthTV = (TextView) findViewById(R.id.tv_this_month);
        mLastMonthTV = (TextView) findViewById(R.id.tv_last_month);
        mThisYearTV = (TextView) findViewById(R.id.tv_this_year);
        mLastYearTV = (TextView) findViewById(R.id.tv_last_year);

        // ******按期  按日亏盈
        mByIdTV = (TextView) findViewById(R.id.tv_by_id);
        mByIdTV.setOnClickListener(this);
        mByDayTV = (TextView) findViewById(R.id.tv_by_day);
        mByDayTV.setOnClickListener(this);
        mByIdLL = (LinearLayout) findViewById(R.id.ll_by_id);
        mByDayLL = (LinearLayout) findViewById(R.id.ll_by_day);
        changeListView(0);

        mByIdLV = (PullToRefreshListView) findViewById(R.id.lv_by_id);
        mByIdLV.setMode(PullToRefreshBase.Mode.BOTH);
        mPeriodsAdapter = new RevenueByPeriodsAdapter();
        mByIdLV.setAdapter(mPeriodsAdapter);
        mByIdLV.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//                if (mPeriodsDataList == null) mPeriodsDataList = new ArrayList<>();
                mPeriodsDataList.clear();
                mByIdLV.setMode(PullToRefreshBase.Mode.BOTH);
                mLotteryId = 0;
                requestPeriodsData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {// 上拉加载更多
                if (mPeriodsDataList != null && mPeriodsDataList.size() > 0) {
                    mLotteryId = mPeriodsDataList.get(mPeriodsDataList.size() - 1).getLotteryId();
                    requestPeriodsData();
                }
            }
        });

        mByDayLV = (ListView) findViewById(R.id.lv_by_day);
        mDaysAdapter = new RevenueByDaysAdapter();
        mByDayLV.setAdapter(mDaysAdapter);

        TextView tv_getgamecoin = (TextView) findViewById(R.id.tv_getgamecoin);
        if (AppPrefs.getInstance().getSelectedGame().isSimulate()){
            tv_getgamecoin.setText(R.string.lottery_d_getscore);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_by_id:
                changeListView(0);
                break;
            case R.id.tv_by_day:
                changeListView(1);
                break;
        }
    }

    /**
     * 选择的按钮
     *
     * @param selector
     */
    private void changeListView(int selector) {
        switch (selector) {
            case 0:
                mByIdTV.setTextColor(getResources().getColor(R.color.white));
                mByIdTV.setBackgroundResource(R.drawable.radio_red_left_bg);
                mByDayTV.setTextColor(getResources().getColor(R.color.main_background));
                mByDayTV.setBackgroundResource(R.color.transparent);
                mByIdLL.setVisibility(View.VISIBLE);
                mByDayLL.setVisibility(View.GONE);
                break;
            case 1:
                mByDayTV.setTextColor(getResources().getColor(R.color.white));
                mByDayTV.setBackgroundResource(R.drawable.radio_red_right_bg);
                mByIdTV.setTextColor(getResources().getColor(R.color.main_background));
                mByIdTV.setBackgroundResource(R.color.transparent);
                mByDayLL.setVisibility(View.VISIBLE);
                mByIdLL.setVisibility(View.GONE);
                break;
        }
    }

    private void setGeneralData(RevenueGeneralModel.RevenueGeneralData generalData) {
        mIssueCountTV.setText(generalData.getPeriods() + "");
        mWinTV.setText(generalData.getVictoryRate());

        long revenue = generalData.getTodayRevenue();
        mTodayTV.setText(revenue + "");
        mTodayTV.setTextColor(getResources().getColor(revenue == 0 ? R.color.gray_xx : (revenue > 0 ? R.color.red_text : R.color.green)));

        revenue = generalData.getYesterdayRevenue();
        mYesterdayTV.setText(revenue + "");
        mYesterdayTV.setTextColor(getResources().getColor(revenue == 0 ? R.color.gray_xx : (revenue > 0 ? R.color.red_text : R.color.green)));

        revenue = generalData.getWeekRevenue();
        mThisWeekTV.setText(revenue + "");
        mThisWeekTV.setTextColor(getResources().getColor(revenue == 0 ? R.color.gray_xx : (revenue > 0 ? R.color.red_text : R.color.green)));

        revenue = generalData.getLastWeekRevenue();
        mLastWeekTV.setText(revenue + "");
        mLastWeekTV.setTextColor(getResources().getColor(revenue == 0 ? R.color.gray_xx : (revenue > 0 ? R.color.red_text : R.color.green)));

        revenue = generalData.getMonthRevenue();
        mThisMonthTV.setText(revenue + "");
        mThisMonthTV.setTextColor(getResources().getColor(revenue == 0 ? R.color.gray_xx : (revenue > 0 ? R.color.red_text : R.color.green)));

        revenue = generalData.getLastMonthRevenue();
        mLastMonthTV.setText(revenue + "");
        mLastMonthTV.setTextColor(getResources().getColor(revenue == 0 ? R.color.gray_xx : (revenue > 0 ? R.color.red_text : R.color.green)));

        revenue = generalData.getYearRevenue();
        mThisYearTV.setText(revenue + "");
        mThisYearTV.setTextColor(getResources().getColor(revenue == 0 ? R.color.gray_xx : (revenue > 0 ? R.color.red_text : R.color.green)));

        revenue = generalData.getLastYearRevenue();
        mLastYearTV.setText(revenue + "");
        mLastYearTV.setTextColor(getResources().getColor(revenue == 0 ? R.color.gray_xx : (revenue > 0 ? R.color.red_text : R.color.green)));
    }

    // --------------------RevenueViewInf method---------------------

    @Override
    public void sendGeneralMessage(Message msg) {
        mRequestCount--;
        if (mRequestCount == 0) if (mLoadingDialog != null) mLoadingDialog.dismiss();
        switch (msg.what) {
            case HandlerConfig.WHAT_POST_SUCCESS:
                setGeneralData((RevenueGeneralModel.RevenueGeneralData) msg.obj);
                break;
            default:
                MyUtil.handMessage(this, msg, "RevenueActivity---General>>>");
                break;
        }
    }

    @Override
    public void sendDaysMessage(Message msg) {
        mRequestCount--;
        if (mRequestCount == 0) if (mLoadingDialog != null) mLoadingDialog.dismiss();
        switch (msg.what) {
            case HandlerConfig.WHAT_POST_SUCCESS:
                mDaysDataList = (List<RevenueByDaysModel.RevenueByDaysData>) msg.obj;
                mDaysAdapter.setList(mDaysDataList);
                break;
            default:
                MyUtil.handMessage(this, msg, "RevenueActivity---ByDays>>>");
                break;
        }
    }

    @Override
    public void sendPeriodsMessage(Message msg) {
        mByIdLV.onRefreshComplete();
        mRequestCount--;
        if (mRequestCount == 0) if (mLoadingDialog != null) mLoadingDialog.dismiss();
        switch (msg.what) {
            case HandlerConfig.WHAT_POST_SUCCESS:
                List<RevenueByPeriodsModel.RevenueByPeriodsData> tempList = (List<RevenueByPeriodsModel.RevenueByPeriodsData>) msg.obj;
                if (tempList.size() > 0) {
                    if (mPeriodsDataList == null || mPeriodsDataList.size() == 0) {
                        mPeriodsDataList = tempList;
                    } else {
                        mPeriodsDataList.addAll(mPeriodsDataList.size(), tempList);
                    }
                    mLotteryId = mPeriodsDataList.get(mPeriodsDataList.size() - 1).getLotteryId();
                } else {
                    mByIdLV.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    ToastUtil.showToast(R.string.lottery_default_no_more_data);
                }
                mPeriodsAdapter.setList(mPeriodsDataList);
                break;
            default:
                MyUtil.handMessage(this, msg, "RevenueActivity---ByPeriods>>>");
                break;
        }
    }
}
