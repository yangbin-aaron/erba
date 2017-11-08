package com.qp.app_new.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.qp.app_new.R;
import com.qp.app_new.adapters.GameListAdapter;
import com.qp.app_new.dialogs.DialogHelp;
import com.qp.app_new.httpnetworks.NetWorkManager;
import com.qp.app_new.interfaces.NetListener;
import com.qp.app_new.interfaces.NormalDialogListener1;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Aaron on 17/11/7.
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener {

    @Override
    public void setContentView() {
        layoutId = R.layout.fragment_home;
    }

    private ListView mGameListLV;
    private GameListAdapter mGameListAdapter;

    @Override
    public void initView() {
        findViewById(R.id.qq_btn).setOnClickListener(this);

        mGameListLV = (ListView) findViewById(R.id.game_list_lv);
        mGameListAdapter = new GameListAdapter();
        mGameListLV.setAdapter(mGameListAdapter);

        mGameListLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("HomeFragment", "setOnItemClickListener >> " + mGameListAdapter.getItem(position).toString());
                DialogHelp.showMessageDialog(getActivity(), mGameListAdapter.getItem(position).optString("gameName"));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
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

        try {// 测试数据
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("gameName", "北京28");
            jsonArray.put(jsonObject);
            jsonObject = new JSONObject();
            jsonObject.put("gameName", "加拿大28");
            jsonArray.put(jsonObject);
            mGameListAdapter.setJSONArray(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Dialog mDialogQQ;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qq_btn:
                // 先登录

                final String kefu_qq_num_default = getString(R.string.kefu_qq_num_default);
                String call_kefu_qq = getString(R.string.call_kefu_qq, kefu_qq_num_default);
                if (mDialogQQ == null) {
                    mDialogQQ = DialogHelp.createOkDialog(getActivity(), call_kefu_qq, new NormalDialogListener1() {
                        @Override
                        public void onOkClickListener() {
                            String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + kefu_qq_num_default;
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                        }
                    });
                }
                mDialogQQ.show();
                break;
        }
    }
}
