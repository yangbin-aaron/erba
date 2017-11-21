package com.qp.app_new.activitys.home;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.qp.app_new.AppPrefs;
import com.qp.app_new.R;
import com.qp.app_new.activitys.BaseActivity;
import com.qp.app_new.adapters.BetDandianAdapter;
import com.qp.app_new.configs.BroadcastConfig;
import com.qp.app_new.dialogs.DialogHelp;
import com.qp.app_new.httpnetworks.NetWorkManager;
import com.qp.app_new.interfaces.DandianDialogListener;
import com.qp.app_new.interfaces.NetListener;
import com.qp.app_new.interfaces.NormalDialogListener1;
import com.qp.app_new.utils.StringUtil;
import com.qp.app_new.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Aaron on 2017/11/20.
 */

public class BetDandianActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    @Override
    public int getContentView() {
        return R.layout.activity_bet_dandian;
    }

    private int mSurplusTime = 0;
    private int stopBetSecond = 60;

    private JSONObject mGameJSONObject, mLotteryJSONObject;
    private int mDandianId;

    private ListView mOddsLV;
    private BetDandianAdapter mAdapter;
    private JSONArray mOddsJSONArray;
    private TextView mIssueTV, mLotteryTimeTV, mLotteryStateTV, mSurplusTimeTV;
    private TextView mClearTV;// 清除
    private TextView mBetTV;// 投注
    private TextView mGameCoinTV, mMaxCoinTV, mBetCoinTV;// 余额,投注额
    private long mTotalCoin;// 本期已经投注额度
    private long mMaxCoin = 0;
    private long mBetCoin = 0;

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
            mDandianId = getIntent().getIntExtra("ddId", 11);
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

        // ******清除按钮  投注按钮
        mClearTV = (TextView) findViewById(R.id.ll_change_mode);
        mClearTV.setOnClickListener(this);
        mBetTV = (TextView) findViewById(R.id.btn_betting);
        mBetTV.setOnClickListener(this);

        // ******余额，投注额
        mGameCoinTV = (TextView) findViewById(R.id.tv_amount);
        mBetCoinTV = (TextView) findViewById(R.id.tv_bet_coin);
        mBetCoinTV.setText("0");
        mMaxCoinTV = (TextView) findViewById(R.id.tv_coin_max);
        mMaxCoin = mGameJSONObject.optLong("capCoin") - mTotalCoin;
        mMaxCoinTV.setText(String.valueOf(mMaxCoin));

        mOddsLV = (ListView) findViewById(R.id.dandian_nums_tv);
        mOddsLV.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mAdapter = new BetDandianAdapter();
        mOddsLV.setAdapter(mAdapter);
        mOddsLV.setOnItemClickListener(this);

        createOdds();
    }

    private void createOdds() {
        mOddsJSONArray = new JSONArray();
        for (int i = 0; i < 28; i++) {
            try {
                JSONObject object = new JSONObject();
                object.put("lotteryNumber", i);
                mOddsJSONArray.put(object);
                mAdapter.setJSONArray(mOddsJSONArray);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_change_mode:
                for (int i = 0; i < mOddsJSONArray.length(); i++) {
                    JSONObject jsonObject = mOddsJSONArray.optJSONObject(i);
                    try {
                        jsonObject.put("amt", 0);
                        mOddsJSONArray.put(i, jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mAdapter.setJSONArray(mOddsJSONArray);
                break;
            case R.id.btn_betting:
                DialogHelp.createOkDialog(this, getString(R.string.betting_confirm_bet, mBetCoin), new NormalDialogListener1() {
                    @Override
                    public void onOkClickListener() {
                        bet();
                    }
                }).show();
                break;
        }
    }

    private void bet() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("gameId", mGameJSONObject.optString("id"));
        hashMap.put("id", mLotteryJSONObject.optString("id"));
        hashMap.put("lotteryId", mLotteryJSONObject.optString("lotteryId"));
        hashMap.put("betPatternId", mDandianId);// 单点的id
        List<HashMap<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < mOddsJSONArray.length(); i++) {
            JSONObject object = mOddsJSONArray.optJSONObject(i);
            if (object.optLong("amt") > 0) {
                HashMap<String, Object> bet = new HashMap<>();
                bet.put("betCoin", object.optLong("amt"));
                bet.put("betNum", object.optLong("lotteryNumber"));
                list.add(bet);
            }
        }
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
    protected void onResume() {
        super.onResume();
        long gameCoin = AppPrefs.getInstance().getGameCoin();
        mGameCoinTV.setText(gameCoin + "");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null || mSurplusTime >= -100) mHandler.removeCallbacks(mRunnable);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        final JSONObject jsonObject = mOddsJSONArray.optJSONObject(position);
        final long oldAmt = jsonObject.optLong("amt");
        DialogHelp.createDanDianDialog(this, oldAmt, new DandianDialogListener() {
            @Override
            public void onOkClickListener(long amt) {
                if (amt != oldAmt) {
                    try {
                        long betCoin = mBetCoin - oldAmt + amt;
                        if (betCoin > mMaxCoin) {
                            amt = mMaxCoin - mBetCoin;
                            betCoin = mMaxCoin;
                            ToastUtil.showToast("本期投注已达上限！");
                        }
                        mBetCoin = betCoin;
                        mBetCoinTV.setText(String.valueOf(mBetCoin));
                        jsonObject.put("amt", amt);
                        mOddsJSONArray.put(position, jsonObject);
                        mAdapter.setJSONArray(mOddsJSONArray);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).show();
    }
}
