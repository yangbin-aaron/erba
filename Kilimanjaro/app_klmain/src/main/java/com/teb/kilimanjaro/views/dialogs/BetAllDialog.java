package com.teb.kilimanjaro.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.teb.kilimanjaro.App;
import com.teb.kilimanjaro.AppPrefs;
import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.utils.ToastUtil;

/**
 * Created by yangbin on 16/7/22.
 */
public class BetAllDialog extends Dialog {

    private View mTopView;

    private TextView mGameCoinTV, mMaxCoinTV;
    private EditText mBetCoinET;
    private TextView[] mBetCoinTVs;
    private int[] ids = {R.id.tv_bet_coin_1_1, R.id.tv_bet_coin_1_2, R.id.tv_bet_coin_1_3, R.id.tv_bet_coin_1_4, R.id.tv_bet_coin_1_5};
    private TextView mCancelTV, mConfirmTV;

    private String mDefaultCoin = null;// 默认金额
    private long mMaxCoin;// 最大可投金额

    private int mCount = 0;

    /**
     * @param context
     * @param numCount 单点球的数量,其他模式时为0
     */
    public BetAllDialog(Context context, int numCount) {
        super(context, R.style.StylesDialog);
        mCount = numCount;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_dialog_betall_layout);
        setCancelable(true);
        initViews();

        Window dialogWindow = getWindow();
        dialogWindow.setWindowAnimations(R.style.dialogWindowAnim2); //设置窗口弹出动画
        dialogWindow.setBackgroundDrawableResource(R.color.transparent); //设置对话框背景为透明
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = App.getAppContext().getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = d.widthPixels;
        dialogWindow.setAttributes(lp);
    }

    private void initViews() {
        final long gameCoin = AppPrefs.getInstance().getGameLong();
        
        mTopView = findViewById(R.id.dialog_top_layout);
        mTopView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mGameCoinTV = (TextView) findViewById(R.id.tv_gamecoin);
        mGameCoinTV.setText(gameCoin + "");

        mMaxCoinTV = (TextView) findViewById(R.id.tv_max_coin);
        mMaxCoinTV.setText(mMaxCoin + "");

        mBetCoinET = (EditText) findViewById(R.id.et_betall_coin);
        if (mDefaultCoin == null) {
            mBetCoinET.setText(gameCoin + "");
        } else {
            mBetCoinET.setText(mDefaultCoin);
        }
        mBetCoinET.setSelection(mBetCoinET.getText().toString().trim().length());

        mBetCoinTVs = new TextView[5];
        for (int i = 0; i < mBetCoinTVs.length; i++) {
            final int position = i;
            mBetCoinTVs[i] = (TextView) findViewById(ids[i]);
            mBetCoinTVs[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int fm = position + 1;// 分母
                    long result = gameCoin / fm;
                    if (result > mMaxCoin) {
                        result = mMaxCoin % 2 == 0 ? mMaxCoin : mMaxCoin - 1;
                    }
                    mBetCoinET.setText(result + "");
                    mBetCoinET.setSelection(mBetCoinET.getText().toString().trim().length());
                }
            });
        }

        mCancelTV = (TextView) findViewById(R.id.dialog_left_textview);
        mCancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mConfirmTV = (TextView) findViewById(R.id.dialog_right_textview);
        mConfirmTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mBetCoinET.getText().toString().trim();
                if (TextUtils.isEmpty(text)) text = "0";
                long coin = Long.parseLong(text);
                if (coin % 2 != 0) {
                    ToastUtil.showToast(R.string.qq_betting_2);
                    return;
                }
                if (mCount > 0) {// 单点，投注必须为球数的偶数倍
                    int minBs = mCount * 2;
                    if (coin % minBs != 0) {
                        ToastUtil.showToast("投注金额应为 (" + mCount + " * 2) 的倍数！");
                        int bs = (int) (coin / minBs);
                        coin = bs * minBs;
                        mBetCoinET.setText(coin + "");
                        mBetCoinET.setSelection(mBetCoinET.getText().toString().trim().length());
                        return;
                    }
                }
                if (coin > mMaxCoin) {
                    ToastUtil.showToast(App.getAppContext().getResources().getString(R.string.coin_max) + mMaxCoin);
                    mBetCoinET.setText(mMaxCoin + "");
                    mBetCoinET.setSelection(mBetCoinET.getText().toString().trim().length());
                    return;
                }
                mOnDialogClickListener.onRightClicked(coin);
                dismiss();
            }
        });
    }

    /**
     * 设置默认金额
     *
     * @param defaultCoin
     * @return
     */
    public BetAllDialog setDefaultCoin(String defaultCoin) {
        mDefaultCoin = defaultCoin;
        return this;
    }

    public BetAllDialog setMaxCoin(long maxCoin) {
        mMaxCoin = maxCoin;
        return this;
    }

    private OnDialogClickListener mOnDialogClickListener = null;

    public BetAllDialog listener(OnDialogClickListener listener) {
        mOnDialogClickListener = listener;
        return this;
    }

    /**
     * 两个按钮使用
     */
    public interface OnDialogClickListener {
        void onRightClicked(long coin);
    }
}
