package com.teb.kilimanjaro.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.teb.kilimanjaro.App;
import com.teb.kilimanjaro.AppPrefs;
import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.activitys.MainActivity;
import com.teb.kilimanjaro.activitys.hall.RevenueActivity;
import com.teb.kilimanjaro.activitys.qq.BettingActivityQQ;
import com.teb.kilimanjaro.activitys.qq.BettingListActivityQQ;
import com.teb.kilimanjaro.activitys.qq.LotteryDefaultActivityQQ;
import com.teb.kilimanjaro.activitys.qq.PayingMethodActivityQQ;
import com.teb.kilimanjaro.adapters.HallLotteryAdapter;
import com.teb.kilimanjaro.configs.HandlerConfig;
import com.teb.kilimanjaro.models.entry.hall.GameListModel;
import com.teb.kilimanjaro.models.entry.hall.LotteryModel;
import com.teb.kilimanjaro.mvp.presenter.HallPresenterImp;
import com.teb.kilimanjaro.mvp.view.HallViewInf;
import com.teb.kilimanjaro.utils.AnimUtil;
import com.teb.kilimanjaro.utils.MyUtil;
import com.teb.kilimanjaro.utils.NetUtil;
import com.teb.kilimanjaro.utils.StringUtil;
import com.teb.kilimanjaro.utils.ToastUtil;
import com.teb.kilimanjaro.views.MyActionBar;
import com.teb.kilimanjaro.views.dialogs.ActionBarRightDialog;
import com.teb.kilimanjaro.views.dialogs.MyDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yangbin on 16/7/1.
 * 大厅 界面fragment
 */
