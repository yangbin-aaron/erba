package com.teb.kilimanjaro.views.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teb.kilimanjaro.App;
import com.teb.kilimanjaro.AppPrefs;
import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.models.entry.mine.BasicInfoModel;

/**
 * Created by yangbin on 16/7/11.
 * 普通的dialog
 */
public class MyDialog extends Dialog {

    private boolean isCancelable = true;// 点击取消按钮以及周边区域 是否隐藏对话框

    // 标题，中间消息，左边，右边的内容
    private String mTitle, mMessage, mLeft, mRight;
    // 标题，中间消息，左边，右边 TextView
    private TextView mTitleTV, mMessageTV, mLeftTV, mRightTV;

    private EditText mLimitET;// 趋势部分编辑框
    private int mLimit = 50;

    public MyDialog(Context context) {
        super(context, R.style.StylesDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_dialog_layout);
        setCancelable(isCancelable);
        initViews();
    }

    private void initViews() {
        mTitleTV = (TextView) findViewById(R.id.dialog_title_textView);
        if (mTitle == null) mTitle = App.getAppContext().getResources().getString(R.string.app_name);
        mTitleTV.setText(mTitle);
        mMessageTV = (TextView) findViewById(R.id.dialog_message_textview);
        if (mMessage != null) {
            mMessageTV.setVisibility(View.VISIBLE);
            findViewById(R.id.dialog_center_trendlimit_ll).setVisibility(View.GONE);
            mMessageTV.setText(mMessage);
        }
        mLeftTV = (TextView) findViewById(R.id.dialog_left_textview);
        if (mLeft == null) {
            mLeftTV.setVisibility(View.GONE);
            findViewById(R.id.dialog_center_line).setVisibility(View.GONE);
        } else {
            mLeftTV.setOnClickListener(mClickListener);
            mLeftTV.setText(mLeft);
        }
        mRightTV = (TextView) findViewById(R.id.dialog_right_textview);
        mRightTV.setOnClickListener(mClickListener);
        mRightTV.setText(mRight);
        mLimitET = (EditText) findViewById(R.id.et_dialog_limit);
        mLimitET.setText(mLimit + "");
        mLimitET.setSelection(mLimitET.getText().toString().trim().length());
    }

    public MyDialog cancelable(boolean isCancelable) {
        this.isCancelable = isCancelable;
        return this;
    }

    /**
     * 按钮文字为 默认
     *
     * @return
     */
    public MyDialog setDefaultBtnText() {
        mLeft = App.getAppContext().getResources().getString(R.string.app_cancel);
        mRight = App.getAppContext().getResources().getString(R.string.app_confirm);
        return this;
    }

    public MyDialog setLeftText(String left) {
        mLeft = left;
        return this;
    }

    public MyDialog setRightText(String right) {
        mRight = right;
        return this;
    }

    public MyDialog setTitleText(String title) {
        mTitle = title;
        if (mTitle == null) mTitle = App.getAppContext().getResources().getString(R.string.app_name);
        return this;
    }

    public MyDialog setMessage(String message) {
        mMessage = message;
        if (mMessage == null) mMessage = App.getAppContext().getString(R.string.msg_error);
        return this;
    }

    public MyDialog setLimit(int limit) {
        mLimit = limit;
        return this;
    }

    public MyDialog listener(OnDialogClickListener listener) {
        mOnDialogClickListener = listener;
        return this;
    }

    public MyDialog listener(OnDialogClickListener1 listener) {
        mOnDialogClickListener1 = listener;
        return this;
    }

    public MyDialog listener(OnDialogClickListener2 listener) {
        mOnDialogClickListener2 = listener;
        return this;
    }

