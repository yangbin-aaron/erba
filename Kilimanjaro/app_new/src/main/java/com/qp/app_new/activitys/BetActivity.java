package com.qp.app_new.activitys;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.qp.app_new.App;
import com.qp.app_new.AppPrefs;
import com.qp.app_new.R;
import com.qp.app_new.adapters.BetModeAdapter;
import com.qp.app_new.adapters.BetNumAdapter;
import com.qp.app_new.contents.AppContent;
import com.qp.app_new.httpnetworks.NetWorkManager;
import com.qp.app_new.interfaces.NetListener;
import com.qp.app_new.utils.BetUtils;
import com.qp.app_new.utils.StringUtil;
import com.qp.app_new.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aaron on 2017/11/13.
 * 投注
 */

public class BetActivity extends BaseActivity implements View.OnClickListener, BetNumAdapter.OnBetNumItemClickListener, BetModeAdapter.OnBetModeItemClickListener {
    @Override
    public int getContentView() {
        return R.layout.activity_bet;
    }

    private final static String DANDIAN = "dandian";
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

    private TextView mClearTV;// 清除
    private TextView mLastIssueTV, mBetTV;// 上期投注，投注

    private long mTotalCoin = 0;// 本期已经投注额度

    private Handler mHandler = new Handler();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initView() {
        initActionBar();
        setLeftIV(R.drawable.ic_back_btn);
        setTitle(R.string.betting_str);
        // ******奖注信息
        mIssueTV = (TextView) findViewById(R.id.tv_issue);
        mLotteryTimeTV = (TextView) findViewById(R.id.tv_lottery_time);
        mSurplusTimeTV = (TextView) findViewById(R.id.tv_bet_surplus_time);
        mLotteryStateTV = (TextView) findViewById(R.id.tv_lottery_state);

        if (mLotteryJSONObject != null) {
            mIssueTV.setText(mLotteryJSONObject.optString("lotteryId"));
            mLotteryTimeTV.setText(StringUtil.getShowTime4(mLotteryJSONObject.optString("lotteryTime")));
            mSurplusTime = mLotteryJSONObject.optInt("lotterySecond") - stopBetSecond;
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

    /**
     * 获取模式数据
     */
    private void getBetModes() {
        NetWorkManager.getInstance().getBetModeList(mLoadingDialog, new NetListener() {
            @Override
            public void onSuccessResponse(String msg, JSONArray jsonArray) {
                super.onSuccessResponse(msg, jsonArray);
                mModeJSONArray = jsonArray;
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
                lastBetMode();
                break;
            case R.id.btn_betting:
                if (mSelectIndex != -1) {
                    long canBetCoin = App.currentGameJsonObject.optLong("capCoin") - mTotalCoin;
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
//                    new BetAllDialog(this, nums)
//                            .setDefaultCoin(mBetCoin + "")
//                            .setMaxCoin(canBetCoin)
//                            .listener(new BetAllDialog.OnDialogClickListener() {
//                                @Override
//                                public void onRightClicked(long coin) {
//                                    mBetCoin = coin;
//                                    compareGameCoin();
//                                }
//                            })
//                            .show();
                }
                break;
        }
    }

    /**
     * 上次投注
     */
    private void lastBetMode() {
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

    // ACTION_REFRESH_GAMELIST   投注完成之后  发送广播刷新数据
}
