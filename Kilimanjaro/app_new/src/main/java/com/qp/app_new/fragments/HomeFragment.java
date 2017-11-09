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
import com.qp.app_new.contents.AppPrefsContent;
import com.qp.app_new.dialogs.DialogHelp;
import com.qp.app_new.httpnetworks.NetWorkManager;
import com.qp.app_new.interfaces.NetListener;
import com.qp.app_new.interfaces.NormalDialogListener1;

import org.json.JSONArray;

/**
 * Created by Aaron on 17/11/7.
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener {

    @Override
    public void setContentView () {
        layoutId = R.layout.fragment_home;
    }

    private ListView mGameListLV;
    private GameListAdapter mGameListAdapter;
    private Dialog mDialogQQ;

    @Override
    public void initView () {
        initActionBar ();

        findViewById (R.id.qq_btn).setOnClickListener (this);

        mGameListLV = (ListView) findViewById (R.id.game_list_lv);
        mGameListAdapter = new GameListAdapter ();
        mGameListLV.setAdapter (mGameListAdapter);

        mGameListLV.setOnItemClickListener (new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                Log.e ("HomeFragment", "setOnItemClickListener >> " + mGameListAdapter.getItem (position).toString ());
                if (judgeLogin ()) {
                    // 去游戏页面

                }
            }
        });
    }

    @Override
    public void onResume () {
        super.onResume ();
        if (AppPrefsContent.isLogined ()) {
            setTitle (AppPrefsContent.getUser ().optString ("nickName"));
        } else {
            setTitle (R.string.text_yk);
        }
        if (mGameListAdapter.getCount () == 0) {
            getGameList ();
        }
    }

    // *********************************

    /**
     * 获取游戏列表
     */
    private void getGameList () {
        NetWorkManager.getInstance ().getGameList (mLoadingDialog, new NetListener () {
            @Override
            public void onSuccessResponse (String msg, JSONArray jsonArray) {
                super.onSuccessResponse (msg, jsonArray);
                mGameListAdapter.setJSONArray (jsonArray);
            }

            @Override
            public void onErrorResponse (int errorWhat, String message) {
            }
        });
    }

    @Override
    public void onClick (View v) {
        switch (v.getId ()) {
            case R.id.qq_btn:
                if (judgeLogin ()) {
                    final String kefu_qq_num_default = getString (R.string.kefu_qq_num_default);
                    String call_kefu_qq = getString (R.string.call_kefu_qq, kefu_qq_num_default);
                    if (mDialogQQ == null) {
                        mDialogQQ = DialogHelp.createOkDialog (getActivity (), call_kefu_qq, new NormalDialogListener1 () {
                            @Override
                            public void onOkClickListener () {
                                String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + kefu_qq_num_default;
                                startActivity (new Intent (Intent.ACTION_VIEW, Uri.parse (url)));
                            }
                        });
                    }
                    mDialogQQ.show ();
                }
                break;
        }
    }
}
