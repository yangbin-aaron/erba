package com.qp.app_new.fragments;

import com.qp.app_new.R;

/**
 * Created by Aaron on 17/11/7.
 */

public class HomeFragment extends BaseFragment {

    @Override
    public void setContentView () {
        layoutId = R.layout.fragment_home;
    }

    @Override
    public void initView () {
        initActionBar ();
    }
}
