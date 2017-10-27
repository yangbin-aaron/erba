package com.teb.kilimanjaro.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.teb.kilimanjaro.App;
import com.teb.kilimanjaro.R;

/**
 * Created by yangbin on 16/7/26.
 */
public class ActionBarRightDialog extends Dialog {

    public static final int GOTO_BET_AUTO = 0;
    public static final int GOTO_MANAGE_MODE = 1;
    public static final int GOTO_PLAYING_METHOD = 2;
    public static final int GOTO_REVENUE = 3;

    private LinearLayout mGotoBetAutoLL, mGotoManageModeLL, mGotoPayingMethodLL, mGotoRevenueLL;
    
    public ActionBarRightDialog(Context context) {
        super(context, R.style.StylesDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_dialog_hall_barright_layout);
        setCancelable(true);
        initViews();

        Window dialogWindow = getWindow();
        dialogWindow.setWindowAnimations(R.style.dialogWindowAnim1); //设置窗口弹出动画
        dialogWindow.setBackgroundDrawableResource(R.color.transparent); //设置对话框背景为透明
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = App.getAppContext().getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = d.widthPixels;
        dialogWindow.setAttributes(lp);

    }

    private void initViews() {
        View topView = findViewById(R.id.dialog_top_layout);
        topView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        View bottomView = findViewById(R.id.dialog_bottom_layout);
        bottomView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mGotoBetAutoLL = (LinearLayout) findViewById(R.id.ll_goto_betauto);
        mGotoBetAutoLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDialogActionBarRightItemClickListener.onActionBarRightItemClicked(GOTO_BET_AUTO);
                dismiss();
            }
        });
        mGotoManageModeLL = (LinearLayout) findViewById(R.id.ll_goto_modemanage);
        mGotoManageModeLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDialogActionBarRightItemClickListener.onActionBarRightItemClicked(GOTO_MANAGE_MODE);
                dismiss();
            }
        });
        mGotoPayingMethodLL = (LinearLayout) findViewById(R.id.ll_goto_addmode);
        mGotoPayingMethodLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDialogActionBarRightItemClickListener.onActionBarRightItemClicked(GOTO_PLAYING_METHOD);
                dismiss();
            }
        });
        mGotoRevenueLL = (LinearLayout) findViewById(R.id.ll_goto_revenue);
        mGotoRevenueLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDialogActionBarRightItemClickListener.onActionBarRightItemClicked(GOTO_REVENUE);
                dismiss();
            }
        });
    }

    private OnDialogActionBarRightItemClickListener mOnDialogActionBarRightItemClickListener = null;

    public ActionBarRightDialog listener(OnDialogActionBarRightItemClickListener listener) {
        mOnDialogActionBarRightItemClickListener = listener;
        return this;
    }

    /**
     * 两个按钮使用
     */
    public interface OnDialogActionBarRightItemClickListener {
        void onActionBarRightItemClicked(int position);
    }
}
