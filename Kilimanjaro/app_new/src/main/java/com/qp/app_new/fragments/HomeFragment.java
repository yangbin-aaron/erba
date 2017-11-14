package com.qp.app_new.fragments;

import android.app.Dialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.qp.app_new.App;
import com.qp.app_new.AppPrefs;
import com.qp.app_new.R;
import com.qp.app_new.adapters.GameListAdapter;
import com.qp.app_new.dialogs.DialogHelp;
import com.qp.app_new.httpnetworks.NetWorkManager;
import com.qp.app_new.interfaces.NetListener;
import com.qp.app_new.interfaces.NormalDialogListener1;
import com.qp.app_new.interfaces.PopupWindowItemListener;
import com.qp.app_new.utils.ActivityStartUtils;
import com.qp.app_new.utils.AppUtils;
import com.qp.app_new.utils.ToastUtil;

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
    private Dialog mDialogQQ, mDialogPhone;
    private PopupWindow mPopupWindow;

    @Override
    public void initView() {
        findViewById(R.id.btn_left).setOnClickListener(this);
        findViewById(R.id.qq_btn).setOnClickListener(this);
        findViewById(R.id.phone_btn).setOnClickListener(this);

        final String[] popList = {getString(R.string.hall_paying_method)};
        mPopupWindow = DialogHelp.createPopupWindow(getActivity(), popList, new PopupWindowItemListener() {
            @Override
            public void onPopItemClick(View v, int position) {
                ToastUtil.showToast(popList[position]);
                mPopupWindow.dismiss();
            }
        });

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
//        if (AppPrefsContent.isLogined ()) {
//            setTitle (AppPrefsContent.getUser ().optString ("nickName"));
//        } else {
//            setTitle (R.string.text_yk);
//        }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                if (!mPopupWindow.isShowing()) {
                    mPopupWindow.showAsDropDown(v, 0, 0);
                }
                break;
            case R.id.qq_btn:
                if (!AppUtils.isQQClientAvailable(getActivity())) {
                    DialogHelp.showMessageDialog(getActivity(), getString(R.string.recharge_hasnot_qq));
                } else {
                    final String kefu_qq_num_default = AppPrefs.getInstance().getSysInfoQQ();
                    String call_kefu_qq = getString(R.string.call_kefu_qq, kefu_qq_num_default);
                    if (mDialogQQ == null) {
                        mDialogQQ = DialogHelp.createOkDialog(getActivity(), call_kefu_qq, new NormalDialogListener1() {
                            @Override
                            public void onOkClickListener() {
                                AppUtils.intoQQ(getActivity(), kefu_qq_num_default);
                            }
                        });
                    }
                    mDialogQQ.show();
                }
                break;
            case R.id.phone_btn:
                if (mDialogPhone == null) {
                    final String kefu_phone_default = AppPrefs.getInstance().getSysInfoPhone();
                    mDialogPhone = DialogHelp.createOkDialog(getActivity(), getString(R.string.app_call_phone, kefu_phone_default), new
                            NormalDialogListener1() {
                                @Override
                                public void onOkClickListener() {
                                    AppUtils.call(getActivity(), kefu_phone_default);
                                }
                            });
                }
                mDialogPhone.show();
                break;
        }
    }
}
