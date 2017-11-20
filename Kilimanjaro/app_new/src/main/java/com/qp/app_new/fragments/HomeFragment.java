package com.qp.app_new.fragments;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.qp.app_new.App;
import com.qp.app_new.AppPrefs;
import com.qp.app_new.R;
import com.qp.app_new.adapters.GameListAdapter;
import com.qp.app_new.dialogs.DialogHelp;
import com.qp.app_new.httpnetworks.NetWorkManager;
import com.qp.app_new.interfaces.NetListener;
import com.qp.app_new.utils.ActivityStartUtils;

import org.json.JSONArray;

/**
 * Created by Aaron on 17/11/7.
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener {

    @Override
    public int getContentView() {
        return R.layout.fragment_home;
    }

    private ListView mGameListLV;
    private GameListAdapter mGameListAdapter;

    @Override
    public void initView() {
//        initActionBar ();
//        setTitle (R.string.bottom_tab_home);
//        setRightIV (R.drawable.ic_menu);

        findViewById(R.id.qq_btn).setOnClickListener(this);
        findViewById(R.id.wx_btn).setOnClickListener(this);

        mGameListLV = (ListView) findViewById(R.id.game_list_lv);
        mGameListAdapter = new GameListAdapter();
        mGameListLV.setAdapter(mGameListAdapter);

        mGameListLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (judgeLogin()) {
                    // 去游戏页面
                    App.currentGameJsonObject = mGameListAdapter.getItem(position);
                    ActivityStartUtils.startGameActivity(getActivity(), mGameListAdapter.getItem(position));
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        updateData();
    }

    public void updateData() {
        if (mGameListAdapter.getCount() == 0) {
            getGameList();
        }
    }

    // *********************************

    /**
     * 获取游戏列表
     */
    private void getGameList() {
        NetWorkManager.getInstance().getGameList(mLoadingDialog, new NetListener() {
            @Override
            public void onSuccessResponse(String msg, JSONArray jsonArray) {
                super.onSuccessResponse(msg, jsonArray);
                mGameListAdapter.setJSONArray(jsonArray);
            }

            @Override
            public void onErrorResponse(int errorWhat, String message) {
            }
        });
    }

//    @Override
//    public void onRightClick (View v) {
//        if (!mPopupWindow.isShowing ()) {
//            mPopupWindow.showAsDropDown (v, 0, 0);
//        }
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qq_btn:
                DialogHelp.showKefuQQDialog(getActivity());
                break;
            case R.id.wx_btn:
                final String kefu_wx_default = AppPrefs.getInstance().getSysInfoWX();
                DialogHelp.showMessageDialog(getActivity(), getString(R.string.kefu_wx_, kefu_wx_default));
                break;
        }
    }
}
