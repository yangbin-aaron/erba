package com.teb.kilimanjaro.activitys.hall;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.teb.kilimanjaro.AppPrefs;
import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.activitys.BaseActivity;
import com.teb.kilimanjaro.adapters.LotteryDeMybetAdapter;
import com.teb.kilimanjaro.adapters.LotteryDeRosterAdapter;
import com.teb.kilimanjaro.configs.HandlerConfig;
import com.teb.kilimanjaro.models.entry.hall.LotteryModel;
import com.teb.kilimanjaro.models.entry.hall.OddsListModel;
import com.teb.kilimanjaro.models.entry.hall.WinnersListModel;
import com.teb.kilimanjaro.mvp.presenter.LotteryDefaultPresenterImp;
import com.teb.kilimanjaro.mvp.view.LotteryDefaultViewInf;
import com.teb.kilimanjaro.utils.MyUtil;
import com.teb.kilimanjaro.utils.StringUtil;
import com.teb.kilimanjaro.utils.ToastUtil;
import com.teb.kilimanjaro.views.MyActionBar;
import com.teb.kilimanjaro.views.dialogs.MyDialog;

import java.util.HashMap;
import java.util.List;

/**
 * Created by yangbin on 16/7/13.
 * 开奖明细界面
 */
public class LotteryDefaultActivity extends BaseActivity implements MyActionBar.OnActionBarClickListener, View.OnClickListener, LotteryDefaultViewInf {

    private LotteryDefaultPresenterImp mLotteryDefaultPresenterImp;
    private Dialog mLoadingDialog;

    private MyActionBar mMyActionBar;

    // 期数，开奖时间，说明，开奖号码
    private TextView mIssueTV, mTimeTV, mCommentTV, mResultTV;

    private RelativeLayout mMyBetRL;

    private TextView mBetCoinTV, mPureProfitTV, mIncomeRateTV;

    private TextView mRosterTV, mMybetTV;// 中奖名单，我的投注
    private LinearLayout mRosterLL, mMybetLL;// 中奖名单列表布局，我的投注列表布局
    private PullToRefreshListView mRosterListView, mMybetListView;// 中奖名单列表，我的投注列表
    private LotteryDeRosterAdapter mRosterAdapter;
    private LotteryDeMybetAdapter mMybetAdapter;
    private List<OddsListModel.OddsData> mMyBetList;// 我的投注   数据列表
    private List<WinnersListModel.WinnersListData> mRosterList;// 中奖名单   数据列表
    private int pageNumber = 1;// 获取中奖数据时的页码

