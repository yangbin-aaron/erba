package com.qp.app_new.activitys.home;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.qp.app_new.R;
import com.qp.app_new.activitys.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aaron on 2017/11/20.
 * 玩法介绍
 */

public class PayingMethodActivity extends BaseActivity {
    @Override
    public int getContentView() {
        return R.layout.activity_paying_method;
    }

    private JSONObject mGameJSONObject;

    @Override
    public void getIntentData() {
        super.getIntentData();
        try {
            mGameJSONObject = new JSONObject(getIntent().getStringExtra("gameJsonObject"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initView() {
        initActionBar();
        setLeftIV(R.drawable.ic_back_btn);
        setTitle(getString(R.string.qq_payingmethod_bar, mGameJSONObject.optString("gameName")));

        // ******WebView
        WebView webView = (WebView) findViewById(R.id.wv_payingmethod);
        WebSettings webSettings = webView.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        //加载需要显示的网页
        webView.loadUrl(mGameJSONObject.optString("descUrl"));
        //设置Web视图
        webView.setWebViewClient(new WebViewClient());
    }
}