public class HallFragment extends Fragment implements
        MyActionBar.OnActionBarClickListener,
        HallViewInf,
        ActionBarRightDialog.OnDialogActionBarRightItemClickListener,
        HallLotteryAdapter.OnItemClickListenerRaelTime,
        HallLotteryAdapter.OnItemClickListenerHistory {

    private static MyActionBar mMyActionBar;// ActionBar

    private TextView mGameCoinTV;// 游戏币
    private TextView mGameTypeTV;// 游戏类型（是否是模拟场）

    private List<LotteryModel.LotteryData> mRaelTimeLotteryDatas;

    private PullToRefreshListView mListView;
    private HallLotteryAdapter mLotteryAdapter;
    private List<LotteryModel.LotteryData> mHistoryLotteryDatas;

    private HallPresenterImp mHallPresenterImp;
    private Dialog mLoadingDialog;
    private boolean mIsFirstLoad = true;// 是否是第一次请求
    private int mRequestCount = 0;// 一次性请求的次数（用来控制dialog）

    private int mType = 0;// 0默认第一次请求,1下拉刷新,-1上拉加载更多

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHallPresenterImp = new HallPresenterImp(this);
        mLoadingDialog = MyDialog.createLoadingDialog(getActivity(), true);
    }

    private View rootView;//缓存Fragment view

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_hall, null);
        }
        //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        initViews();
        return rootView;
    }

    /**
     * 初始化组件
     */
    private void initViews() {
        // ******MyActionBar
        mMyActionBar = (MyActionBar) rootView.findViewById(R.id.actionBar);
        mMyActionBar.setBarBackGround(R.color.main_background);
        mMyActionBar.setBarLeftImage(R.drawable.ic_menu);
        mMyActionBar.setBarRightImage(R.drawable.ic_more);
        mMyActionBar.setOnActionBarClickListener(this);
        setTitleName();

        mGameCoinTV = (TextView) rootView.findViewById(R.id.tv_gamecoin);
        mGameTypeTV = (TextView) rootView.findViewById(R.id.tv_game_type);
        if (AppPrefs.getInstance().getSelectedGame() != null) setGameCoin();

        // ******ListView
        initLotteryListView();
    }

    /**
     * 设置余额
     */
    public void setGameCoin() {
        if (AppPrefs.getInstance().getSelectedGame().isSimulate()) {
            mGameTypeTV.setText(R.string.gamescore);
            mGameCoinTV.setText(AppPrefs.getInstance().getGameScoreLong() + "");
        } else {
            mGameTypeTV.setText(R.string.gamecoin);
            mGameCoinTV.setText(AppPrefs.getInstance().getGameCoinLong() + "");
        }
    }

    /**
     * 初始化listview   并获取数据列表
     */
    private void initLotteryListView() {
        mListView = (PullToRefreshListView) rootView.findViewById(R.id.lv_hall);
        mListView.setMode(PullToRefreshBase.Mode.BOTH);
        mLotteryAdapter = new HallLotteryAdapter();
        mListView.setAdapter(mLotteryAdapter);
        mListView.setOnRefreshListener(mListener);// 设置刷新的事件
        mLotteryAdapter.setListenerRaelTime(this);
        mLotteryAdapter.setListenerHistory(this);
    }

    private PullToRefreshBase.OnRefreshListener2<ListView> mListener = new PullToRefreshBase.OnRefreshListener2<ListView>() {
        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            if (AppPrefs.getInstance().getGameListJson() == null) {
                ((MainActivity) getActivity()).getUpdateAppInfo();// 如果没有游戏列表，将认为是没有网络导致的，所以从源头开始获取一次网络信息
                mListView.onRefreshComplete();
                return;
            }
            mType = 0;
            if (mHistoryLotteryDatas == null || mHistoryLotteryDatas.size() == 0) mType = 0;
            getAllData(true);
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            if (AppPrefs.getInstance().getGameListJson() == null) {
                ((MainActivity) getActivity()).getUpdateAppInfo();// 如果没有游戏列表，将认为是没有网络导致的，所以从源头开始获取一次网络信息
                mListView.onRefreshComplete();
                return;
            }
            mType = -1;
            if (mHistoryLotteryDatas == null || mHistoryLotteryDatas.size() == 0) mType = 0;
            getHistoryLotteryList(true);
        }
    };

    @Override
    public void onActionBarLeftClicked() {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.openDrawerLayout();
    }

    @Override
    public void onActionBarRightClicked() {
        new ActionBarRightDialog(getActivity())
                .listener(this)
                .show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 刷新  (供MainActivity使用)
     */
    public void onRefresh(boolean isShowDialog) {
        mType = 0;
        getAllData(isShowDialog);
    }

    /**
     * 更新相关数据（由MainActivity使用）
     */
    public void initData() {
        // 清空数据，防止：当从北京28切换到加拿大28游戏时，网络不好导致加载失败，如果下拉刷新继续加载将会出现加拿大28和北京28的数据同时出现
        if (mRaelTimeLotteryDatas != null) mRaelTimeLotteryDatas.clear();
        if (mHistoryLotteryDatas != null) mHistoryLotteryDatas.clear();

        GameListModel.GameListData data = AppPrefs.getInstance().getSelectedGame();
        mMyActionBar.setBarTitleText(data.getGameName() + App.getAppContext().getResources().getString(R.string.bottom_tab_hall));
        mType = 0;
        mIsFirstLoad = true;
        getAllData(true);// 此处true OR false   无所谓
    }

    public static void setTitleName() {
        GameListModel.GameListData data = AppPrefs.getInstance().getSelectedGame();
        if (data != null) {
            mMyActionBar.setBarTitleText(data.getGameName() + App.getAppContext().getResources().getString(R.string.bottom_tab_hall));
        }
    }


    @Override
    public void onItemClickHistory(LotteryModel.LotteryData data) {
//        Intent intent = new Intent(getActivity(), LotteryDefaultActivity.class);
        Intent intent = new Intent(getActivity(), LotteryDefaultActivityQQ.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", data);
        intent.putExtras(bundle);
        MyUtil.startActivity(getActivity(), intent);
    }

    @Override
    public void onItemClickRaelTime(View view, LotteryModel.LotteryData data) {
        switch (view.getId()) {
            case R.id.tv_time:// 刷新
                onRefresh(true);
                break;
            case R.id.btn_betting:// 进入投注
                Intent intent = null;
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", data);

                // 根据投注的订单数判断
                if (data.getBetNum() > 0) {// 有订单
                    intent = new Intent(getActivity(), BettingListActivityQQ.class);
                } else {// 无订单
                    intent = new Intent(getActivity(), BettingActivityQQ.class);
                }
                intent.putExtras(bundle);
                MyUtil.startActivity(getActivity(), intent);
                break;
        }
    }

    /**
     * 获取积分
     *
     * @param isShowDialog
     */
    public void getGameCoin(boolean isShowDialog) {
        mRequestCount++;
        if (mIsFirstLoad) {
            if (getActivity() instanceof MainActivity) ((MainActivity) getActivity()).showLoadingDialog("获取游戏币余额show");
        } else {
            if (isShowDialog) {
                if (mRequestCount == 1) mLoadingDialog.show();// 只有自己一个请求时打开dailog
            }
        }
        mHallPresenterImp.getGameCoin(StringUtil.getJson(new HashMap<String, Object>()));
    }

    /**
     * 获取历史数据列表
     */
    private void getHistoryLotteryList(boolean isShowDialog) {
        mRequestCount++;
        if (mIsFirstLoad) {
            if (getActivity() instanceof MainActivity) ((MainActivity) getActivity()).showLoadingDialog("历史开奖信息show");
        } else {
            if (isShowDialog) {
                if (mRequestCount == 1) mLoadingDialog.show();// 只有自己一个请求时打开dailog
            }
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("gameId", AppPrefs.getInstance().getSelectedGame().getId());
        hashMap.put("pageSize", 10);// 每次获取数据的数量
        hashMap.put("type", mType);// 1 下拉刷新   0第一次请求   -1上拉加载
        hashMap.put("id", 1);
        switch (mType) {
            case 1:
                // 取最新的一条数据
                if (mHistoryLotteryDatas == null) {
                    return;
                }
                hashMap.put("id", mHistoryLotteryDatas.get(0).getId());
                break;
            case -1:
                // 取最后一条数据
                hashMap.put("id", mHistoryLotteryDatas.get(mHistoryLotteryDatas.size() - 1).getId());
                break;
        }
        mHallPresenterImp.getHistoryLotteryList(StringUtil.getJson(hashMap));
    }

    /**
     * 获取未开奖数据列表
     *
     * @param isShowDialog
     */
    private void getNoLotteryList(boolean isShowDialog) {
        mRequestCount++;
        if (mIsFirstLoad) {
            if (getActivity() instanceof MainActivity) ((MainActivity) getActivity()).showLoadingDialog("未开奖信息show");
        } else {
            if (isShowDialog) {
                if (mRequestCount == 1) mLoadingDialog.show();// 只有自己一个请求时打开dailog
            }
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("gameId", AppPrefs.getInstance().getSelectedGame().getId());
        mHallPresenterImp.getNoLotteryList(StringUtil.getJson(hashMap));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mLotteryAdapter != null) mLotteryAdapter.removeRunnable();
    }

    /**
     * 供内部使用
     *
     * @param isShowDialog
     */
    private void getAllData(boolean isShowDialog) {
        if (AppPrefs.getInstance().getTokenState()) {
            getGameCoin(isShowDialog);
            getHistoryLotteryList(isShowDialog);
            getNoLotteryList(isShowDialog);
        }
    }

    @Override
    public void onActionBarRightItemClicked(int position) {
        switch (position) {
            case ActionBarRightDialog.GOTO_BET_AUTO:// 自动投注
                MyDialog.showWaitDialog(getActivity());
//                startActivity(new Intent(getActivity(), BetAutoActivityQQ.class));
                break;
            case ActionBarRightDialog.GOTO_MANAGE_MODE:// 模式管理
                MyDialog.showWaitDialog(getActivity());
//                startActivity(new Intent(getActivity(), ModeManageActivity.class));
                break;
            case ActionBarRightDialog.GOTO_PLAYING_METHOD:// 玩法介绍
                if (!NetUtil.checkNet()) {
                    ToastUtil.showToast(R.string.have_not_net);
                    return;
                }
                startActivity(new Intent(getActivity(), PayingMethodActivityQQ.class));
                break;
            case ActionBarRightDialog.GOTO_REVENUE:// 我的亏盈
                startActivity(new Intent(getActivity(), RevenueActivity.class));
                break;
        }
        AnimUtil.actFromRightToLeft(getActivity());
    }

    // --------------------HallViewInf method---------------------

    @Override
    public void sendGameCoinMessage(Message msg) {
        if (!isAdded()) {
            return;
        }
        if (mIsFirstLoad) {
            if (getActivity() instanceof MainActivity) ((MainActivity) getActivity()).dismissLoadingDialog("获取游戏币余额dismiss");
            mRequestCount--;
            if (mRequestCount == 0) mIsFirstLoad = false;
        } else {
            if (mLoadingDialog != null) mLoadingDialog.dismiss();
        }
        mListView.onRefreshComplete();
        switch (msg.what) {
            case HandlerConfig.WHAT_POST_SUCCESS:// 设置积分
                ((MainActivity) getActivity()).setFragmentGameCoin();
                break;
            default:
                MyUtil.handMessage1(getActivity(), msg, "HallFragment---GameCoin>>>");
                break;
        }
    }

    @Override
    public void sendHistoryLotteryMessage(Message msg) {
        if (!isAdded()) {
            return;
        }
        if (mIsFirstLoad) {
            if (getActivity() instanceof MainActivity) ((MainActivity) getActivity()).dismissLoadingDialog("历史开奖信息dismiss");
            mRequestCount--;
            if (mRequestCount == 0) mIsFirstLoad = false;
        } else {
            if (mLoadingDialog != null) mLoadingDialog.dismiss();
        }
        mListView.onRefreshComplete();
        switch (msg.what) {
            case HandlerConfig.WHAT_POST_SUCCESS:
                List<LotteryModel.LotteryData> tempList = (List<LotteryModel.LotteryData>) msg.obj;
                switch (mType) {
                    case 1:
                        if (mHistoryLotteryDatas == null) mHistoryLotteryDatas = new ArrayList<>();
                        mHistoryLotteryDatas.addAll(0, tempList);
                        break;
                    case 0:
                        mHistoryLotteryDatas = tempList;
                        break;
                    case -1:
                        if (mHistoryLotteryDatas == null) mHistoryLotteryDatas = new ArrayList<>();
                        mHistoryLotteryDatas.addAll(mHistoryLotteryDatas.size(), tempList);
                        break;
                }
                // 保存最新的一条开奖数据（自动投注使用）
                if (mHistoryLotteryDatas != null && mHistoryLotteryDatas.size() > 0) {
                    AppPrefs.getInstance().saveNewBetInfo(new Gson().toJson(mHistoryLotteryDatas.get(0)));
                }
                mLotteryAdapter.setHadList(mHistoryLotteryDatas);
                break;
            default:
                MyUtil.handMessage1(getActivity(), msg, "HallFragment---HistoryList>>>");
                break;
        }
    }

    @Override
    public void sendNoLotteryMessage(Message msg) {
        if (!isAdded()) {
            return;
        }
        if (mIsFirstLoad) {
            if (getActivity() instanceof MainActivity) ((MainActivity) getActivity()).dismissLoadingDialog("未开奖信息dismiss");
            mRequestCount--;
            if (mRequestCount == 0) mIsFirstLoad = false;
        } else {
            if (mLoadingDialog != null) mLoadingDialog.dismiss();
        }
        mListView.onRefreshComplete();
        switch (msg.what) {
            case HandlerConfig.WHAT_POST_SUCCESS:
                mRaelTimeLotteryDatas = (List<LotteryModel.LotteryData>) msg.obj;
                mLotteryAdapter.setNoList(mRaelTimeLotteryDatas);
                break;
            default:
                MyUtil.handMessage1(getActivity(), msg, "HallFragment---NewList>>>");
                break;
        }
    }
}
