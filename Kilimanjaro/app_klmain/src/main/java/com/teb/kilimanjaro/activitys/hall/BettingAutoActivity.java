package com.teb.kilimanjaro.activitys.hall;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.teb.kilimanjaro.AppPrefs;
import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.activitys.BaseActivity;
import com.teb.kilimanjaro.activitys.hall.modes.ModeEditActivity;
import com.teb.kilimanjaro.models.bean.BetAutoModel;
import com.teb.kilimanjaro.models.bean.ModeAutoList;
import com.teb.kilimanjaro.services.BetAutoService;
import com.teb.kilimanjaro.utils.MyUtil;
import com.teb.kilimanjaro.utils.ToastUtil;
import com.teb.kilimanjaro.views.MyActionBar;
import com.teb.kilimanjaro.views.dialogs.ModeAutpListDialog;
import com.teb.kilimanjaro.views.dialogs.MyDialog;

import java.util.List;

/**
 * Created by yangbin on 16/7/26.
 */
public class BettingAutoActivity extends BaseActivity implements View.OnClickListener {

    private static final int AUTO_BET_MIN_ISSUE_COUNT = 2;// 自动投注最少期数

    private MyActionBar mMyActionBar;

    private LinearLayout mSelectModeLL;
    private TextView mSelectModeNameTV;
    private List<ModeAutoList.ModeAutoData> mDataList;

    private CheckBox mIsContinueAfterWinCB;
    // 开始期号   自动投注期数   余额达到金额    余额不足金额
    private EditText mStartIssueET, mIssueCountET, mMaxCoinET, mMinCoinET;
    // 当前期数
    private TextView mCurrentIssueTV;

