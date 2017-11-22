package com.qp.app_new.activitys.home;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.qp.app_new.App;
import com.qp.app_new.AppPrefs;
import com.qp.app_new.R;
import com.qp.app_new.activitys.BaseActivity;
import com.qp.app_new.adapters.BetModeAdapter;
import com.qp.app_new.adapters.BetNumAdapter;
import com.qp.app_new.configs.BroadcastConfig;
import com.qp.app_new.contents.AppContent;
import com.qp.app_new.dialogs.BetAllDialog;
import com.qp.app_new.dialogs.DialogHelp;
import com.qp.app_new.httpnetworks.NetWorkManager;
import com.qp.app_new.interfaces.NetListener;
import com.qp.app_new.interfaces.NormalDialogListener1;
import com.qp.app_new.utils.ActivityStartUtils;
import com.qp.app_new.utils.BetUtils;
import com.qp.app_new.utils.StringUtil;
import com.qp.app_new.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Aaron on 2017/11/13.
 * 投注
 */

public class BetActivity extends BaseActivity implements View.OnClickListener, BetNumAdapter.OnBetNumItemClickListener, BetModeAdapter
        .OnBetModeItemClickListener {
    @Override
    public int getContentView() {
        return R.layout.activity_bet;
    }

    private final static String DANDIAN = "dandian";
    private final static String ZIDINGYI = "zidingyi";// 自定义
    private int mDanDianId;// 单点的ID

    private int mSurplusTime = 0;
    private int stopBetSecond = 60;
    private int mSelectIndex = -1;

    private JSONObject mGameJSONObject, mLotteryJSONObject;
    private TextView mIssueTV, mLotteryTimeTV, mLotteryStateTV, mSurplusTimeTV;
    private GridView mModeGV, mNumGV;
    private JSONArray mModeJSONArray, mOddsJSONArray;
    private BetModeAdapter mBetModeAdapter;
    private BetNumAdapter mBetNumAdapter;
    private String mBetNums = "";

    private TextView mClearTV;// 清除
    private TextView mLastIssueTV, mBetTV;// 上期投注，投注

    private TextView mGameCoinTV, mBetCoinTV;// 余额,投注额
    private long mTotalCoin;// 本期已经投注额度
    private long mBetCoin = 20;// 默认投注额为20

    private Handler mHandler;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mSurplusTime--;
            if (mSurplusTime > 0) {
                mLotteryStateTV.setVisibility(View.GONE);
                findViewById(R.id.ll_bet_state).setVisibility(View.VISIBLE);
                mSurplusTimeTV.setText(mSurplusTime + "");// 逻辑没有问题，先减少1s再setText
            } else {
                mBetTV.setEnabled(false);
                mBetTV.setBackgroundResource(R.drawable.shape_r5_grayx_str0);
                mLotteryStateTV.setVisibility(View.VISIBLE);
                findViewById(R.id.ll_bet_state).setVisibility(View.GONE);
                if (mSurplusTime > -stopBetSecond) {
                    mLotteryStateTV.setText(R.string.betting_endbet);
                } else {
                    mLotteryStateTV.setText(R.string.betting_open_result);
                }
            }
            if (mSurplusTime < -100) {
                mHandler.removeCallbacks(mRunnable);
            } else {
                mHandler.postDelayed(mRunnable, 1000);
            }
        }
    };

    @Override
    public void getIntentData() {
        super.getIntentData();
        try {
            mGameJSONObject = new JSONObject(getIntent().getStringExtra("gameJsonObject"));
            stopBetSecond = mGameJSONObject.optInt("stopBetSecond", 60);
            mLotteryJSONObject = new JSONObject(getIntent().getStringExtra("lotteryJsonObject"));
            mTotalCoin = mLotteryJSONObject.optLong("total_coin", 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initView() {
        initActionBar();
        setLeftIV(R.drawable.ic_back_btn);
        setTitle(getString(R.string.betting_text, mGameJSONObject.optString("gameName")));
        // ******奖注信息
        mIssueTV = (TextView) findViewById(R.id.tv_issue);
        mLotteryTimeTV = (TextView) findViewById(R.id.tv_lottery_time);
        mSurplusTimeTV = (TextView) findViewById(R.id.tv_bet_surplus_time);
        mLotteryStateTV = (TextView) findViewById(R.id.tv_lottery_state);

        if (mLotteryJSONObject != null) {
            mIssueTV.setText(mLotteryJSONObject.optString("lotteryId"));
            mLotteryTimeTV.setText(StringUtil.getShowTime4(mLotteryJSONObject.optString("lotteryTime")));
            mSurplusTime = mLotteryJSONObject.optInt("lotterySecond") - stopBetSecond;
            mHandler = new Handler();
            mHandler.post(mRunnable);
        }

        // ******清除按钮
        mClearTV = (TextView) findViewById(R.id.ll_change_mode);
        mClearTV.setOnClickListener(this);
        // ******下面的两个按钮
        mLastIssueTV = (TextView) findViewById(R.id.btn_last_issue);
        mLastIssueTV.setOnClickListener(this);
        mBetTV = (TextView) findViewById(R.id.btn_betting);
        mBetTV.setOnClickListener(this);

        mModeGV = (GridView) findViewById(R.id.gv_mode);
        mModeGV.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mBetModeAdapter = new BetModeAdapter();
        mModeGV.setAdapter(mBetModeAdapter);
        mBetModeAdapter.setOnBetModeItemClickListener(this);

        mNumGV = (GridView) findViewById(R.id.gv_nums);
        mNumGV.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mBetNumAdapter = new BetNumAdapter();
        mBetNumAdapter.setOnBetNumItemClickListener(this);
        mNumGV.setAdapter(mBetNumAdapter);
        createOdds();
        mBetNumAdapter.setJSONArray(mOddsJSONArray);

        // ******余额，投注额
        mGameCoinTV = (TextView) findViewById(R.id.tv_amount);
        mBetCoinTV = (TextView) findViewById(R.id.tv_bet_coin);
        mBetCoinTV.setText(mBetCoin + "");

        getBetModes();
    }

    private void createOdds() {
        mOddsJSONArray = new JSONArray();
        for (int i = 0; i < 28; i++) {
            try {
                JSONObject object = new JSONObject();
                object.put("lotteryNumber", i);
                mOddsJSONArray.put(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        long gameCoin = AppPrefs.getInstance().getGameCoin();
        mGameCoinTV.setText(gameCoin + "");
    }

    /**
     * 获取模式数据
     */
    private void getBetModes() {
        NetWorkManager.getInstance().getBetModeList(mLoadingDialog, new NetListener() {
            @Override
            public void onSuccessResponse(String msg, JSONArray jsonArray) {
                super.onSuccessResponse(msg, jsonArray);
                mModeJSONArray = jsonArray;
                if (App.openDandianActivity) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("patternName", "自定义");
                        jsonObject.put("code", "zidingyi");
                        mModeJSONArray.put(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mBetModeAdapter.setJSONArray(mModeJSONArray);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_change_mode:
                // 恢复初始状态
                if (mSelectIndex != -1) {
                    JSONObject modeJsonObject = mModeJSONArray.optJSONObject(mSelectIndex);
                    try {
                        modeJsonObject.put(AppContent.ISSELECTED, false);
                        mModeJSONArray.put(mSelectIndex, modeJsonObject);
                        mBetModeAdapter.setJSONArray(mModeJSONArray);
                        mSelectIndex = -1;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // 清除数字选择
                    mOddsJSONArray = BetUtils.handlerOddsListByTimes(mOddsJSONArray, 0);
                    mBetNumAdapter.setJSONArray(mOddsJSONArray);
                    mBetNumAdapter.setCanClick(false);
                }
                break;
            case R.id.btn_last_issue:
                getLastBetMode();
                break;
            case R.id.btn_betting:
                if (mSelectIndex != -1) {
                    long canBetCoin = mGameJSONObject.optLong("capCoin") - mTotalCoin;
                    int nums = 0;
                    boolean isDandian = false;
                    for (int i = 0; i < mModeJSONArray.length(); i++) {
                        JSONObject modeObject = mModeJSONArray.optJSONObject(i);
                        if (modeObject.optBoolean(AppContent.ISSELECTED) && modeObject.optString("code").equals(DANDIAN)) {
                            isDandian = true;
                            for (int j = 0; j < mOddsJSONArray.length(); j++) {
                                JSONObject oddObject = mOddsJSONArray.optJSONObject(j);
                                if (oddObject.optBoolean(AppContent.ISSELECTED)) {
                                    nums++;
                                }
                            }
                            break;
                        }
                    }
                    if (isDandian && nums == 0) return;// 单点，没有选球
                    new BetAllDialog(this, nums)
                            .setDefaultCoin(mBetCoin + "")
                            .setMaxCoin(canBetCoin)
                            .listener(new BetAllDialog.OnDialogClickListener() {
                                @Override
                                public void onRightClicked(long coin) {
                                    mBetCoin = coin;
                                    compareGameCoin();
                                }
                            })
                            .show();
                }
                break;
        }
    }

    /**
     * 保存投注的信息（上次投注功能）
     */
    private void saveLastBetMode() {
        JSONObject jsonObject = mModeJSONArray.optJSONObject(mSelectIndex);
        try {
            jsonObject.put("includeNum", mBetNums);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AppPrefs.getInstance().saveLastBetMode(jsonObject);
    }

    /**
     * 上次投注
     */
    private void getLastBetMode() {
        JSONObject modeJsonObject = AppPrefs.getInstance().getLastBetMode();
        try {
            if (modeJsonObject != null) {
                String nums[] = modeJsonObject.optString("includeNum").split(",");
                mOddsJSONArray = BetUtils.handlerOddsList(nums, mOddsJSONArray);
                mBetNumAdapter.setJSONArray(mOddsJSONArray);
                mBetNumAdapter.setCanClick(mDanDianId == modeJsonObject.optInt("id"));

                // 设置模式按钮
                if (mSelectIndex != -1) {
                    JSONObject selectObject = mModeJSONArray.optJSONObject(mSelectIndex);
                    selectObject.put(AppContent.ISSELECTED, false);
                    mModeJSONArray.put(mSelectIndex, selectObject);
                }
                int position = -1;
                for (int i = 0; i < mModeJSONArray.length(); i++) {
                    if (modeJsonObject.optInt("id") == mModeJSONArray.optJSONObject(i).optInt("id")) {
                        position = i;
                        break;
                    }
                }
                mSelectIndex = position;
                JSONObject currJsonObject = mModeJSONArray.optJSONObject(position);
                currJsonObject.put(AppContent.ISSELECTED, true);
                mModeJSONArray.put(position, currJsonObject);
                mBetModeAdapter.setJSONArray(mModeJSONArray);
            } else {
                ToastUtil.showToast("没有找到您的投注记录！");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBetNumItemClickListener(int position, JSONObject jsonObject) {
        // 选球
        boolean isSelected = jsonObject.optBoolean(AppContent.ISSELECTED, false);
        try {
            jsonObject.put(AppContent.ISSELECTED, !isSelected);
            mOddsJSONArray.put(position, jsonObject);
            mBetNumAdapter.setJSONArray(mOddsJSONArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBetModeItemClickListener(int position, JSONObject jsonObject) {
        try {
            if (jsonObject.optString("code").equals(ZIDINGYI)) {
                int ddId = 11;
                for (int i = 0; i < mModeJSONArray.length(); i++) {
                    if (mModeJSONArray.optJSONObject(i).optString("code").equals(DANDIAN)) {
                        ddId = mModeJSONArray.optJSONObject(i).optInt("id");
                        break;
                    }
                }
                ActivityStartUtils.startBetDandianActivity(this, mGameJSONObject, mLotteryJSONObject, ddId);
                return;
            }
            if (mSelectIndex != -1) {
                JSONObject selectedObject = mModeJSONArray.optJSONObject(mSelectIndex);

                selectedObject.put(AppContent.ISSELECTED, false);
                mModeJSONArray.put(mSelectIndex, selectedObject);

            }
            mSelectIndex = position;
            jsonObject.put(AppContent.ISSELECTED, true);
            mModeJSONArray.put(position, jsonObject);
            mBetModeAdapter.setJSONArray(mModeJSONArray);

            if (jsonObject.optString("code").equals(DANDIAN)) {
                mDanDianId = jsonObject.optInt("id");
                mOddsJSONArray = BetUtils.handlerOddsListByTimes(mOddsJSONArray, 0);
                mBetNumAdapter.setJSONArray(mOddsJSONArray);
                mBetNumAdapter.setCanClick(true);
            } else {
                String nums[] = jsonObject.optString("includeNum").split(",");
                mOddsJSONArray = BetUtils.handlerOddsList(nums, mOddsJSONArray);
                mBetNumAdapter.setJSONArray(mOddsJSONArray);
                mBetNumAdapter.setCanClick(false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 比较余额
     */
    private void compareGameCoin() {
        mBetCoinTV.setText(mBetCoin + "");
        // 判断余额是否充足
        long gameCoin = AppPrefs.getInstance().getGameCoin();
        for (int i = 0; i < mOddsJSONArray.length(); i++) {
            JSONObject object = mOddsJSONArray.optJSONObject(i);
            if (object.optBoolean(AppContent.ISSELECTED)) {
                mBetNums += "," + object.optString("lotteryNumber");
            }
        }
        if (!TextUtils.isEmpty(mBetNums)) mBetNums = mBetNums.substring(1);
        if (gameCoin < mBetCoin) {// 余额不足，请充值
            saveLastBetMode();
            DialogHelp.showMessageDialog(this, getString(R.string.coin_nohave_residual));
            return;
        }
        DialogHelp.createOkDialog(this, getString(R.string.betting_confirm_bet, mBetCoin), new NormalDialogListener1() {
            @Override
            public void onOkClickListener() {
                bet();
            }
        }).show();
    }

    private void bet() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("gameId", mGameJSONObject.optString("id"));
        hashMap.put("id", mLotteryJSONObject.optString("id"));
        hashMap.put("lotteryId", mLotteryJSONObject.optString("lotteryId"));
        hashMap.put("betPatternId", mModeJSONArray.optJSONObject(mSelectIndex).optString("id"));
        List<HashMap<String, Object>> list = new ArrayList<>();
        HashMap<String, Object> bet = new HashMap<>();
        bet.put("betCoin", mBetCoin);
        bet.put("betNum", mBetNums);
        list.add(bet);
        hashMap.put("bets", list);

        NetWorkManager.getInstance().orderBet(StringUtil.getJson(hashMap), mLoadingDialog, new NetListener(this) {
            @Override
            public void onSuccessResponse(JSONObject jsonObject) {
                super.onSuccessResponse(jsonObject);
                ToastUtil.showToast("投注成功！");
                sendBroadcast(new Intent(BroadcastConfig.ACTION_REFRESH_GAMELIST));
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null || mSurplusTime >= -100) mHandler.removeCallbacks(mRunnable);
    }
}