    private LotteryModel.LotteryData mLotteryData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottery_default);
        mLotteryDefaultPresenterImp = new LotteryDefaultPresenterImp(this);
        mLoadingDialog = MyDialog.createLoadingDialog(this, true);
        mLotteryData = (LotteryModel.LotteryData) getIntent().getSerializableExtra("data");
        initViews();
        getWinnersList();
    }

    private void initViews() {
        // ******ActionBar
        mMyActionBar = (MyActionBar) findViewById(R.id.actionBar);
        mMyActionBar.setBarBackGround(R.color.main_background);
        mMyActionBar.setBarTitleText(AppPrefs.getInstance().getSelectedGame().getGameName() + getResources().getString(R.string.lottery_d_bar));
        mMyActionBar.setOnActionBarClickListener(this);
        mMyActionBar.setBarLeftImage(R.drawable.ic_back_btn);

        // ******开奖信息
        mIssueTV = (TextView) findViewById(R.id.tv_issue);
        mIssueTV.setText(mLotteryData.getId() + "");
        mTimeTV = (TextView) findViewById(R.id.tv_time);
        mTimeTV.setText(mLotteryData.getLotteryTime());
        mCommentTV = (TextView) findViewById(R.id.tv_comment);
        mCommentTV.setText(mLotteryData.getLotteryComment() + "=");
        mResultTV = (TextView) findViewById(R.id.tv_result);
        mResultTV.setText(mLotteryData.getLotteryResult() + "");

        mMyBetRL = (RelativeLayout) findViewById(R.id.rl_mybet);
        if (mLotteryData.getBetNum() > 0) {
            mMyBetRL.setVisibility(View.VISIBLE);
            mBetCoinTV = (TextView) findViewById(R.id.tv_bet_coin);
            mBetCoinTV.setText(mLotteryData.getBetCoin() + "");
            mPureProfitTV = (TextView) findViewById(R.id.tv_pure_profit);
            mPureProfitTV.setText(mLotteryData.getPureProfit());
            mPureProfitTV.setTextColor(
                    getResources().getColor(Double.parseDouble(mLotteryData.getPureProfit()) > 0 ? R.color.red_text : R.color.green));
            mIncomeRateTV = (TextView) findViewById(R.id.tv_income_rate);
            mIncomeRateTV.setText(mLotteryData.getIncomeRate());
            mIncomeRateTV.setTextColor(
                    getResources().getColor(Double.parseDouble(mLotteryData.getPureProfit()) > 0 ? R.color.red_text : R.color.green));
        } else {
            mMyBetRL.setVisibility(View.GONE);
        }

        // ******中奖名单，我的投注 按钮
        mRosterTV = (TextView) findViewById(R.id.tv_roster);
        mMybetTV = (TextView) findViewById(R.id.tv_mybet);
        mRosterTV.setOnClickListener(this);
        mMybetTV.setOnClickListener(this);

        // ******中奖名单，我的投注 列表
        mRosterLL = (LinearLayout) findViewById(R.id.ll_roster);
        mMybetLL = (LinearLayout) findViewById(R.id.ll_mybet);
        mRosterListView = (PullToRefreshListView) findViewById(R.id.lv_roster);
        mRosterListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mRosterListView.setOnRefreshListener(mRefreshListener2);
        mMybetListView = (PullToRefreshListView) findViewById(R.id.lv_mybet);
        mMybetListView.setMode(PullToRefreshBase.Mode.DISABLED);
        mRosterAdapter = new LotteryDeRosterAdapter();
        mMybetAdapter = new LotteryDeMybetAdapter();
        mRosterListView.setAdapter(mRosterAdapter);
        mMybetListView.setAdapter(mMybetAdapter);
        
        TextView tv_getgamecoin1 = (TextView) findViewById(R.id.tv_getgamecoin1);
        TextView tv_getgamecoin2 = (TextView) findViewById(R.id.tv_getgamecoin2);
        if (AppPrefs.getInstance().getSelectedGame().isSimulate()){
            tv_getgamecoin1.setText(R.string.lottery_d_getscore);
            tv_getgamecoin2.setText(R.string.lottery_d_getscore);
        }

        changeListView(0);// 默认显示第一个
    }

    // 下拉  上拉刷新事件
    private PullToRefreshBase.OnRefreshListener2 mRefreshListener2 = new PullToRefreshBase.OnRefreshListener2<ListView>() {
        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            switch (refreshView.getId()) {
//                case R.id.lv_roster:
//
//                    mRosterListView.onRefreshComplete();
//                    return;
//                case R.id.lv_mybet:
//
//                    mMybetListView.onRefreshComplete();
//                    return;
            }
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            switch (refreshView.getId()) {
                case R.id.lv_roster:
                    getWinnersList();
                    return;
//                case R.id.lv_mybet:
//
//                    mMybetListView.onRefreshComplete();
//                    return;
            }
        }
    };

    private void getWinnersList() {
        if (mRosterList == null) {
            mLoadingDialog.show();
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("gameId", mLotteryData.getGameId());
        hashMap.put("gameLotteryId", mLotteryData.getId());
        hashMap.put("pageNumber", pageNumber);
        hashMap.put("pageSize", 20);
        mLotteryDefaultPresenterImp.getRosterList(StringUtil.getJson(hashMap));
    }

    /**
     * 选择的按钮
     *
     * @param selector
     */
    private void changeListView(int selector) {
        switch (selector) {
            case 0:
                mRosterTV.setTextColor(getResources().getColor(R.color.white));
                mRosterTV.setBackgroundResource(R.drawable.radio_red_left_bg);
                mMybetTV.setTextColor(getResources().getColor(R.color.main_background));
                mMybetTV.setBackgroundResource(R.color.transparent);
                mRosterLL.setVisibility(View.VISIBLE);
                mMybetLL.setVisibility(View.GONE);
                break;
            case 1:
                mMybetTV.setTextColor(getResources().getColor(R.color.white));
                mMybetTV.setBackgroundResource(R.drawable.radio_red_right_bg);
                mRosterTV.setTextColor(getResources().getColor(R.color.main_background));
                mRosterTV.setBackgroundResource(R.color.transparent);
                mMybetLL.setVisibility(View.VISIBLE);
                mRosterLL.setVisibility(View.GONE);
                if (mMyBetList == null) {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("gameId", mLotteryData.getGameId());
                    hashMap.put("gameLotteryId", mLotteryData.getId());
                    mLoadingDialog.show();
                    mLotteryDefaultPresenterImp.getMyBetList(StringUtil.getJson(hashMap));
                }
                break;
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
            case R.id.tv_roster:
                changeListView(0);
                break;
            case R.id.tv_mybet:
                changeListView(1);
                break;
        }
    }

    // --------------------LotteryDefaultViewInf method---------------------

    /**
     * 处理我的投注数据
     *
     * @param msg
     */
    @Override
    public void sendMyBetMessage(Message msg) {
        if (mLoadingDialog != null) mLoadingDialog.dismiss();
        switch (msg.what) {
            case HandlerConfig.WHAT_POST_SUCCESS:
                mMyBetList = (List<OddsListModel.OddsData>) msg.obj;
                mMybetAdapter.setList(mMyBetList);
                break;
            default:
                MyUtil.handMessage(this, msg, "LotteryDefaultActivity---MyBet>>>");
                break;
        }
    }

    /**
     * 处理中奖名单数据
     *
     * @param msg
     */
    @Override
    public void sendRosterMessage(Message msg) {
        if (mLoadingDialog != null) mLoadingDialog.dismiss();
        mRosterListView.onRefreshComplete();
        switch (msg.what) {
            case HandlerConfig.WHAT_POST_SUCCESS:
                List<WinnersListModel.WinnersListData> tempList = (List<WinnersListModel.WinnersListData>) msg.obj;
                if (tempList.size() > 0) {
                    pageNumber++;
                } else {
                    if (mRosterList != null) ToastUtil.showToast(R.string.lottery_default_no_more_data);
                }
                if (mRosterList == null) {
                    mRosterList = tempList;
                } else {
                    mRosterList.addAll(mRosterList.size(), tempList);
                }
                mRosterAdapter.setList(mRosterList);
                break;
            default:
                MyUtil.handMessage(this, msg, "LotteryDefaultActivity---Roster>>>");
                break;
        }
    }
}
