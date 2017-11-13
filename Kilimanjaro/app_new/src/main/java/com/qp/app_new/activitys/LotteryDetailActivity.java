package com.qp.app_new.activitys;

import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.qp.app_new.R;
import com.qp.app_new.adapters.BettingListAdapter;
import com.qp.app_new.configs.NetStatusConfig;
import com.qp.app_new.httpnetworks.NetWorkManager;
import com.qp.app_new.interfaces.NetListener;
import com.qp.app_new.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Aaron on 2017/11/11.
 * 投注详情
 */

public class LotteryDetailActivity extends BaseActivity {
    @Override
    public int getContentView () {
        return R.layout.activity_lottery_detail;
    }

    private JSONObject mGameJSONObject, mLotteryJSONObject;

    private ListView mListView;
    private BettingListAdapter mAdapter;
    private TextView mErrorTV;

    @Override
    public void getIntentData () {
        super.getIntentData ();
        try {
            String gameJson = getIntent ().getStringExtra ("gameJson");
            Log.e ("LotteryDetailActivity", gameJson);
            mGameJSONObject = new JSONObject (gameJson);
            String lotteryJson = getIntent ().getStringExtra ("lotteryJson");
            Log.e ("LotteryDetailActivity", lotteryJson);
            mLotteryJSONObject = new JSONObject (lotteryJson);
        } catch (JSONException e) {
            e.printStackTrace ();
        }
    }

    @Override
    public void initView () {
        initActionBar ();
        setLeftIV (R.drawable.ic_back_btn);
        setTitle (getString (R.string.qq_lottery_default_bar, mGameJSONObject.optString ("gameName")));

        ((TextView) findViewById (R.id.tv_issue)).setText (mLotteryJSONObject.optString ("lotteryId") + " 期");
        ((TextView) findViewById (R.id.tv_comment)).setText (mLotteryJSONObject.optString ("lotteryComment") + " =");
        ((TextView) findViewById (R.id.tv_result)).setText (mLotteryJSONObject.optString ("lotteryResult"));
        ((TextView) findViewById (R.id.tv_time)).setText (StringUtil.getShowTime14 (mLotteryJSONObject.optString ("lotteryTime")));

        String text = getString (R.string.lottery_d_ys_id, mGameJSONObject.optString ("referName"))
                + getString (R.string.lottery_d_ys_id1, mLotteryJSONObject.optInt ("referLotteryId"));
        ((TextView) findViewById (R.id.tv_hint)).setText (text);

        if (mLotteryJSONObject.optInt ("betNum") > 0) {
            findViewById (R.id.ly_mybet).setVisibility (View.VISIBLE);
            ((TextView) findViewById (R.id.tv_bet_coin)).setText (mLotteryJSONObject.optString ("betCoin", "0"));
            ((TextView) findViewById (R.id.tv_pure_profit)).setText (mLotteryJSONObject.optString ("pureProfit", "0"));
            ((TextView) findViewById (R.id.tv_income_rate)).setText (mLotteryJSONObject.optString ("incomeRate", "0"));
        } else {
            findViewById (R.id.ly_mybet).setVisibility (View.GONE);
        }

        mListView = (ListView) findViewById (R.id.lv_bettinglist);
        mAdapter = new BettingListAdapter ();
        mListView.setAdapter (mAdapter);

        mErrorTV = (TextView) findViewById (R.id.tv_no_beted);
        getNetData ();
    }

    private void getNetData () {
        HashMap<String, Object> hashMap = new HashMap<> ();
        hashMap.put ("gameId", mGameJSONObject.opt ("id"));
        hashMap.put ("id", mLotteryJSONObject.optString ("id"));
        NetWorkManager.getInstance ().getBettingListOfHadLottery (StringUtil.getJson (hashMap), mLoadingDialog, new NetListener (this) {
            @Override
            public void onSuccessResponse (String msg, JSONArray jsonArray) {
                super.onSuccessResponse (msg, jsonArray);
                mAdapter.setJSONArray (jsonArray);
                mErrorTV.setVisibility (View.GONE);
            }

            @Override
            public void onErrorResponse (int errorWhat, String message) {
                mErrorTV.setVisibility (View.VISIBLE);
                if (errorWhat == NetStatusConfig.STATUS_HAVE_NO_DATA) {
                    // 没有投注
                    message = getString (R.string.bqnmytz);
                    mErrorTV.setEnabled (false);
                } else {
                    mErrorTV.setEnabled (true);
                    mErrorTV.setOnClickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            getNetData ();
                        }
                    });
                }
                mErrorTV.setText (message);

                if (NetStatusConfig.STATUS_TOKEN_IS_UPDATED == errorWhat) {
                    super.onErrorResponse (errorWhat, message);
                }
            }
        });
    }
}
