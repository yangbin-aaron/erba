package com.teb.kilimanjaro.activitys.qq;

import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.teb.kilimanjaro.AppPrefs;
import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.activitys.BaseActivity;
import com.teb.kilimanjaro.models.entry.hall.GameListModel;
import com.teb.kilimanjaro.views.MyActionBar;

/**
 * Created by yangbin on 16/8/23.
 */
public class PayingMethodActivityQQ extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paying_method);
        initViews();
    }

    private void initViews() {
        GameListModel.GameListData data = AppPrefs.getInstance().getSelectedGame();
//        Log.e("hhh", data.toString());
        // ******MyActionBar
        MyActionBar myActionBar = (MyActionBar) findViewById(R.id.actionBar);
        myActionBar.setBarBackGround(R.color.main_background);
        myActionBar.setBarLeftImage(R.drawable.ic_back_btn);
        myActionBar.setBarTitleText(getString(R.string.qq_payingmethod_bar, TextUtils.isEmpty(data.getGameName()) ? "" : data.getGameName()));
        myActionBar.setOnActionBarClickListener(new MyActionBar.OnActionBarClickListener() {
            @Override
            public void onActionBarLeftClicked() {
                finish();
            }

            @Override
            public void onActionBarRightClicked() {
            }
        });

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
        webView.loadUrl(data.getDescUrl());
        //设置Web视图    
        webView.setWebViewClient(new WebViewClient());
    }
}
