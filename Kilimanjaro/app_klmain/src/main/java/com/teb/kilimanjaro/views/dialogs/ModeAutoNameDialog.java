package com.teb.kilimanjaro.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.utils.ToastUtil;

/**
 * Created by yangbin on 16/7/26.
 */
public class ModeAutoNameDialog extends Dialog {

    private EditText mModeNameET;
    private String mName;

    public ModeAutoNameDialog(Context context) {
        super(context, R.style.StylesDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_dialog_modename_layout);
        setCancelable(true);
        initViews();
    }

    private void initViews() {
        mModeNameET = (EditText) findViewById(R.id.et_dialog_mode_name);
        if (mName == null) mName = "";
        mModeNameET.setText(mName);
        mModeNameET.setSelection(mModeNameET.getText().toString().trim().length());
        (findViewById(R.id.dialog_right_textview)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mModeNameET.getText().toString().trim();
                if (TextUtils.isEmpty(text)) {
                    ToastUtil.showToast("请输入模式名称");
                    return;
                }
                mOnDialogClickListener.onRightClicked(text);
                dismiss();
            }
        });

        (findViewById(R.id.dialog_left_textview)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public ModeAutoNameDialog setMessage(String text) {
        mName = text;
        return this;
    }

    private OnDialogClickListener mOnDialogClickListener = null;

    public ModeAutoNameDialog listener(OnDialogClickListener listener) {
        mOnDialogClickListener = listener;
        return this;
    }

    /**
     * 两个按钮使用
     */
    public interface OnDialogClickListener {
        void onRightClicked(String text);
    }
}
