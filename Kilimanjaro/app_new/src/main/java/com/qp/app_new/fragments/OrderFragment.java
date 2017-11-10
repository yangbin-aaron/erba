package com.qp.app_new.fragments;

import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.qp.app_new.R;
import com.qp.app_new.contents.AppPrefsContent;
import com.qp.app_new.dialogs.DialogHelp;
import com.qp.app_new.interfaces.PopupWindowItemListener;
import com.qp.app_new.utils.ActivityStartUtils;
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

    private PullToRefreshListView mOrderDLV, mOrderWLV, mOrderMLV;
    private View mPullView, mErrorView;
    private TextView mErrorTV;
    private int mPageNum = 1;
    private static final int PAGE_SIZE = 15;
    private String[] mPopList;
    private int mPosition = 0;

    private PopupWindow mPopupWindow;

    @Override
    public void initView () {
        mPopList = new String[]{getString (R.string.bottom_tab_order_d),
                getString (R.string.bottom_tab_order_w),
                getString (R.string.bottom_tab_order_m)};
        initActionBar ();
        setRightIV (R.drawable.ic_menu);
        mPopupWindow = DialogHelp.createPopupWindow (getActivity (), mPopList, new PopupWindowItemListener () {
            @Override
            public void onPopItemClick (View v, int position) {
                mPosition = position;
                updateData ();
                mPopupWindow.dismiss ();
            }
        });

        mPullView = findViewById (R.id.order_rl);

        mOrderDLV = (PullToRefreshListView) findViewById (R.id.order_d_lv);
        mOrderDLV.setMode (PullToRefreshBase.Mode.BOTH);
        mOrderDLV.setOnRefreshListener (mOnRefreshListener2);
        mOrderWLV = (PullToRefreshListView) findViewById (R.id.order_w_lv);
        mOrderWLV.setMode (PullToRefreshBase.Mode.BOTH);
        mOrderWLV.setOnRefreshListener (mOnRefreshListener2);
        mOrderMLV = (PullToRefreshListView) findViewById (R.id.order_m_lv);
        mOrderMLV.setMode (PullToRefreshBase.Mode.BOTH);
        mOrderMLV.setOnRefreshListener (mOnRefreshListener2);

        mErrorView = findViewById (R.id.error_ly);
        mErrorView.setOnClickListener (this);
        mErrorTV = (TextView) findViewById (R.id.error_tv);
    }

    private PullToRefreshBase.OnRefreshListener2 mOnRefreshListener2 = new PullToRefreshBase.OnRefreshListener2 () {
        @Override
        public void onPullDownToRefresh (PullToRefreshBase refreshView) {
            mPageNum = 1;
            getOrderList ();
        }

        @Override
        public void onPullUpToRefresh (PullToRefreshBase refreshView) {
            getOrderList ();
        }
    };

    @Override
    public void onResume () {
        super.onResume ();
        updateData ();
    }

    public void updateData () {
        // 如果没有数据，才加载
        setTitle (mPopList[mPosition]);
        mOrderDLV.setVisibility (mPosition == 0 ? View.VISIBLE : View.GONE);
        mOrderWLV.setVisibility (mPosition == 1 ? View.VISIBLE : View.GONE);
        mOrderMLV.setVisibility (mPosition == 2 ? View.VISIBLE : View.GONE);
        // 判断是否有登录
        if (!AppPrefsContent.isLogined ()) {
            mErrorTV.setText (R.string.click_login);
            return;
        }

        mPullView.setVisibility (View.VISIBLE);
        mErrorView.setVisibility (View.GONE);
        getOrderList ();
    }

    // ************************************
    private void getOrderList () {// 在此方法中需要顺便判断Token是否过期
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
                if (mErrorTV.getText ().toString ().equals (getString (R.string.click_login))) {
                    ActivityStartUtils.startLoginActivity (getActivity ());
                } else {// 无数据  刷新
                    mPageNum = 1;
                    getOrderList ();
                }
                break;
        }
    }

    @Override
    public void onRightClick (View v) {
        if (!mPopupWindow.isShowing ()) mPopupWindow.showAsDropDown (v, 0, 0);
    }
}
