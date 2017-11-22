package com.qp.app_new.activitys.home;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.qp.app_new.R;
import com.qp.app_new.activitys.BaseActivity;
import com.qp.app_new.adapters.RevenueByDaysAdapter;
import com.qp.app_new.adapters.RevenueByPeriodsAdapter;
import com.qp.app_new.configs.NetStatusConfig;
import com.qp.app_new.dialogs.DialogHelp;
import com.qp.app_new.httpnetworks.NetWorkManager;
import com.qp.app_new.interfaces.NetListener;
import com.qp.app_new.interfaces.PopupWindowItemListener;
import com.qp.app_new.utils.LogUtil;
import com.qp.app_new.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Aaron on 2017/11/20.
 */

public class RevenueActivity extends BaseActivity {
    @Override
    public int getContentView() {
        return R.layout.activity_revenue;
    }

    private JSONObject mGameJSONObject;
    private PopupWindow mPopupWindow;
    private TextView mShowTypeTV;// 列表模式

    // 期数数量    胜率   今天    昨天   本周    上周   本月   上月    今年    去年
    private TextView mIssueCountTV, mWinTV, mTodayTV, mYesterdayTV, mThisWeekTV, mLastWeekTV, mThisMonthTV, mLastMonthTV, mThisYearTV, mLastYearTV;

    private LinearLayout mByIdLL, mByDayLL;// 按期亏盈列表，按日亏盈列表
    private ListView mByDayLV;// 按日亏盈列表
    private PullToRefreshListView mByIdLV;// 按期亏盈列表
    private RevenueByPeriodsAdapter mPeriodsAdapter;
    private RevenueByDaysAdapter mDaysAdapter;
    private JSONArray mPeriodsDataList;

    private int mLotteryId = 0;

