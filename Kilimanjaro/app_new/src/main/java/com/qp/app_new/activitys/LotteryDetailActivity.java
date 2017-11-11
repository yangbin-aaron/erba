package com.qp.app_new.activitys;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.qp.app_new.R;
import com.qp.app_new.utils.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aaron on 2017/11/11.
 * 投注详情
 */

public class LotteryDetailActivity extends BaseActivity {
    @Override
    public void setContentView() {
        layoutId = R.layout.activity_lottery_detail;
    }

    private JSONObject mGameJSONObject, mLotteryJSONObject;

    @Override
    public void getIntentData() {
        super.getIntentData();
        try {
            String gameJson = getIntent().getStringExtra("gameJson");
            Log.e("LotteryDetailActivity", gameJson);
            mGameJSONObject = new JSONObject(gameJson);
            String lotteryJson = getIntent().getStringExtra("lotteryJson");
            Log.e("LotteryDetailActivity", lotteryJson);
            mLotteryJSONObject = new JSONObject(lotteryJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initView() {
        initActionBar();
        setLeftIV(R.drawable.ic_back_btn);
        setTitle(getString(R.string.qq_lottery_default_bar, mGameJSONObject.optString("gameName")));

        ((TextView) findViewById(R.id.tv_issue)).setText(mLotteryJSONObject.optString("lotteryId") + " 期");
        ((TextView) findViewById(R.id.tv_comment)).setText(mLotteryJSONObject.optString("lotteryComment") + " =");
        ((TextView) findViewById(R.id.tv_result)).setText(mLotteryJSONObject.optString("lotteryResult"));
        ((TextView) findViewById(R.id.tv_time)).setText(StringUtil.getShowTime14(mLotteryJSONObject.optString("lotteryTime")));

        String text = getString(R.string.lottery_d_ys_id, mGameJSONObject.optString("referName"))
                + getString(R.string.lottery_d_ys_id1, mLotteryJSONObject.optInt("referLotteryId"));
        ((TextView) findViewById(R.id.tv_hint)).setText(text);

        if (mLotteryJSONObject.optInt("betNum") > 0) {
            findViewById(R.id.ly_mybet).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.tv_bet_coin)).setText(mLotteryJSONObject.optString("betCoin", "0"));
            ((TextView) findViewById(R.id.tv_pure_profit)).setText(mLotteryJSONObject.optString("pureProfit", "0"));
            ((TextView) findViewById(R.id.tv_income_rate)).setText(mLotteryJSONObject.optString("incomeRate", "0"));
        } else {
            findViewById(R.id.ly_mybet).setVisibility(View.GONE);
        }

    }
}
