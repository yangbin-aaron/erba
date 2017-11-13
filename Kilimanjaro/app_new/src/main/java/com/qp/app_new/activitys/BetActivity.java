package com.qp.app_new.activitys;

import com.qp.app_new.R;

import org.json.JSONObject;

/**
 * Created by Aaron on 2017/11/13.
 * 投注
 */

public class BetActivity extends BaseActivity {
    @Override
    public int getContentView() {
        return R.layout.activity_bet;
    }

    private JSONObject mJSONObject;

    @Override
    public void getIntentData() {
        super.getIntentData();
        try {
            mJSONObject = new JSONObject(getIntent().getStringExtra("data"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initView() {
        initActionBar();
        setLeftIV(R.drawable.ic_back_btn);
        setTitle(R.string.betting_str);
    }
}
