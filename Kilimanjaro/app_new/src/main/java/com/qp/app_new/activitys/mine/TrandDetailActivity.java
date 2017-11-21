package com.qp.app_new.activitys.mine;

import android.view.View;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.qp.app_new.R;
import com.qp.app_new.activitys.BaseActivity;
import com.qp.app_new.adapters.TrandBetAdapter;
import com.qp.app_new.adapters.TrandCoinAdapter;
import com.qp.app_new.configs.NetStatusConfig;
import com.qp.app_new.dialogs.DialogHelp;
import com.qp.app_new.httpnetworks.NetWorkManager;
import com.qp.app_new.interfaces.NetListener;
import com.qp.app_new.interfaces.PopupWindowItemListener;
import com.qp.app_new.utils.StringUtil;

import org.json.JSONArray;

import java.util.HashMap;

/**
 * Created by Aaron on 2017/11/21.
 */

public class TrandDetailActivity extends BaseActivity {

    @Override
    public int getContentView() {
        return R.layout.activity_trande_detail;
    }

    private PopupWindow mPopupWindow;

    private PullToRefreshListView mListViewCoin, mListViewBet;
    private TrandCoinAdapter mCoinAdapter;
    private TrandBetAdapter mBetAdapter;
    private int mPageNumCoin, mPageNumBet;
    private JSONArray mJSONArrayCoin, mJSONArrayBet;

    @Override
    public void initView() {
        initActionBar();
        setLeftIV(R.drawable.ic_back_btn);
        setRightIV(R.drawable.ic_menu);
        final String[] popList = {getString(R.string.trand_coin), getString(R.string.trand_bet)};
        setTitle(popList[0]);
        mPopupWindow = DialogHelp.createPopupWindow(this, popList, new PopupWindowItemListener() {
            @Override
            public void onPopItemClick(View v, int position) {
                changeListView(position);
                setTitle(popList[position]);
                mPopupWindow.dismiss();
            }
        });

        mListViewCoin = (PullToRefreshListView) findViewById(R.id.lv_trand_coin);
        mListViewCoin.setMode(PullToRefreshBase.Mode.BOTH);
        mListViewCoin.setOnRefreshListener(mListenerCoin);// 设置刷新的事件
        mCoinAdapter = new TrandCoinAdapter();
        mListViewCoin.setAdapter(mCoinAdapter);
        mPageNumCoin = 1;
        mJSONArrayCoin = new JSONArray();

        mListViewBet = (PullToRefreshListView) findViewById(R.id.lv_trand_bet);
        mListViewBet.setMode(PullToRefreshBase.Mode.BOTH);
        mListViewBet.setOnRefreshListener(mListenerBet);// 设置刷新的事件
        mBetAdapter = new TrandBetAdapter();
        mListViewBet.setAdapter(mBetAdapter);
        mPageNumBet = 1;
        mJSONArrayBet = new JSONArray();
//        mListViewBet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                try {
//                    JSONObject jsonObject = mBetAdapter.getItem(position);
//                    JSONObject gameObject = new JSONObject();
//                    gameObject.put("id", jsonObject.optString("gameId"));
//                    gameObject.put("gameName", jsonObject.optString("gameName"));
//
//                    JSONObject lotteryObject = new JSONObject();
//                    lotteryObject.put("id", "");
//                    ActivityStartUtils.startLotteryDetailActivity(TrandDetailActivity.this, gameObject, lotteryObject);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

        changeListView(0);
    }

    private void changeListView(int type) {
        switch (type) {
            case 0:
                mListViewCoin.setVisibility(View.VISIBLE);
                mListViewBet.setVisibility(View.INVISIBLE);
                if (mJSONArrayCoin.length() == 0) {
                    getCoinList();
                }
                break;
            case 1:
                mListViewCoin.setVisibility(View.INVISIBLE);
                mListViewBet.setVisibility(View.VISIBLE);
                if (mJSONArrayBet.length() == 0) {
                    getBetList();
                }
                break;
        }
    }

    private PullToRefreshBase.OnRefreshListener2<ListView> mListenerCoin = new PullToRefreshBase.OnRefreshListener2<ListView>() {
        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            mPageNumCoin = 1;
            mJSONArrayCoin = new JSONArray();
            mListViewCoin.setMode(PullToRefreshBase.Mode.BOTH);
            getCoinList();
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            getCoinList();
        }
    };

    private PullToRefreshBase.OnRefreshListener2<ListView> mListenerBet = new PullToRefreshBase.OnRefreshListener2<ListView>() {
        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            mPageNumBet = 1;
            mJSONArrayBet = new JSONArray();
            mListViewBet.setMode(PullToRefreshBase.Mode.BOTH);
            getBetList();
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            getBetList();
        }
    };

    private void getCoinList() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("pageNum", mPageNumCoin);// 页码
        hashMap.put("pageSize", 10);// 每次获取数据的数量
        NetWorkManager.getInstance().getReChargesList(StringUtil.getJson(hashMap), mLoadingDialog, new NetListener() {
            @Override
            public void onSuccessResponse(String msg, JSONArray jsonArray) {
                super.onSuccessResponse(msg, jsonArray);
                mListViewCoin.onRefreshComplete();
                mPageNumCoin++;
                for (int i = 0; i < jsonArray.length(); i++) {
                    mJSONArrayCoin.put(jsonArray.optJSONObject(i));
                }

                mCoinAdapter.setJSONArray(mJSONArrayCoin);
            }

            @Override
            public void onErrorResponse(int errorWhat, String message) {
                super.onErrorResponse(errorWhat, message);
                mListViewCoin.onRefreshComplete();
                if (errorWhat == NetStatusConfig.STATUS_HAVE_NO_DATA) {
                    mListViewCoin.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
            }
        });
    }

    private void getBetList() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("pageNum", mPageNumBet);// 页码
        hashMap.put("pageSize", 10);// 每次获取数据的数量
        NetWorkManager.getInstance().getOrdersList(StringUtil.getJson(hashMap), mLoadingDialog, new NetListener() {
            @Override
            public void onSuccessResponse(String msg, JSONArray jsonArray) {
                super.onSuccessResponse(msg, jsonArray);
                mListViewBet.onRefreshComplete();
                mPageNumBet++;
                for (int i = 0; i < jsonArray.length(); i++) {
                    mJSONArrayBet.put(jsonArray.optJSONObject(i));
                }

                mBetAdapter.setJSONArray(mJSONArrayBet);
            }

            @Override
            public void onErrorResponse(int errorWhat, String message) {
                super.onErrorResponse(errorWhat, message);
                mListViewBet.onRefreshComplete();
                if (errorWhat == NetStatusConfig.STATUS_HAVE_NO_DATA) {
                    mListViewBet.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
            }
        });
    }

    @Override
    public void onRightClick(View v) {
        super.onRightClick(v);
        if (!mPopupWindow.isShowing()) mPopupWindow.showAsDropDown(v, 0, 0);
    }
}
