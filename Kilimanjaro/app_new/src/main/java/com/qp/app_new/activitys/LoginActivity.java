package com.qp.app_new.activitys;

import com.qp.app_new.R;

/**
 * Created by Aaron on 17/11/8.
 */

public class LoginActivity extends BaseActivity {
    @Override
    public void setContentView () {
        layoutId = R.layout.activity_login;
    }

    @Override
    public void initView () {
        initActionBar ();
        setTitle ("登录");
    }
}
