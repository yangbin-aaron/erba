package com.teb.kilimanjaro.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.teb.kilimanjaro.R;

/**
 * Created by yangbin on 16/7/11.
 * 输入密码的dialog
 */
public class PwdDialog extends Dialog {

    // 标题，中间消息，左边，右边 TextView
    private TextView  mLeftTV, mRightTV;

    private EditText mPwdET;// 趋势部分编辑框

    public PwdDialog(Context context) {
        super(context, R.style.StylesDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_dialog_pwd_layout);
        setCancelable(true);
        initViews();
    }

    private void initViews() {
        mLeftTV = (TextView) findViewById(R.id.dialog_left_textview);
        mLeftTV.setOnClickListener(mClickListener);
        mRightTV = (TextView) findViewById(R.id.dialog_right_textview);
        mRightTV.setOnClickListener(mClickListener);
        mPwdET = (EditText) findViewById(R.id.et_dialog_pwd);
    }

    public PwdDialog listener(OnDialogClickListener listener) {
        mOnDialogClickListener = listener;
        return this;
    }

    private OnDialogClickListener mOnDialogClickListener = null;
    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mOnDialogClickListener != null) {
                if (v == mLeftTV) {
                    PwdDialog.this.dismiss();
                } else if (v == mRightTV) {
                    String pwd = mPwdET.getText().toString().trim();
                    if (!TextUtils.isEmpty(pwd)) {
                        mOnDialogClickListener.onRightClicked(pwd);
                        PwdDialog.this.dismiss();
                    }
                }
            }
        }
    };

    /**
     * 密码界面使用
     */
    public interface OnDialogClickListener {
        void onRightClicked(String pwd);
    }
}
