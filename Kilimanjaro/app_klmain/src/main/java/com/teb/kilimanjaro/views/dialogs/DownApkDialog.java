package com.teb.kilimanjaro.views.dialogs;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.activitys.MainActivity;
import com.teb.kilimanjaro.services.DownloadService;

/**
 * Created by yangbin on 16/9/5.
 */
public class DownApkDialog extends Dialog {

    private TextView mProssTV;// 显示进度
    private Context mContext;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DownloadService.ACTION_PROGRESS)) {
                int pross = intent.getIntExtra("progress", 0);
                if (pross < 101) {
                    mProssTV.setText(pross + "%");
                } else {
                    dismiss();
                    ((MainActivity) mContext).finish();
                }
            }
        }
    };

    public DownApkDialog(Context context) {
        super(context, R.style.StylesDialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_dialog_util_down);
        setCancelable(false);
        initViews();
    }

    private void initViews() {
        mProssTV = (TextView) findViewById(R.id.tv_down_pross);

        ImageView imageView = (ImageView) findViewById(R.id.dialog_util_loading_imageview);
        // 开始旋转
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
        animationDrawable.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadService.ACTION_PROGRESS);
        mContext.registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mContext.unregisterReceiver(mReceiver);
    }
}
