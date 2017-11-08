package com.qp.app_new.fragments;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.qp.app_new.R;
import com.qp.app_new.contents.AppPrefsContent;
import com.qp.app_new.utils.ToastUtil;

import java.util.HashMap;

/**
 * Created by Aaron on 17/11/7.
 * 排行
 */

public class OrderFragment extends BaseFragment implements View.OnClickListener {

    @Override
    public void setContentView () {
        layoutId = R.layout.fragment_order;
    }

    private PullToRefreshListView mOrderLV;
    private View mErrorView;
    private TextView mErrorTV;
    private int mPageNum = 1;
    private static final int PAGE_SIZE = 15;

    @Override
    public void initView () {
        initActionBar ();
        setTitle (R.string.bottom_tab_order);
        mOrderLV = (PullToRefreshListView) findViewById (R.id.order_lv);
        mOrderLV.setMode (PullToRefreshBase.Mode.BOTH);
        mOrderLV.setOnRefreshListener (new PullToRefreshBase.OnRefreshListener2<ListView> () {
            @Override
            public void onPullDownToRefresh (PullToRefreshBase<ListView> refreshView) {
                mPageNum = 1;
                getOrderList ();
            }

            @Override
            public void onPullUpToRefresh (PullToRefreshBase<ListView> refreshView) {
                getOrderList ();
            }
        });

        mErrorView = findViewById (R.id.error_ly);
        mErrorView.setOnClickListener (this);
        mErrorTV = (TextView) findViewById (R.id.error_tv);

        getData ();
    }

    /**
     * 另外登陆完成之后需要发广播 调此方法
     */
    private void getData () {
        // 判断是否有登录
        boolean isLogin = AppPrefsContent.isLogined ();// 需要判断  TODO
        if (!isLogin) {
            mErrorTV.setText (R.string.click_login);
            return;
        }
        getOrderList ();
    }

    // ************************************
    private void getOrderList () {
        HashMap<String, Object> hashMap = new HashMap<> ();
        hashMap.put ("pageNum", mPageNum);
        hashMap.put ("pageSize", PAGE_SIZE);
        ToastUtil.showToast ("getOrderList()");

        // 判断网络返回数据
    }

    @Override
    public void onClick (View v) {
        switch (v.getId ()) {
            case R.id.error_ly:
                if (mErrorTV.getText ().toString ().equals (getString (R.string.please_login))) {
                    ToastUtil.showToast ("去登录");
                } else {// 无数据  刷新
                    mPageNum = 1;
                    getOrderList ();
                }
                break;
        }
    }
}
