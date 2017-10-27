package com.teb.kilimanjaro.activitys.qq;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.teb.kilimanjaro.AppPrefs;
import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.activitys.BaseActivity;
import com.teb.kilimanjaro.adapters.BettingListAdapterQQ;
import com.teb.kilimanjaro.configs.HandlerConfig;
import com.teb.kilimanjaro.models.entry.hall.LotteryModel;
import com.teb.kilimanjaro.models.entry.qq.BettingListModelQQ.BettingListData;
import com.teb.kilimanjaro.mvp.presenter.BettingListOfHadLotteryPresenterImpQQ;
import com.teb.kilimanjaro.mvp.view.BettingListOfHadLotteryViewInfQQ;
import com.teb.kilimanjaro.utils.MyUtil;
import com.teb.kilimanjaro.utils.StringUtil;
import com.teb.kilimanjaro.views.MyActionBar;
import com.teb.kilimanjaro.views.dialogs.MyDialog;

import java.util.HashMap;
import java.util.List;

/**
 * Created by yangbin on 16/8/1.
 * 开奖明细界面(QQ群玩法)
 */
public class LotteryDefaultActivityQQ extends BaseActivity implements BettingListOfHadLotteryViewInfQQ {

    private BettingListOfHadLotteryPresenterImpQQ mBettingListOfHadLotteryPresenterImpQQ;
    private Dialog mLoadingDialog;

    private MyActionBar mMyActionBar;

    // 期数，开奖时间，说明，开奖号码
    private TextView mIssueTV, mTimeTV, mCommentTV, mResultTV;
    private RelativeLayout mMyBetRL;
    private TextView mBetCoinTV, mPureProfitTV, mIncomeRateTV;
    private LotteryModel.LotteryData mLotteryData;

    // 原始网站期数
    private TextView mYsIdTV;

    // 列表
    private ListView mListView;
    private List<BettingListData> mList;
    private BettingListAdapterQQ mAdapterQQ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottery_default_qq);
        mLotteryData = (LotteryModel.LotteryData) getIntent().getSerializableExtra("data");
        initViews();

        mBettingListOfHadLotteryPresenterImpQQ = new BettingListOfHadLotteryPresenterImpQQ(this);
        mLoadingDialog = MyDialog.createLoadingDialog(this, true);

        getBettingList();
    }

    private void initViews() {
        // ******MyActionBar
        mMyActionBar = (MyActionBar) findViewById(R.id.actionBar);
        mMyActionBar.setBarBackGround(R.color.main_background);
        mMyActionBar.setBarLeftImage(R.drawable.ic_back_btn);
        mMyActionBar.setBarTitleText(R.string.qq_lottery_default_bar);
        mMyActionBar.setOnActionBarClickListener(new MyActionBar.OnActionBarClickListener() {
            @Override
            public void onActionBarLeftClicked() {
                finish();
            }

            @Override
            public void onActionBarRightClicked() {

            }
        });

        mYsIdTV = (TextView) findViewById(R.id.tv_hint);
        mIssueTV = (TextView) findViewById(R.id.tv_issue);
        mTimeTV = (TextView) findViewById(R.id.tv_time);
        mCommentTV = (TextView) findViewById(R.id.tv_comment);
        mResultTV = (TextView) findViewById(R.id.tv_result);
        if (mLotteryData != null) {
            String text = getString(R.string.lottery_d_ys_id, AppPrefs.getInstance().getSelectedGame().getReferName())
                    + getString(R.string.lottery_d_ys_id1, mLotteryData.getReferLotteryId());
            mYsIdTV.setText(text);
            mIssueTV.setText(mLotteryData.getLotteryId() + "");
            mTimeTV.setText(mLotteryData.getLotteryTime());
            mCommentTV.setText(mLotteryData.getLotteryComment() + "=");
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
        }

        // 我的投注列表

        mListView = (ListView) findViewById(R.id.lv_bettinglist);
        mAdapterQQ = new BettingListAdapterQQ();
        mListView.setAdapter(mAdapterQQ);
    }

    /**
     * 获取网络数据
     */
    private void getBettingList() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("gameId", AppPrefs.getInstance().getSelectedGame().getId());
        hashMap.put("id", mLotteryData.getId());
        mBettingListOfHadLotteryPresenterImpQQ.getBettingListOfHadLottery(StringUtil.getJson(hashMap));
        mLoadingDialog.show();
    }

    // --------------------BettingListOfHadLotteryViewInfQQ method---------------------

    @Override
    public void sendBettingListOfHadLotteryMssage(Message msg) {
        if (mLoadingDialog != null) mLoadingDialog.dismiss();
        switch (msg.what) {
            case HandlerConfig.WHAT_POST_SUCCESS:
                mList = (List<BettingListData>) msg.obj;
                if (mList.size() > 0) {
                    mAdapterQQ.setList(mList);
                } else {
                    findViewById(R.id.tv_no_beted).setVisibility(View.VISIBLE);
                }
                break;
            default:
                MyUtil.handMessage(this, msg, "LotteryDefaultActivityQQ---bettingList>>>");
                break;
        }
    }
}
