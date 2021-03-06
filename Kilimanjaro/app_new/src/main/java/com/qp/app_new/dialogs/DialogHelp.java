package com.qp.app_new.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.qp.app_new.AppPrefs;
import com.qp.app_new.R;
import com.qp.app_new.interfaces.DandianDialogListener;
import com.qp.app_new.interfaces.NormalDialogListener1;
import com.qp.app_new.interfaces.NormalDialogListener2;
import com.qp.app_new.interfaces.PopupWindowItemListener;
import com.qp.app_new.utils.ActivityStartUtils;
import com.qp.app_new.utils.AppUtils;

/**
 * Created by yangbin on 16/7/11.
 * 普通的dialog
 */
public class DialogHelp {

    private static Dialog createBaseDialog(Context context, boolean isCancelable, View view) {
        final Dialog dialog = new Dialog(context, R.style.StylesDialog); // 创建自定义样式dialog
        dialog.setCancelable(isCancelable);
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth(); // 设置宽度
        lp.height = display.getHeight();
        dialog.getWindow().setAttributes(lp);
        dialog.setContentView(view);
        return dialog;
    }

    /**
     * 加载对话框
     *
     * @param context
     * @return
     */
    public static Dialog createLoadingDialog(Context context, int textResId, boolean setCancelable) {
        // 加载布局
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_loading, null);
        TextView textView = (TextView) v.findViewById(R.id.dialog_util_loading_textview);
        textView.setText(textResId);
        ImageView imageView = (ImageView) v.findViewById(R.id.dialog_util_loading_imageview);
        // 开始旋转
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
        animationDrawable.start();