    @Override
    public void getIntentData() {
        super.getIntentData();
        try {
            mGameJSONObject = new JSONObject(getIntent().getStringExtra("gameJsonObject"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initView() {
        initActionBar();
        setLeftIV(R.drawable.ic_back_btn);
        setTitle(getString(R.string.revenue_bar_, mGameJSONObject.optString("gameName")));
        setRightIV(R.drawable.ic_menu);

        mShowTypeTV = (TextView) findViewById(R.id.type_tv);
        final String[] popList = {getString(R.string.revenue_byperoid), getString(R.string.revenue_bydays)};
        mShowTypeTV.setText(popList[0]);
        mPopupWindow = DialogHelp.createPopupWindow(this, popList, new PopupWindowItemListener() {
            @Override
            public void onPopItemClick(View v, int position) {
                mShowTypeTV.setText(popList[position]);
                changeListView(position);
                mPopupWindow.dismiss();
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

        getRevenueGeneral();

        mByIdLL = (LinearLayout) findViewById(R.id.ll_by_id);
        mByDayLL = (LinearLayout) findViewById(R.id.ll_by_day);

        mByIdLV = (PullToRefreshListView) findViewById(R.id.lv_by_id);
        mByIdLV.setMode(PullToRefreshBase.Mode.BOTH);
        mPeriodsAdapter = new RevenueByPeriodsAdapter();
        mByIdLV.setAdapter(mPeriodsAdapter);
        mPeriodsDataList = new JSONArray();
        mByIdLV.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPeriodsDataList = null;
                mPeriodsDataList = new JSONArray();
                mByIdLV.setMode(PullToRefreshBase.Mode.BOTH);
                mLotteryId = 0;
                getRevenueByPeriods();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {// 上拉加载更多
                if (mPeriodsDataList != null && mPeriodsDataList.length() > 0) {
                    JSONObject jsonObject = mPeriodsDataList.optJSONObject(mPeriodsDataList.length() - 1);
                    mLotteryId = jsonObject.optInt("lotteryId");
                    getRevenueByDays();
                }
            }
        });

        mByDayLV = (ListView) findViewById(R.id.lv_by_day);
        mDaysAdapter = new RevenueByDaysAdapter();
        mByDayLV.setAdapter(mDaysAdapter);

        changeListView(0);

        getRevenueByPeriods();
        getRevenueByDays();
    }

    /**
     * 选择的按钮
     *
     * @param selector
     */
    private void changeListView(int selector) {
        switch (selector) {
            case 0:
                mByIdLL.setVisibility(View.VISIBLE);
                mByDayLL.setVisibility(View.GONE);
                break;
            case 1:
                mByDayLL.setVisibility(View.VISIBLE);
                mByIdLL.setVisibility(View.GONE);
                break;
        }
    }

    private void setGeneralData(JSONObject generalData) {
        LogUtil.e("setGeneralData", "generalData = " + generalData.toString());
        mIssueCountTV.setText(generalData.optString("periods"));
        mWinTV.setText(generalData.optString("victoryRate"));

        long revenue = generalData.optLong("todayRevenue");
        mTodayTV.setText(revenue + "");
        mTodayTV.setTextColor(getResources().getColor(revenue == 0 ? R.color.gray_xx : (revenue > 0 ? R.color.red_text : R.color.green)));

        revenue = generalData.optLong("yesterdayRevenue");
        mYesterdayTV.setText(revenue + "");
        mYesterdayTV.setTextColor(getResources().getColor(revenue == 0 ? R.color.gray_xx : (revenue > 0 ? R.color.red_text : R.color.green)));

        revenue = generalData.optLong("weekRevenue");
        mThisWeekTV.setText(revenue + "");
        mThisWeekTV.setTextColor(getResources().getColor(revenue == 0 ? R.color.gray_xx : (revenue > 0 ? R.color.red_text : R.color.green)));

        revenue = generalData.optLong("lastWeekRevenue");
        mLastWeekTV.setText(revenue + "");
        mLastWeekTV.setTextColor(getResources().getColor(revenue == 0 ? R.color.gray_xx : (revenue > 0 ? R.color.red_text : R.color.green)));

        revenue = generalData.optLong("monthRevenue");
        mThisMonthTV.setText(revenue + "");
        mThisMonthTV.setTextColor(getResources().getColor(revenue == 0 ? R.color.gray_xx : (revenue > 0 ? R.color.red_text : R.color.green)));

        revenue = generalData.optLong("lastMonthRevenue");
        mLastMonthTV.setText(revenue + "");
        mLastMonthTV.setTextColor(getResources().getColor(revenue == 0 ? R.color.gray_xx : (revenue > 0 ? R.color.red_text : R.color.green)));

        revenue = generalData.optLong("yearRevenue");
        mThisYearTV.setText(revenue + "");
        mThisYearTV.setTextColor(getResources().getColor(revenue == 0 ? R.color.gray_xx : (revenue > 0 ? R.color.red_text : R.color.green)));

        revenue = generalData.optLong("lastYearRevenue");
        mLastYearTV.setText(revenue + "");
        mLastYearTV.setTextColor(getResources().getColor(revenue == 0 ? R.color.gray_xx : (revenue > 0 ? R.color.red_text : R.color.green)));
    }

    /**
     * 获取基本信息
     */
    private void getRevenueGeneral() {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("gameId", mGameJSONObject.optString("id"));
        NetWorkManager.getInstance().getRevenueGeneral(StringUtil.getJson(hashMap), new NetListener() {
            @Override
            public void onSuccessResponse(String msg, JSONObject jsonObject) {
                super.onSuccessResponse(msg, jsonObject);
                setGeneralData(jsonObject);
            }
        });
    }

    private void getRevenueByPeriods() {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("gameId", mGameJSONObject.optString("id"));
        hashMap.put("lotteryId", mLotteryId);
        hashMap.put("pageSize", 10);
        NetWorkManager.getInstance().getRevenueByPeriods(StringUtil.getJson(hashMap), mLoadingDialog, new NetListener() {
            @Override
            public void onSuccessResponse(String msg, JSONArray jsonArray) {
                super.onSuccessResponse(msg, jsonArray);
                mByIdLV.onRefreshComplete();
                for (int i = 0; i < jsonArray.length(); i++) {
                    mPeriodsDataList.put(jsonArray.optJSONObject(i));
                }

                JSONObject jsonObject = mPeriodsDataList.optJSONObject(mPeriodsDataList.length() - 1);
                mLotteryId = jsonObject.optInt("lotteryId");
                mPeriodsAdapter.setJSONArray(mPeriodsDataList);
            }

            @Override
            public void onErrorResponse(int errorWhat, String message) {
                super.onErrorResponse(errorWhat, message);
                mByIdLV.onRefreshComplete();
                if (errorWhat == NetStatusConfig.STATUS_HAVE_NO_DATA) {
                    mByIdLV.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
            }
        });
    }

    private void getRevenueByDays() {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("gameId", mGameJSONObject.optString("id"));
        NetWorkManager.getInstance().getRevenueByDays(StringUtil.getJson(hashMap), new NetListener() {
            @Override
            public void onSuccessResponse(String msg, JSONArray jsonArray) {
                super.onSuccessResponse(msg, jsonArray);
                mDaysAdapter.setJSONArray(jsonArray);
            }
        });
    }

    @Override
    public void onRightClick(View v) {
        if (!mPopupWindow.isShowing()) {
            mPopupWindow.showAsDropDown(v, 0, 0);
        }
    }
}
