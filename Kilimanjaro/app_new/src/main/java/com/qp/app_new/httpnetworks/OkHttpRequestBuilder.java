package com.qp.app_new.httpnetworks;

import android.app.Dialog;
import android.text.TextUtils;

import com.qp.app_new.App;
import com.qp.app_new.AppPrefs;
import com.qp.app_new.R;
import com.qp.app_new.configs.NetStatusConfig;
import com.qp.app_new.interfaces.NetListener;
import com.qp.app_new.okhttp.callback.ResultCallback;
import com.qp.app_new.okhttp.request.OkHttpRequest;
import com.qp.app_new.utils.LogUtil;
import com.squareup.okhttp.Request;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by yangbin on 16/8/12.
 */
public class OkHttpRequestBuilder {

    public OkHttpRequestBuilder create(String apiUrl, Dialog dialog, String json, NetListener listener) {
        return this.create(apiUrl, dialog, json, false, listener);
    }

    public OkHttpRequestBuilder create(final String apiUrl, final Dialog dialog, final String json, final boolean isList, final NetListener
            listener) {
        LogUtil.e("apiUrl = " + apiUrl + " \n json = " + json);
        if (dialog != null) dialog.show();
        new OkHttpRequest.Builder()
                .url(apiUrl)
                .content(json)
                .post(new ResultCallback<String>() {

                    @Override
                    public void onBefore(Request request) {
                        super.onBefore(request);
                    }

                    @Override
                    public void onError(Request request, Exception e) {
                        LogUtil.e( "错误编码:" + NetStatusConfig.STATUS_NET_ERROR + ", 错误信息:" + App.mContext.getResources().getString(R.string
                                .can_not_connect));
                        if (listener != null)
                            listener.onErrorResponse(NetStatusConfig.STATUS_NET_ERROR, App.mContext.getResources().getString(R.string
                                    .can_not_connect));
                        if (dialog != null) dialog.dismiss();
                    }

                    @Override
                    public void onResponse(String response) {
                        if (dialog != null) dialog.dismiss();
                        LogUtil.e( "apiUrl = " + apiUrl + "\nresponse = " + response);
                        if (listener != null) listener.onResponse(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int status = jsonObject.optInt("status");
                            String token = jsonObject.optString("token");
                            String message = jsonObject.optString("message");

                            if (status == NetStatusConfig.STATUS_POST_SUCCESS) {// 成功
                                if (!TextUtils.isEmpty(token)) AppPrefs.getInstance().saveToken(token);
                                if (isList) {// 获取列表
                                    JSONArray array = jsonObject.optJSONArray("data");
                                    if (array != null && array.length() > 0) {
                                        if (listener != null) listener.onSuccessResponse(message, array);
                                    } else {
                                        if (listener != null)
                                            listener.onErrorResponse(NetStatusConfig.STATUS_HAVE_NO_DATA, App.mContext.getString(R.string
                                                    .app_no_data));
                                    }
                                } else {
                                    JSONObject object = jsonObject.optJSONObject("data");
                                    if (object != null) {
                                        if (listener != null) listener.onSuccessResponse(message, object);
                                    } else {
                                        if (listener != null) listener.onSuccessResponse(jsonObject);
                                    }
                                }
                            } else {// 失败
                                if (status == NetStatusConfig.STATUS_TOKEN_IS_UPDATED) {
                                    LogUtil.e( "错误编码:" + NetStatusConfig.STATUS_TOKEN_IS_UPDATED + ", 错误信息:" + App.mContext.getResources()
                                            .getString(R.string.have_login_wrong));
                                    listener.onErrorResponse(NetStatusConfig.STATUS_TOKEN_IS_UPDATED, message);
                                } else {
                                    LogUtil.e( "错误编码:" + NetStatusConfig.STATUS_POST_FAIL + ", 错误信息:" + message);
                                    if (listener != null)
                                        listener.onErrorResponse(NetStatusConfig.STATUS_POST_FAIL, message);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            LogUtil.e( "错误编码:" + NetStatusConfig.STATUS_DATA_WRONG + ", 错误信息:" + App.mContext.getResources().getString(R.string
                                    .have_not_service));
                            if (listener != null)
                                listener.onErrorResponse(NetStatusConfig.STATUS_DATA_WRONG, App.mContext.getResources().getString(R.string
                                        .have_not_service));
                        }
                    }

                    @Override
                    public void onAfter() {
                        super.onAfter();
                    }
                });
        return this;
    }

//    public OkHttpRequestBuilderCallBack mOkHttpRequestBuilderCallBack;
//
//    public OkHttpRequestBuilder callBack(OkHttpRequestBuilderCallBack okHttpRequestBuilderCallBack) {
//        mOkHttpRequestBuilderCallBack = okHttpRequestBuilderCallBack;
//        return this;
//    }

//    public interface OkHttpRequestBuilderCallBack {
//        void onRespone(String response);
//    }
}