        return createBaseDialog(context, setCancelable, v);
    }

    /**
     * @param context
     * @param msg
     * @param isShowLeft   是否显示左边按钮
     * @param isCancelable 返回键能否关闭
     * @param sideCance    点击周围能否关闭
     * @param listener1    一个按钮的监听
     * @param listener2    两个按钮的监听
     * @return
     */
    private static Dialog createNormalDialog(Context context,
                                             String msg,
                                             boolean isShowLeft,
                                             String leftText,
                                             String rightText,
                                             boolean isCancelable,
                                             boolean sideCance,
                                             final NormalDialogListener1 listener1,
                                             final NormalDialogListener2 listener2,
                                             final boolean isDismiss) {
        // 加载布局
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_nomal, null);

        final Dialog dialog = createBaseDialog(context, isCancelable, v);

        TextView msgTV = (TextView) v.findViewById(R.id.dialog_message_textview);
        msgTV.setText(msg);
        TextView leftTV = (TextView) v.findViewById(R.id.dialog_left_textview);
        if (!TextUtils.isEmpty(leftText)) leftTV.setText(leftText);
        TextView rightTV = (TextView) v.findViewById(R.id.dialog_right_textview);
        if (!TextUtils.isEmpty(rightText)) rightTV.setText(rightText);
        leftTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener2 != null) listener2.onLeftClickListener();
                if (isDismiss) dialog.dismiss();
            }
        });
        rightTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener1 != null) listener1.onOkClickListener();
                if (listener2 != null) listener2.onRightClickListener();
                if (isDismiss) dialog.dismiss();
            }
        });
        if (sideCance) {
            v.findViewById(R.id.dialog_normal_layout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isDismiss) dialog.dismiss();
                }
            });
        }
        if (!isShowLeft) {
            v.findViewById(R.id.dialog_center_line).setVisibility(View.GONE);
            leftTV.setVisibility(View.GONE);
        }
        return dialog;
    }

    public static Dialog createLoadingDialog(Context context, boolean setCancelable) {
        return createLoadingDialog(context, R.string.app_loading, setCancelable);
    }

    /**
     * 不能关闭的确定对话框
     *
     * @param context
     * @param msg
     * @param listener
     * @return
     */
    public static Dialog createMustDialog(Context context, String msg, NormalDialogListener1 listener, boolean isDismiss) {
        return createNormalDialog(context, msg, false, "", "", false, false, listener, null, isDismiss);
    }

    public static Dialog createMustDialog(Context context, String msg, NormalDialogListener1 listener) {
        return createNormalDialog(context, msg, false, "", "", false, false, listener, null, true);
    }

    /**
     * 两个按钮，只有确定按钮有用
     *
     * @param context
     * @param msg
     * @param listener
     * @return
     */
    public static Dialog createOkDialog(Context context, String msg, NormalDialogListener1 listener) {
        return createNormalDialog(context, msg, true, "", "", true, true, listener, null, true);
    }

    /**
     * 两个按钮，只有确定按钮有用(能设置确定按钮的值)
     *
     * @param context
     * @param msg
     * @param listener
     * @return
     */
    public static Dialog createOkDialog(Context context, String msg, String rightText, NormalDialogListener1 listener) {
        return createNormalDialog(context, msg, true, "", rightText, true, true, listener, null, true);
    }

    /**
     * 两个按钮，都能响应事件
     *
     * @param context
     * @param msg
     * @param listener
     * @return
     */
    public static Dialog createOkCancelDialog(Context context, String msg, NormalDialogListener2 listener) {
        return createNormalDialog(context, msg, true, "", "", true, true, null, listener, true);
    }

    /**
     * 两个按钮，都能响应事件，并能设置按钮的值
     *
     * @param context
     * @param msg
     * @param left
     * @param right
     * @param listener
     * @return
     */
    public static Dialog createOkCancelDialog(Context context, String msg, String left, String right, NormalDialogListener2 listener) {
        return createNormalDialog(context, msg, true, left, right, true, true, null, listener, true);
    }

    /**
     * 只做显示
     *
     * @param context
     * @param msg
     */
    public static void showMessageDialog(Context context, String msg) {
        createNormalDialog(context, msg, false, "", "", true, true, null, null, true).show();
    }

    /**
     * 请先登录
     *
     * @param context
     * @return
     */
    public static Dialog createToLoginDialog(final Context context) {
        return createNormalDialog(context, context.getString(R.string.please_login), true,
                "", context.getString(R.string.goto_login_btn), true, true,
                new NormalDialogListener1() {
                    @Override
                    public void onOkClickListener() {
                        ActivityStartUtils.startLoginActivity((Activity) context);
                    }
                }, null, true);
    }

    public static PopupWindow createPopupWindow(Context context, String[] list, final PopupWindowItemListener listener) {
        View popView = LayoutInflater.from(context).inflate(R.layout.popwindow_list, null);
        LinearLayout pop_ly = (LinearLayout) popView.findViewById(R.id.pop_ly);
        pop_ly.removeAllViews();
        if (list != null && list.length > 0) {
            for (int i = 0; i < list.length; i++) {
                View v = LayoutInflater.from(context).inflate(R.layout.popwindow_list_item, null);
                TextView textView = (TextView) v.findViewById(R.id.pop_tv);
                View line = v.findViewById(R.id.pop_line);
                textView.setText(list[i]);
                final int finalI = i;
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onPopItemClick(v, finalI);
                    }
                });
                line.setVisibility(View.VISIBLE);
                if (i == list.length - 1) {
                    line.setVisibility(View.GONE);
                }
                pop_ly.addView(v);
            }
        }
        PopupWindow popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        return popupWindow;
    }

    /**
     * 进入QQ
     *
     * @param context
     */
    public static void showKefuQQDialog(final Context context) {
        if (!AppUtils.isQQClientAvailable(context)) {
            showMessageDialog(context, context.getString(R.string.recharge_hasnot_qq));
        } else {
            final String kefu_qq_num_default = AppPrefs.getInstance().getSysInfoQQ();
            String call_kefu_qq = context.getString(R.string.call_kefu_qq, kefu_qq_num_default);
            createOkDialog(context, call_kefu_qq, new NormalDialogListener1() {
                @Override
                public void onOkClickListener() {
                    AppUtils.intoQQ(context, kefu_qq_num_default);
                }
            }).show();
        }
    }

    /**
     * 拨打电话
     *
     * @param context
     */
    public static void showKefuPhoneDialog(final Context context) {
        final String kefu_phone_default = AppPrefs.getInstance().getSysInfoPhone();
        createOkDialog(context, context.getString(R.string.app_call_phone, kefu_phone_default), new
                NormalDialogListener1() {
                    @Override
                    public void onOkClickListener() {
                        AppUtils.call(context, kefu_phone_default);
                    }
                }).show();
    }

    /**
     * 自定义单点    输入金额的对话框
     *
     * @param context
     * @param amt
     * @param listener
     * @return
     */
    public static Dialog createDanDianDialog(Context context,
                                             long amt,
                                             final DandianDialogListener listener) {
        // 加载布局
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_dandian, null);
        final Dialog dialog = createBaseDialog(context, true, v); // 创建自定义样式dialog

        final EditText et = (EditText) v.findViewById(R.id.et_bet_coin);
        if (amt == 0) amt = 10;// 默认10
        et.setText(amt + "");
        et.setSelection(et.getText().toString().trim().length());
        TextView leftTV = (TextView) v.findViewById(R.id.dialog_left_textview);
        TextView rightTV = (TextView) v.findViewById(R.id.dialog_right_textview);

        leftTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        rightTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    String etStr = et.getText().toString().trim();
                    if (TextUtils.isEmpty(etStr)) etStr = "0";
                    long a = Long.parseLong(etStr);
                    listener.onOkClickListener(a);
                }
                dialog.dismiss();
            }
        });
        v.findViewById(R.id.dialog_normal_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        return dialog;
    }
}
