package com.qp.app_new.httpnetworks;

import android.app.Dialog;
import android.text.TextUtils;
import android.util.Log;

import com.qp.app_new.App;
import com.qp.app_new.AppPrefs;
import com.qp.app_new.R;
import com.qp.app_new.configs.NetStatusConfig;
import com.qp.app_new.interfaces.NetListener;
import com.qp.app_new.okhttp.callback.ResultCallback;
import com.qp.app_new.okhttp.request.OkHttpRequest;
import com.squareup.okhttp.Request;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by yangbin on 16/8/12.
 */
public class OkHttpRequestBuilder {
    public static final String TAG = "OkHttpRequestBuilder";

    public OkHttpRequestBuilder create (final String apiUrl, final Dialog dialog, final String json, final NetListener listener) {
        Log.e (TAG, "apiUrl = " + apiUrl + " \n json = " + json);
        if (dialog != null) dialog.show ();
        new OkHttpRequest.Builder ()
                .url (apiUrl)
                .content (json)
                .post (new ResultCallback<String> () {

                    @Override
                    public void onBefore (Request request) {
                        super.onBefore (request);
                    }

                    @Override
                    public void onError (Request request, Exception e) {
                        Log.e (TAG, "错误编码:" + NetStatusConfig.STATUS_NET_ERROR + ", 错误信息:" + App.mContext.getResources ().getString (R.string
                                .can_not_connect));
                        listener.onErrorResponse (NetStatusConfig.STATUS_NET_ERROR, App.mContext.getResources ().getString (R.string
                                .can_not_connect));
                        if (dialog != null) dialog.dismiss ();
                    }

                    @Override
                    public void onResponse (String response) {
                        if (dialog != null) dialog.dismiss ();
                        Log.e (TAG, "response = " + response);
                        listener.onResponse (response);
                        try {
                            JSONObject jsonObject = new JSONObject (response);
                            int status = jsonObject.optInt ("status");
                            String token = jsonObject.optString ("token");

                            if (status == NetStatusConfig.STATUS_POST_SUCCESS) {// 成功
                                if (!TextUtils.isEmpty (token)) AppPrefs.getInstance ().saveToken (token);
                                JSONObject object = jsonObject.optJSONObject ("data");
                                if (object != null) {
                                    listener.onSuccessResponse (jsonObject.optString ("message"), object);
                                }
                                JSONArray array = jsonObject.optJSONArray ("data");
                                if (array != null) {
                                    listener.onSuccessResponse (jsonObject.optString ("message"), array);
                                }
                            } else {// 失败
                                if (status == NetStatusConfig.STATUS_TOKEN_IS_UPDATED) {
                                    Log.e (TAG, "错误编码:" + NetStatusConfig.STATUS_TOKEN_IS_UPDATED + ", 错误信息:" + App.mContext.getResources ()
                                            .getString (R.string.have_login_wrong));
                                    listener.onErrorResponse (NetStatusConfig.STATUS_TOKEN_IS_UPDATED, App.mContext.getResources ().getString (R
                                            .string.have_login_wrong));
                                } else {
                                    Log.e (TAG, "错误编码:" + NetStatusConfig.STATUS_POST_FAIL + ", 错误信息:" + jsonObject.optString ("message"));
                                    listener.onErrorResponse (NetStatusConfig.STATUS_POST_FAIL, jsonObject.optString ("message"));
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace ();
                            Log.e (TAG, "错误编码:" + NetStatusConfig.STATUS_DATA_WRONG + ", 错误信息:" + App.mContext.getResources ().getString (R.string
                                    .have_not_service));
                            listener.onErrorResponse (NetStatusConfig.STATUS_DATA_WRONG, App.mContext.getResources ().getString (R.string
                                    .have_not_service));
                        }
                    }

                    @Override
                    public void onAfter () {
                        super.onAfter ();
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