    private OnDialogClickListener mOnDialogClickListener = null;
    private OnDialogClickListener1 mOnDialogClickListener1 = null;
    private OnDialogClickListener2 mOnDialogClickListener2 = null;
    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mOnDialogClickListener != null) {
                if (v == mLeftTV) {
                    mOnDialogClickListener.onLeftClicked();
                    MyDialog.this.dismiss();
                } else if (v == mRightTV) {
                    mOnDialogClickListener.onRightClicked();
                    MyDialog.this.dismiss();
                }
            } else if (mOnDialogClickListener1 != null) {
                if (v == mLeftTV) {
                    MyDialog.this.dismiss();
                } else if (v == mRightTV) {
                    mOnDialogClickListener1.onRightClicked();
                    MyDialog.this.dismiss();
                }
            } else if (mOnDialogClickListener2 != null) {
                if (v == mLeftTV) {
                    MyDialog.this.dismiss();
                } else if (v == mRightTV) {
                    String limitStr = mLimitET.getText().toString().trim();
                    if (TextUtils.isEmpty(limitStr) || Integer.parseInt(limitStr) > 200) {
                        mLimit = 200;
                        mLimitET.setText(mLimit + "");
                    } else {
                        mLimit = Integer.parseInt(limitStr);
                    }
                    mOnDialogClickListener2.onRightClicked(mLimit);
                    MyDialog.this.dismiss();
                }
            } else {
                MyDialog.this.dismiss();
            }
        }
    };

    /**
     * 两个按钮使用
     */
    public interface OnDialogClickListener {
        void onLeftClicked();

        void onRightClicked();
    }

    /**
     * 单个按钮使用
     */
    public interface OnDialogClickListener1 {
        void onRightClicked();
    }

    /**
     * 趋势界面使用
     */
    public interface OnDialogClickListener2 {
        void onRightClicked(int limit);
    }


    // -----------------加载对话框-------------------

    /**
     * 加载对话框
     *
     * @param context
     * @return
     */
    public static Dialog createLoadingDialog(Context context, int textResId, boolean setCancelable) {
        // 加载布局
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.view_dialog_util_loading, null);
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_util_loading_layout);
        TextView textView = (TextView) v.findViewById(R.id.dialog_util_loading_textview);
        textView.setText(textResId);
        ImageView imageView = (ImageView) v.findViewById(R.id.dialog_util_loading_imageview);
        // 开始旋转
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
        animationDrawable.start();

        Dialog loadingDialog = new Dialog(context, R.style.StylesDialog); // 创建自定义样式dialog
        loadingDialog.setCancelable(setCancelable);
        loadingDialog.setContentView(layout);
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = loadingDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); // 设置宽度
        lp.height = (int) (display.getHeight());
        loadingDialog.getWindow().setAttributes(lp);
        return loadingDialog;
    }

    public static Dialog createLoadingDialog(Context context, boolean setCancelable) {
        return createLoadingDialog(context, R.string.app_loading, setCancelable);
    }

    // ------------------------------------------------------------------------

    /**
     * 强制更新
     *
     * @param context
     * @param appUri
     */
    public static void showUpdateAppDialog(final Context context, boolean mustUpdate, final String appUri) {
        createUpdateAppDialog(context, mustUpdate)
                .listener(new MyDialog.OnDialogClickListener() {
                    @Override
                    public void onLeftClicked() {

                    }

                    @Override
                    public void onRightClicked() {
                        Uri uri = Uri.parse(appUri);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        context.startActivity(intent);

                        ((Activity) context).finish();
                    }
                }).show();
    }

    /**
     * @param context
     * @param mustUpdate
     */
    public static MyDialog createUpdateAppDialog(final Context context, boolean mustUpdate) {
        return new MyDialog(context)
                .setLeftText(mustUpdate ? null : context.getString(R.string.app_cancel))
                .setRightText(context.getString(R.string.app_confirm))
                .setMessage(context.getString(mustUpdate ? R.string.app_updateapp_must : R.string.app_updateapp_))
                .cancelable(!mustUpdate);
    }

    /**
     * 敬请期待对话框
     *
     * @param context
     */
    public static void showWaitDialog(final Context context) {
        new MyDialog(context)
                .setRightText(context.getString(R.string.app_confirm))
                .setMessage(context.getString(R.string.app_please_wait))
                .show();
    }

    /**
     * 拨打客服电话号码
     *
     * @param activity
     */
    public static void callKefu(final Activity activity) {
        final BasicInfoModel basicInfoModel = AppPrefs.getInstance().getBasicInfoModel();
        boolean flag = false;// 默认拨打电话
        if (basicInfoModel == null) {// 如果没有客服信息，则不给回应
            return;
        }
        MyDialog myDialog = null;
        if (basicInfoModel.getData().hasPhone()) {
            myDialog = new MyDialog(activity)
                    .setDefaultBtnText()
                    .setMessage(activity.getString(R.string.app_call_phone, basicInfoModel.getData().getPhone()))
                    .listener(new MyDialog.OnDialogClickListener1() {
                        @Override
                        public void onRightClicked() {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_CALL);
                            //uri:统一资源标示符（更广）  
                            intent.setData(Uri.parse("tel:" + basicInfoModel.getData().getPhone()));
                            //开启系统拨号器  
                            activity.startActivity(intent);
                        }
                    });
        } else {
            new MyDialog(activity)
                    .setRightText(activity.getString(R.string.app_cancel))
                    .setMessage(activity.getString(R.string.kefu_qq_num_default));
        }
        myDialog.show();
    }


}
