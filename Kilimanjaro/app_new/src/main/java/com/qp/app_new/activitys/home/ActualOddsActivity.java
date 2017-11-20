package com.qp.app_new.activitys.home;

import android.widget.ListView;

import com.qp.app_new.R;
import com.qp.app_new.activitys.BaseActivity;
import com.qp.app_new.adapters.ActualOddsAdapter;
import com.qp.app_new.httpnetworks.NetWorkManager;
import com.qp.app_new.interfaces.NetListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aaron on 2017/11/20.
 * 实际赔率
 */

public class ActualOddsActivity extends BaseActivity {
    @Override
    public int getContentView() {
        return R.layout.activity_actual_odds;
    }

    private JSONObject mGameJSONObject;
    private ActualOddsAdapter mAdapter;

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
        setTitle(getString(R.string.odds_title, mGameJSONObject.optString("gameName")));

        ListView lv = (ListView) findViewById(R.id.actual_odds_lv);
        mAdapter = new ActualOddsAdapter();
        lv.setAdapter(mAdapter);

        getBetModes();
    }

    private void getBetModes() {
        NetWorkManager.getInstance().getBetModeList(mLoadingDialog, new NetListener() {
            @Override
            public void onSuccessResponse(String msg, JSONArray jsonArray) {
                super.onSuccessResponse(msg, jsonArray);
                mAdapter.setJSONArray(jsonArray);
            }
        });
    }
}