    private TextView mStartAutoBetTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_betting_auto);
        mDataList = AppPrefs.getInstance().getModeAutoList();
        initViews();
    }

    private void initViews() {
        // ******MyActionBar
        mMyActionBar = (MyActionBar) findViewById(R.id.actionBar);
        mMyActionBar.setBarBackGround(R.color.main_background);
        mMyActionBar.setBarTitleText(AppPrefs.getInstance().getSelectedGame().getGameName() + getResources().getString(R.string.betauto_bar));
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

        // ******开始参数
        mSelectModeLL = (LinearLayout) findViewById(R.id.ll_select_mode);
        mSelectModeLL.setOnClickListener(this);
        mSelectModeNameTV = (TextView) findViewById(R.id.tv_mode_name);

        setStartModeName();

        // ******结束参数
        mCurrentIssueTV = (TextView) findViewById(R.id.tv_current_issue);
        mStartIssueET = (EditText) findViewById(R.id.et_start_issue);

        mIssueCountET = (EditText) findViewById(R.id.et_auto_issue_count);
        mMaxCoinET = (EditText) findViewById(R.id.et_max_coin);
        mMinCoinET = (EditText) findViewById(R.id.et_min_coin);

        mIsContinueAfterWinCB = (CheckBox) findViewById(R.id.cb_is_continue_adter_win);
        mIsContinueAfterWinCB.setChecked(AppPrefs.getInstance().isContinueAfterWin());
        mIsContinueAfterWinCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AppPrefs.getInstance().saveIsContinueAfterWin(isChecked);
            }
        });

        // ******开始投注按钮
        mStartAutoBetTV = (TextView) findViewById(R.id.tv_start_auto_bet);
        mStartAutoBetTV.setText(AppPrefs.getInstance().getBetAutoStartState() ? R.string.betauto_stop : R.string.betauto_start);

        mStartAutoBetTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 开始自动投注
                Intent intentAutoService = new Intent(BetAutoService.ACTION);
                if (AppPrefs.getInstance().getBetAutoStartState()) {// 正在进行
                    stopService(intentAutoService);// 停止
                    AppPrefs.getInstance().saveBetAutoStartState(false);
                    mStartAutoBetTV.setText(R.string.betauto_start);
                } else {
                    if (((TextView) v).getText().toString().trim().equals(R.string.betauto_stop)) {
                        mStartAutoBetTV.setText(R.string.betauto_start);
                    } else {// 开始
                        if (saveDataToAppPrefs()) {
                            AppPrefs.getInstance().saveBetAutoStartState(true);
                            startService(intentAutoService);
                            finish();
                        }
                    }
                }
                setViewData();
            }
        });

        setViewData();
    }

    private void setViewData() {
        boolean flag = AppPrefs.getInstance().getBetAutoStartState();
        Log.e("aaaa", flag + "");
        mSelectModeLL.setEnabled(!flag);
        mStartIssueET.setEnabled(!flag);
        mIssueCountET.setEnabled(!flag);
        mMaxCoinET.setEnabled(!flag);
        mMinCoinET.setEnabled(!flag);
        mIsContinueAfterWinCB.setEnabled(!flag);

        BetAutoModel model = AppPrefs.getInstance().getBetAutoModel();
        if (model != null) {
            mStartIssueET.setText(model.getStartIssue() + "");
            mIssueCountET.setText(model.getAutoCount() + "");
            mMaxCoinET.setText(model.getMaxCoin() + "");
            mMinCoinET.setText(model.getMinCoin() + "");
        }
        if (!flag) {
            int currIssue = AppPrefs.getInstance().getCurrentBetInfo().getId();
            mCurrentIssueTV.setText(getResources().getString(R.string.betauto_current_issue, currIssue));
            mStartIssueET.setText((currIssue + 1) + "");
        }
    }

    /**
     * 保存相关数据
     */
    private boolean saveDataToAppPrefs() {
        long gameCoin = AppPrefs.getInstance().getGameLong();
        String issueStartText = mStartIssueET.getText().toString().trim();
        if (TextUtils.isEmpty(issueStartText)) issueStartText = "0";
        int issueStart = Integer.parseInt(issueStartText);
        if (issueStart <= AppPrefs.getInstance().getCurrentBetInfo().getId()) {
            ToastUtil.showToast(getResources().getString(R.string.betauto_start_issue_hine, AppPrefs.getInstance().getCurrentBetInfo().getId()));
            return false;
        }
        String issueCountText = mIssueCountET.getText().toString().trim();
        if (TextUtils.isEmpty(issueCountText)) issueCountText = "0";
        int issueCount = Integer.parseInt(issueCountText);
        if (issueCount < AUTO_BET_MIN_ISSUE_COUNT) {
            ToastUtil.showToast(getResources().getString(R.string.betauto_min_issue_count, AUTO_BET_MIN_ISSUE_COUNT));
            return false;
        }
        String maxCoinText = mMaxCoinET.getText().toString().trim();
        if (TextUtils.isEmpty(maxCoinText)) maxCoinText = "0";
        long maxCoin = Long.parseLong(maxCoinText);
        String minCoinText = mMinCoinET.getText().toString().trim();
        if (TextUtils.isEmpty(minCoinText)) minCoinText = "0";
        if (maxCoin != 0 && maxCoin < gameCoin) {
            ToastUtil.showToast(getResources().getString(R.string.betauto_coin_max, gameCoin));
            return false;
        }
        long minCoin = Long.parseLong(minCoinText);
        if (minCoin > gameCoin) {
            ToastUtil.showToast(getResources().getString(R.string.betauto_coin_min, gameCoin));
            return false;
        }

        BetAutoModel autoModel = new BetAutoModel();
        autoModel.setMinCoin(minCoin);
        autoModel.setMaxCoin(maxCoin);
        autoModel.setAutoCount(issueCount);
        autoModel.setStartIssue(issueStart);
        AppPrefs.getInstance().saveBetAutoModel(autoModel);

        Log.e("mode", new Gson().toJson(AppPrefs.getInstance().getBetAutoModel()));
        return true;
    }

    /**
     * 设置开始参数模式名称
     */
    private void setStartModeName() {
        String name = AppPrefs.getInstance().getModeStartName();
        if (TextUtils.isEmpty(name)) {
            mSelectModeNameTV.setText(R.string.betauto_no_select);
            return;
        }
        ModeAutoList.ModeAutoData data = MyUtil.findModeAutoByName(name);
        mSelectModeNameTV.setText(data.getName() == null ? getResources().getString(R.string.betauto_no_select) : data.getName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_select_mode:// 选择模式
                if (mDataList.size() == 0) {
                    new MyDialog(this)
                            .setDefaultBtnText()
                            .setMessage(getResources().getString(R.string.betauto_goto_addmode))
                            .listener(new MyDialog.OnDialogClickListener() {
                                @Override
                                public void onLeftClicked() {
                                }

                                @Override
                                public void onRightClicked() {
                                    MyUtil.startActivity(BettingAutoActivity.this, new Intent(BettingAutoActivity.this, ModeEditActivity.class));
                                }
                            })
                            .show();
                    return;
                }
                new ModeAutpListDialog(this)
                        .listener(new ModeAutpListDialog.OnDialogClickListener() {
                            @Override
                            public void onSelectModetClicked() {
                                setStartModeName();
                            }
                        })
                        .show();
                break;
        }
    }
}
