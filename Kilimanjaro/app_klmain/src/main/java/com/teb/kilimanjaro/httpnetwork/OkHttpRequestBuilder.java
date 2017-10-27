package com.teb.kilimanjaro.httpnetwork;

import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.teb.kilimanjaro.configs.HandlerConfig;
import com.teb.kilimanjaro.models.entry.BaseJsonModel;
import com.teb.kilimanjaro.okhttp.callback.ResultCallback;
import com.teb.kilimanjaro.okhttp.request.OkHttpRequest;

/**
 * Created by yangbin on 16/8/12.
 */
public class OkHttpRequestBuilder {
    public static final String TAG = "OkHttpRequestBuilder";

    public OkHttpRequestBuilder create(final String apiUrl, String json, final Handler handler) {
        Log.e("aaron", TAG + "***request***" + apiUrl + ">>>" + json);
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
                        handler.obtainMessage(HandlerConfig.WHAT_NET_ERROR).sendToTarget();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("result", TAG + "---response---" + apiUrl + ">>>" + response);
                        try {
                            BaseJsonModel result = new Gson().fromJson(response, BaseJsonModel.class);
                            if (result.isSuccess()) {
                                mOkHttpRequestBuilderCallBack.onRespone(response);
                            } else {
                                if (result.isUpdateToken()) {
                                    handler.obtainMessage(HandlerConfig.WHAT_TOKEN_IS_UPDATED).sendToTarget();
                                } else {
                                    handler.obtainMessage(HandlerConfig.WHAT_POST_FAIL, result.getMessage()).sendToTarget();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            handler.obtainMessage(HandlerConfig.WHAT_DATA_WRONG).sendToTarget();
                        }
                    }

                    @Override
                    public void onAfter() {
                        super.onAfter();
                    }
                });
        return this;
    }

    public OkHttpRequestBuilderCallBack mOkHttpRequestBuilderCallBack;

    public OkHttpRequestBuilder callBack(OkHttpRequestBuilderCallBack okHttpRequestBuilderCallBack) {
        mOkHttpRequestBuilderCallBack = okHttpRequestBuilderCallBack;
        return this;
    }

    public interface OkHttpRequestBuilderCallBack {
        void onRespone(String response);
    }
}
