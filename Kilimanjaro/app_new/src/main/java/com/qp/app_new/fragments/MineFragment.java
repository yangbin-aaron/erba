package com.qp.app_new.fragments;

import com.qp.app_new.R;

/**
 * Created by Aaron on 2017/11/8.
 */

public class MineFragment extends BaseFragment {
    @Override
    public void setContentView() {
        layoutId = R.layout.fragment_mine;
    }

    @Override
    public void initView() {
        initActionBar();
        setTitle(R.string.bottom_tab_mine);
    }
}
