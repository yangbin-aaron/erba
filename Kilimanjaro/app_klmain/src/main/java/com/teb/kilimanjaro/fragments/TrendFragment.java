package com.teb.kilimanjaro.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;
import com.teb.kilimanjaro.App;
import com.teb.kilimanjaro.AppPrefs;
import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.activitys.MainActivity;
import com.teb.kilimanjaro.configs.HandlerConfig;
import com.teb.kilimanjaro.models.entry.hall.GameListModel;
import com.teb.kilimanjaro.mvp.presenter.TrendPresenterImp;
import com.teb.kilimanjaro.views.dialogs.MyDialog;
import com.teb.kilimanjaro.mvp.view.TrendViewInf;
import com.teb.kilimanjaro.utils.StringUtil;
import com.teb.kilimanjaro.views.MyActionBar;

import java.util.HashMap;

/**
 * Created by yangbin on 16/7/1.
 * 趋势 界面fragment
 */
public class TrendFragment extends Fragment implements TrendViewInf, MyActionBar.OnActionBarClickListener {

    private static MyActionBar mMyActionBar;// ActionBar

    private PullToRefreshWebView mWebView;

    private boolean mIsBigChart;// 大图还是小图
    private int mLimit;// 期数
    private int mGameId;
    private boolean mIsFirstLoad = true;

    private TrendPresenterImp mTrendPresenterImp;
    private Dialog mLoadingDialog;

    private final static int DISMISS_DIALOG = 0x00;// 关闭加载对话框
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DISMISS_DIALOG:
                    if (mIsFirstLoad) {
                        if (getActivity() instanceof MainActivity) ((MainActivity) getActivity()).dismissLoadingDialog("趋势图dismiss");
                        mIsFirstLoad = false;
                    } else {
                        if (mLoadingDialog != null) mLoadingDialog.dismiss();
                    }
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTrendPresenterImp = new TrendPresenterImp(this);
        mLimit = 50;// 默认100
        mIsBigChart = false;// 默认小图
    }

    private View rootView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_trend, null);
        }
        //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
//        getChart();
    }

    public void getChart() {
        if (AppPrefs.getInstance().getGameListJson() == null) {
            ((MainActivity) getActivity()).getUpdateAppInfo();// 如果没有游戏列表，将认为是没有网络导致的，所以从源头开始获取一次网络信息
            return;
        }
        WebSettings settings = mWebView.getRefreshableView().getSettings();
        settings.setSupportZoom(!mIsBigChart);
        settings.setBuiltInZoomControls(!mIsBigChart);
        settings.setDisplayZoomControls(mIsBigChart);

        if (mIsFirstLoad) {
            if (getActivity() instanceof MainActivity) ((MainActivity) getActivity()).showLoadingDialog("趋势图show");
        } else {
            mLoadingDialog = MyDialog.createLoadingDialog(getActivity(), false);
            mLoadingDialog.show();
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        mGameId = AppPrefs.getInstance().getSelectedGame().getId();
        hashMap.put("gameId", mGameId + "");
        hashMap.put("limit", mLimit + "");
        mTrendPresenterImp.getChart(StringUtil.getJson(hashMap), mIsBigChart);
    }

    /**
     * 初始化组件
     */
    private void initViews() {
        // ******MyActionBar
        mMyActionBar = (MyActionBar) rootView.findViewById(R.id.actionBar);
        mMyActionBar.setBarBackGround(R.color.main_background);
        setTitleName();
        mMyActionBar.setBarLeftImage(mIsBigChart ? R.drawable.ic_trend_actionbar_left_small : R.drawable.ic_trend_actionbar_left_big);
        mMyActionBar.setBarRightImage(R.drawable.ic_trend_actionbar_right);
        mMyActionBar.setOnActionBarClickListener(this);

        // ******WebView
        mWebView = (PullToRefreshWebView) rootView.findViewById(R.id.trend_webview);
        mWebView.setMode(PullToRefreshBase.Mode.DISABLED);
        WebSettings settings = mWebView.getRefreshableView().getSettings();
        // 不不使用缓存
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //自适应屏幕
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        // 设置交互
        settings.setJavaScriptEnabled(true);
        // 设置缩放 
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        mWebView.setOnRefreshListener(mListener);
    }

    /**
     * 刷新监听事件
     */
    private PullToRefreshBase.OnRefreshListener2<WebView> mListener = new PullToRefreshBase.OnRefreshListener2<WebView>() {
        @Override
        public void onPullDownToRefresh(PullToRefreshBase<WebView> refreshView) {
            getChart();
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<WebView> refreshView) {

        }
    };

    @Override
    public void onActionBarLeftClicked() {
        // 大图还是小图
        mIsBigChart = !mIsBigChart;
        getChart();
        // 改变图标大小
        mMyActionBar.setBarLeftImage(mIsBigChart ? R.drawable.ic_trend_actionbar_left_small : R.drawable.ic_trend_actionbar_left_big);
    }

    @Override
    public void onActionBarRightClicked() {
        // 选择期数
        new MyDialog(getActivity())
                .setTitleText(getResources().getString(R.string.trend_dialog_title))
                .setLeftText(getResources().getString(R.string.app_cancel))
                .setRightText(getResources().getString(R.string.trend_dialog_right_btn))
                .setLimit(mLimit)
                .listener(new MyDialog.OnDialogClickListener2() {
                    @Override
                    public void onRightClicked(int limit) {
                        mLimit = limit;
                        getChart();
                    }
                }).show();

    }

    // --------------------TrendViewInf method---------------------

    @Override
    public void sendGetChartMessage(Message msg) {
        if (!isAdded()) {
            return;
        }
        mWebView.onRefreshComplete();
        // 延时3秒关闭对话框，选人数据需要时间
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(100);
                    mHandler.sendEmptyMessage(DISMISS_DIALOG);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        switch (msg.what) {
            case HandlerConfig.WHAT_POST_SUCCESS:
                mWebView.getRefreshableView().getSettings().setDefaultTextEncodingName("UTF-8");
                // 需要加载两遍，book   未找到原因
                mWebView.getRefreshableView().loadData((String) msg.obj, "text/html;charset=UTF-8", null);
                mWebView.getRefreshableView().loadData((String) msg.obj, "text/html;charset=UTF-8", null);
                break;
        }
    }

    /**
     * 更新相关数据
     */
    public void initData() {
        mWebView.getRefreshableView().loadData("切换到" + (mIsBigChart ? "小" : "大") + "图刷新", "text/html;charset=UTF-8", null);

        GameListModel.GameListData data = AppPrefs.getInstance().getSelectedGame();
        if (data != null) {
            mGameId = AppPrefs.getInstance().getSelectedGame().getId();
            mMyActionBar.setBarTitleText(data.getGameName() + App.getAppContext().getResources().getString(R.string.bottom_tab_trend));
            mIsFirstLoad = true;
            getChart();
        }
    }

    public static void setTitleName() {
        GameListModel.GameListData data = AppPrefs.getInstance().getSelectedGame();
        if (data != null) {
            mMyActionBar.setBarTitleText(data.getGameName() + App.getAppContext().getResources().getString(R.string.bottom_tab_trend));
        }
    }
}
