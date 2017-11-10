package com.qp.app_new.activitys;

import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.qp.app_new.R;

import org.json.JSONObject;

/**
 * Created by Aaron on 17/11/11.
 */

public class GameActivity extends BaseActivity {
    @Override
    public void setContentView () {
        layoutId = R.layout.activity_game_item;
    }

    private JSONObject mGameJSONObject;
    private PullToRefreshListView mListView;

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

        mListView = (PullToRefreshListView) findViewById (R.id.lv_game);
        mListView.setMode (PullToRefreshBase.Mode.BOTH);
        mListView.setOnRefreshListener (mListener);// 设置刷新的事件
    }

    private PullToRefreshBase.OnRefreshListener2<ListView> mListener = new PullToRefreshBase.OnRefreshListener2<ListView>() {
        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//            if (AppPrefs.getInstance().getGameListJson() == null) {
//                ((MainActivity) getActivity()).getUpdateAppInfo();// 如果没有游戏列表，将认为是没有网络导致的，所以从源头开始获取一次网络信息
                mListView.onRefreshComplete();
//                return;
//            }
//            mType = 0;
//            if (mHistoryLotteryDatas == null || mHistoryLotteryDatas.size() == 0) mType = 0;
//            getAllData(true);
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//            if (AppPrefs.getInstance().getGameListJson() == null) {
//                ((MainActivity) getActivity()).getUpdateAppInfo();// 如果没有游戏列表，将认为是没有网络导致的，所以从源头开始获取一次网络信息
                mListView.onRefreshComplete();
//                return;
//            }
//            mType = -1;
//            if (mHistoryLotteryDatas == null || mHistoryLotteryDatas.size() == 0) mType = 0;
//            getHistoryLotteryList(true);
        }
    };
}
