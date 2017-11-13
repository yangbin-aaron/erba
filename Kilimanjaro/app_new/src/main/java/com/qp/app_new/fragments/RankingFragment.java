package com.qp.app_new.fragments;

import android.app.Dialog;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.qp.app_new.R;
import com.qp.app_new.adapters.RankingAdapter;
import com.qp.app_new.contents.AppPrefsContent;
import com.qp.app_new.dialogs.DialogHelp;
import com.qp.app_new.httpnetworks.NetWorkManager;
import com.qp.app_new.interfaces.NetListener;
import com.qp.app_new.interfaces.PopupWindowItemListener;
import com.qp.app_new.utils.ActivityStartUtils;
import com.qp.app_new.utils.StringUtil;

import org.json.JSONArray;

import java.util.HashMap;

/**
 * Created by Aaron on 17/11/7.
 * 排行
 */

public class RankingFragment extends BaseFragment implements View.OnClickListener {

    @Override
    public int getContentView() {
        return R.layout.fragment_ranking;
    }

    private ListView mRankingLV;
    private RankingAdapter mRankingAdapter;
    private JSONArray mJSONArray;
    private View mRankingView, mErrorView;
    private TextView mErrorTV;
    private String[] mPopList;
    private int mPosition = 0;
    private Dialog mDialog = null;// 第一次不显示对话框

    private PopupWindow mPopupWindow;
    private Handler mHandler;
    private Runnable mRunnable;

    @Override
    public void initView() {
        mPopList = new String[]{getString(R.string.bottom_tab_order_d),
                getString(R.string.bottom_tab_order_w),
                getString(R.string.bottom_tab_order_m)};
        initActionBar();
        setRightIV(R.drawable.ic_menu);
        setTitle(mPopList[mPosition]);

        mPopupWindow = DialogHelp.createPopupWindow(getActivity(), mPopList, new PopupWindowItemListener() {
            @Override
            public void onPopItemClick(View v, int position) {
                mPosition = position;
                setTitle(mPopList[mPosition]);
                updateData();
                mPopupWindow.dismiss();
            }
        });

        mRankingLV = (ListView) findViewById(R.id.lv_ranking);
        mRankingAdapter = new RankingAdapter();
        mRankingLV.setAdapter(mRankingAdapter);

        mRankingView = findViewById(R.id.ly_ranking);
        mErrorView = findViewById(R.id.error_ly);
        mErrorView.setOnClickListener(this);
        mErrorTV = (TextView) findViewById(R.id.error_tv);

        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                mDialog = null;
                updateData();
                mHandler.postDelayed(mRunnable, 1000 * 60 * 1);// 一分钟刷新一次
            }
        };
    }

//    private PullToRefreshBase.OnRefreshListener2 mOnRefreshListener2 = new PullToRefreshBase.OnRefreshListener2 () {
//        @Override
//        public void onPullDownToRefresh (PullToRefreshBase refreshView) {
//            refresh ();
//        }
//
//        @Override
//        public void onPullUpToRefresh (PullToRefreshBase refreshView) {
////            getOrderList ();
//            // 加载更多
//        }
//    };

    @Override
    public void onResume() {
        super.onResume();
        mHandler.post(mRunnable);
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
    }

    // 界面刷新数据(有数据时，不需要)
    public void updateData() {
        mJSONArray = null;
        // 判断是否有登录
        if (!AppPrefsContent.isLogined()) {
            dataError(getString(R.string.click_login));
            return;
        }

        mErrorTV.setText(getString(R.string.app_loading));
        getOrderList();
    }

    // ************************************
    private void getOrderList() {// 在此方法中需要顺便判断Token是否过期
        // 判断网络返回数据
        HashMap<String, Object> agrs = new HashMap<>();
        agrs.put("type", mPosition + 1);// //类型 1:日排行 2:周排行 3:月排行  五分钟刷新一次数据
        NetWorkManager.getInstance().getGameRankingList(StringUtil.getJson(agrs), mDialog, new NetListener() {
            @Override
            public void onSuccessResponse(String msg, JSONArray jsonArray) {
                super.onSuccessResponse(msg, jsonArray);
                mDialog = mLoadingDialog;
                if (mJSONArray == null) {
                    mJSONArray = new JSONArray();
                    mJSONArray = jsonArray;
                } else {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        mJSONArray.put(jsonArray.optJSONObject(i));
                    }
                }

                // 设置到adapter
                mRankingView.setVisibility(View.VISIBLE);
                mErrorView.setVisibility(View.GONE);
                mRankingAdapter.setJSONArray(mJSONArray);
            }

            @Override
            public void onErrorResponse(int errorWhat, String message) {
                mDialog = mLoadingDialog;
                dataError(message);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.error_ly:
                if (mErrorTV.getText().toString().equals(getString(R.string.click_login))) {
                    ActivityStartUtils.startLoginActivity(getActivity());
                } else {// 无数据  刷新
                    updateData();
                }
                break;
        }
    }

    @Override
    public void onRightClick(View v) {
        if (!mPopupWindow.isShowing()) {
            mPopupWindow.showAsDropDown(v, 0, 0);
        }
    }

    private void dataError(String message) {
        mErrorTV.setText(message);
        mJSONArray = null;
        mRankingView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.VISIBLE);
    }
}
