package com.qp.app_new.interfaces;

import com.qp.app_new.configs.NetStatusConfig;
import com.qp.app_new.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Aaron on 2017/11/8.
 */

public class NetListener {

    public void onResponse(String json) {

    }

    public void onSuccessResponse(String msg, JSONObject jsonObject) {

    }

    public void onSuccessResponse(String msg, JSONArray jsonArray) {
    }

    public void onErrorResponse(int errorWhat, String message) {
        switch (errorWhat) {
            case NetStatusConfig.STATUS_DATA_WRONG:
            case NetStatusConfig.STATUS_POST_FAIL:
            case NetStatusConfig.STATUS_NET_ERROR:
                ToastUtil.showToast(message);
                break;
            case NetStatusConfig.STATUS_TOKEN_IS_UPDATED:
                ToastUtil.showToast(message);
//                if (AppPrefs.getInstance().getTokenState()) {
//                    AppPrefs.getInstance().saveTokenState(false);// 不能够访问
//                    new MyDialog(act)
//                            .setRightText(act.getResources().getString(R.string.app_confirm))
//                            .cancelable(false)
//                            .setMessage(act.getResources().getString(R.string.have_login_wrong))
//                            .listener(new MyDialog.OnDialogClickListener1() {
//                                @Override
//                                public void onRightClicked() {
//                                    // 清空UserJson数据和GameListJson数据
//                                    AppPrefs.getInstance().saveUserJson(null);
//                                    AppPrefs.getInstance().saveGameListJson(null);
//                                    Intent intent = new Intent(act, LoginActivity.class);
//                                    act.finish();
//                                    act.startActivity(intent);
//                                }
//                            })
//                            .show();
//                }
                break;
        }
    }
}
