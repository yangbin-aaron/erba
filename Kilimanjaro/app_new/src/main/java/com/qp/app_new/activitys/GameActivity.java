package com.qp.app_new.activitys;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.qp.app_new.AppPrefs;
import com.qp.app_new.R;
import com.qp.app_new.adapters.HallLotteryAdapter;
import com.qp.app_new.configs.BroadcastConfig;
import com.qp.app_new.configs.NetStatusConfig;
import com.qp.app_new.dialogs.DialogHelp;
import com.qp.app_new.httpnetworks.NetWorkManager;
import com.qp.app_new.interfaces.NetListener;
import com.qp.app_new.interfaces.PopupWindowItemListener;
import com.qp.app_new.utils.ActivityStartUtils;
import com.qp.app_new.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Aaron on 17/11/11.
 */

public class GameActivity extends BaseActivity implements HallLotteryAdapter.OnItemClickListenerRaelTime, HallLotteryAdapter
        .OnItemClickListenerHistory {
    @Override
    public int getContentView () {
        return R.layout.activity_game_item;
    }

    private PopupWindow mPopupWindow;

    private JSONObject mGameJSONObject;
    private PullToRefreshListView mListView;
    private HallLotteryAdapter mLotteryAdapter;
    private JSONArray mHistoryLotteryDatas;

    private Dialog mDialog;

    private int mType = 0;// 0默认第一次请求,1下拉刷新,-1上拉加载更多

    @Override
    public void getIntentData () {
        super.getIntentData ();
        String gameJson = getIntent ().getStringExtra ("gameJson");
        try {
            mGameJSONObject = new JSONObject (gameJson);
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }

    @Override
    public void initView () {
        initActionBar ();
        setTitle (mGameJSONObject.optString ("gameName"));
        setLeftIV (R.drawable.ic_back_btn);
        setRightIV (R.drawable.ic_menu);

        final String[] popList = {getString (R.string.hall_paying_method), getString (R.string.revenue_bar_base)};
        mPopupWindow = DialogHelp.createPopupWindow (this, popList, new PopupWindowItemListener () {
            @Override
            public void onPopItemClick (View v, int position) {
                DialogHelp.showMessageDialog (GameActivity.this, getString (R.string.app_please_wait));
                mPopupWindow.dismiss ();
            }
        });

        mListView = (PullToRefreshListView) findViewById (R.id.lv_game);
        mListView.setMode (PullToRefreshBase.Mode.BOTH);
        mListView.setOnRefreshListener (mListener);// 设置刷新的事件
        mLotteryAdapter = new HallLotteryAdapter (mGameJSONObject.optInt ("stopBetSecond"));
        mListView.setAdapter (mLotteryAdapter);
        mListView.setOnRefreshListener (mListener);// 设置刷新的事件
        mLotteryAdapter.setListenerRaelTime (this);
        mLotteryAdapter.setListenerHistory (this);

        initData ();
    }

    @Override
    public void addFilterAction (IntentFilter filter) {
        super.addFilterAction (filter);
        filter.addAction (BroadcastConfig.ACTION_REFRESH_GAMELIST);
    }

    @Override
    public void handlerBroadcastReceiver (Intent intent) {
        super.handlerBroadcastReceiver (intent);
        if (intent.getAction ().equals (BroadcastConfig.ACTION_REFRESH_GAMELIST)) {
            mDialog = null;
            getAllData ();
        }
    }

    private PullToRefreshBase.OnRefreshListener2<ListView> mListener = new PullToRefreshBase.OnRefreshListener2<ListView> () {
        @Override
        public void onPullDownToRefresh (PullToRefreshBase<ListView> refreshView) {
            mType = 0;
            if (mHistoryLotteryDatas == null || mHistoryLotteryDatas.length () == 0) mType = 0;
            mDialog = mLoadingDialog;
            getAllData ();
        }

        @Override
        public void onPullUpToRefresh (PullToRefreshBase<ListView> refreshView) {
            mType = -1;
            if (mHistoryLotteryDatas == null || mHistoryLotteryDatas.length () == 0) mType = 0;
            getHistoryLotteryList ();
        }
    };

    @Override
    public void onItemClickRaelTime (View view, JSONObject data) {
        switch (view.getId ()) {
            case R.id.tv_time:// 刷新
                mType = 0;
                mDialog = mLoadingDialog;
                getAllData ();
                break;
            case R.id.btn_betting:// 进入投注
                if (data.optInt ("betNum") > 0) {// 有订单
                    ActivityStartUtils.startBetedListActivity (this, mGameJSONObject, data);
                } else {// 无订单
                    ActivityStartUtils.startBetActivity (this, mGameJSONObject, data);
                }
                break;
        }
    }

    @Override
    public void onItemClickHistory (JSONObject data) {// 查看本期详情
        ActivityStartUtils.startLotteryDetailActivity (this, mGameJSONObject, data);
    }

    private void initData () {
        mHistoryLotteryDatas = new JSONArray ();
        mType = 0;
        mDialog = mLoadingDialog;
        getAllData ();
    }

    private void getAllData () {
        getGameCoin ();
        getHistoryLotteryList ();
        getNoLotteryList ();
    }

    /**
     * 获取历史数据列表
     */
    private void getHistoryLotteryList () {
        HashMap<String, Object> hashMap = new HashMap<> ();
        hashMap.put ("gameId", mGameJSONObject.optString ("id"));
        hashMap.put ("pageSize", 10);// 每次获取数据的数量
        hashMap.put ("type", mType);// 1 下拉刷新   0第一次请求   -1上拉加载
        hashMap.put ("id", 1);
        switch (mType) {
            case 1:
                // 取最新的一条数据
                if (mHistoryLotteryDatas == null) {
                    return;
                }
                hashMap.put ("id", mHistoryLotteryDatas.optJSONObject (0).optString ("id"));
                break;
            case -1:
                // 取最后一条数据
                hashMap.put ("id", mHistoryLotteryDatas.optJSONObject (mHistoryLotteryDatas.length () - 1).optString ("id"));
                break;
        }
        NetWorkManager.getInstance ().getHistoryLotteryList (StringUtil.getJson (hashMap), mDialog, new NetListener () {
            @Override
            public void onSuccessResponse (String msg, JSONArray jsonArray) {
                super.onSuccessResponse (msg, jsonArray);
                mListView.onRefreshComplete ();
                if (jsonArray == null) return;
                switch (mType) {
                    case 1:
                        if (mHistoryLotteryDatas == null) mHistoryLotteryDatas = new JSONArray ();
                        for (int i = 0; i < jsonArray.length (); i++) {
                            JSONObject object1 = jsonArray.optJSONObject (i);
                            try {
                                mHistoryLotteryDatas.put (i, object1);
                            } catch (JSONException e) {
                                e.printStackTrace ();
                            }
                        }
                        break;
                    case 0:
                        mHistoryLotteryDatas = jsonArray;
                        break;
                    case -1:
                        if (mHistoryLotteryDatas == null) mHistoryLotteryDatas = new JSONArray ();
                        for (int i = 0; i < jsonArray.length (); i++) {
                            JSONObject object1 = jsonArray.optJSONObject (i);
                            mHistoryLotteryDatas.put (object1);
                        }
                        break;
                }
                // 保存最新的一条开奖数据（自动投注使用）
//                if (mHistoryLotteryDatas != null && mHistoryLotteryDatas.size() > 0) {
//                    AppPrefs.getInstance().saveNewBetInfo(new Gson().toJson(mHistoryLotteryDatas.get(0)));
//                }
                mLotteryAdapter.setHadList (mHistoryLotteryDatas);
            }

            @Override
            public void onErrorResponse (int errorWhat, String message) {
                if (errorWhat != NetStatusConfig.STATUS_TOKEN_IS_UPDATED) {
                    super.onErrorResponse (errorWhat, message);
                }
            }
        });
    }

    /**
     * 获取未开奖数据列表
     */
    private void getNoLotteryList () {
        HashMap<String, Object> hashMap = new HashMap<> ();
        hashMap.put ("gameId", mGameJSONObject.optString ("id"));
        NetWorkManager.getInstance ().getNoLotteryList (StringUtil.getJson (hashMap), new NetListener () {
            @Override
            public void onSuccessResponse (String msg, JSONArray jsonArray) {
                super.onSuccessResponse (msg, jsonArray);
                mListView.onRefreshComplete ();
                if (jsonArray == null) return;
                // 保存当前期数ID(自动投注使用)
//                if (lotteryDatas.size() > 0) {
//                    AppPrefs.getInstance().saveCurrentBetInfo(lotteryDatas.get(lotteryDatas.size() - 1));
//                }
                // 做标记
                for (int i = 0; i < jsonArray.length (); i++) {
                    JSONObject jsonObject = jsonArray.optJSONObject (i);
                    try {
                        jsonObject.put ("isNoLottery", true);
                    } catch (JSONException e) {
                        e.printStackTrace ();
                    }
                }
                mLotteryAdapter.setNoList (jsonArray);
            }
        });
    }

    private void getGameCoin () {
        NetWorkManager.getInstance ().getGameCoin (new NetListener (this) {
            @Override
            public void onSuccessResponse (String msg, JSONObject jsonObject) {
                super.onSuccessResponse (msg, jsonObject);
                AppPrefs.getInstance ().saveGameCoin (jsonObject.optLong ("coin"));
            }

            @Override
            public void onErrorResponse (int errorWhat, String message) {
                if (errorWhat == NetStatusConfig.STATUS_TOKEN_IS_UPDATED)
                    super.onErrorResponse (errorWhat, message);
            }
        });
    }

    @Override
    public void onRightClick (View v) {
        if (!mPopupWindow.isShowing ()) {
            mPopupWindow.showAsDropDown (v, 0, 0);
        }
    }
}
