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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.qp.app_new.R;
import com.qp.app_new.interfaces.NormalDialogListener1;
import com.qp.app_new.interfaces.NormalDialogListener2;
import com.qp.app_new.interfaces.PopupWindowItemListener;
import com.qp.app_new.utils.ActivityStartUtils;

/**
 * Created by yangbin on 16/7/11.
 * 普通的dialog
 */
public class DialogHelp {
    /**
     * 加载对话框
     *
     * @param context
     * @return
     */
    public static Dialog createLoadingDialog (Context context, int textResId, boolean setCancelable) {
        // 加载布局
        LayoutInflater inflater = LayoutInflater.from (context);
        View v = inflater.inflate (R.layout.dialog_loading, null);
        LinearLayout layout = (LinearLayout) v.findViewById (R.id.dialog_util_loading_layout);
        TextView textView = (TextView) v.findViewById (R.id.dialog_util_loading_textview);
        textView.setText (textResId);
        ImageView imageView = (ImageView) v.findViewById (R.id.dialog_util_loading_imageview);
        // 开始旋转
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground ();
        animationDrawable.start ();

        Dialog loadingDialog = new Dialog (context, R.style.StylesDialog); // 创建自定义样式dialog
        loadingDialog.setCancelable (setCancelable);
        loadingDialog.setContentView (layout);
        WindowManager windowManager = ((Activity) context).getWindowManager ();
        Display display = windowManager.getDefaultDisplay ();
        WindowManager.LayoutParams lp = loadingDialog.getWindow ().getAttributes ();
        lp.width = (int) (display.getWidth ()); // 设置宽度
        lp.height = (int) (display.getHeight ());
        loadingDialog.getWindow ().setAttributes (lp);
        return loadingDialog;
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
    private static Dialog createNormalDialog (Context context,
                                              String msg,
                                              boolean isShowLeft,
                                              String leftText,
                                              String rightText,
                                              boolean isCancelable,
                                              boolean sideCance,
                                              final NormalDialogListener1 listener1,
                                              final NormalDialogListener2 listener2) {
        // 加载布局
        LayoutInflater inflater = LayoutInflater.from (context);
        View v = inflater.inflate (R.layout.dialog_nomal, null);
        final Dialog dialog = new Dialog (context, R.style.StylesDialog); // 创建自定义样式dialog
        dialog.setCancelable (isCancelable);
        dialog.setContentView (v);
        WindowManager windowManager = ((Activity) context).getWindowManager ();
        Display display = windowManager.getDefaultDisplay ();
        WindowManager.LayoutParams lp = dialog.getWindow ().getAttributes ();
        lp.width = display.getWidth (); // 设置宽度
        lp.height = display.getHeight ();
        dialog.getWindow ().setAttributes (lp);

        TextView msgTV = (TextView) v.findViewById (R.id.dialog_message_textview);
        msgTV.setText (msg);
        TextView leftTV = (TextView) v.findViewById (R.id.dialog_left_textview);
        if (!TextUtils.isEmpty (leftText)) leftTV.setText (leftText);
        TextView rightTV = (TextView) v.findViewById (R.id.dialog_right_textview);
        if (!TextUtils.isEmpty (rightText)) rightTV.setText (rightText);
        leftTV.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (listener2 != null) listener2.onLeftClickListener ();
                dialog.dismiss ();
            }
        });
        rightTV.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (listener1 != null) listener1.onOkClickListener ();
                if (listener2 != null) listener2.onRightClickListener ();
                dialog.dismiss ();
            }
        });
        if (sideCance) {
            v.findViewById (R.id.dialog_normal_layout).setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View v) {
                    dialog.dismiss ();
                }
            });
        }
        if (!isShowLeft) {
            v.findViewById (R.id.dialog_center_line).setVisibility (View.GONE);
            leftTV.setVisibility (View.GONE);
        }
        return dialog;
    }

    public static Dialog createLoadingDialog (Context context, boolean setCancelable) {
        return createLoadingDialog (context, R.string.app_loading, setCancelable);
    }

    /**
     * 两个按钮，只有确定按钮有用
     *
     * @param context
     * @param msg
     * @param listener
     * @return
     */
    public static Dialog createOkDialog (Context context, String msg, NormalDialogListener1 listener) {
        return createNormalDialog (context, msg, true, "", "", true, true, listener, null);
    }

    /**
     * 两个按钮，都能响应事件
     *
     * @param context
     * @param msg
     * @param listener
     * @return
     */
    public static Dialog createOkCancelDialog (Context context, String msg, NormalDialogListener2 listener) {
        return createNormalDialog (context, msg, true, "", "", true, true, null, listener);
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
    public static Dialog createOkCancelDialog (Context context, String msg, String left, String right, NormalDialogListener2 listener) {
        return createNormalDialog (context, msg, true, left, right, true, true, null, listener);
    }

    /**
     * 只做显示
     *
     * @param context
     * @param msg
     */
    public static void showMessageDialog (Context context, String msg) {
        createNormalDialog (context, msg, false, "", "", true, true, null, null).show ();
    }

    /**
     * 请先登录
     *
     * @param context
     * @return
     */
    public static Dialog createToLoginDialog (final Context context) {
        return createNormalDialog (context, context.getString (R.string.please_login), true,
                "", context.getString (R.string.goto_login_btn), true, true,
                new NormalDialogListener1 () {
                    @Override
                    public void onOkClickListener () {
                        ActivityStartUtils.startLoginActivity ((Activity) context);
                    }
                }, null);
    }

    public static PopupWindow createPopupWindow (Context context, String[] list, final PopupWindowItemListener listener) {
        int padding = (int) context.getResources ().getDimension (R.dimen.dp_14);
        View popView = LayoutInflater.from (context).inflate (R.layout.popwindow_list, null);
        LinearLayout pop_ly = (LinearLayout) popView.findViewById (R.id.pop_ly);
        pop_ly.removeAllViews ();
        if (list != null && list.length > 0) {
            for (int i = 0; i < list.length; i++) {
                View v = LayoutInflater.from (context).inflate (R.layout.popwindow_list_item, null);
                TextView textView = (TextView) v.findViewById (R.id.pop_tv);
                View line = v.findViewById (R.id.pop_line);
                textView.setText (list[i]);
                final int finalI = i;
                textView.setOnClickListener (new View.OnClickListener () {
                    @Override
                    public void onClick (View v) {
                        listener.onPopItemClick (v, finalI);
                    }
                });
                line.setVisibility (View.VISIBLE);
                if (i == list.length - 1) {
                    line.setVisibility (View.GONE);
                }
                pop_ly.addView (v);
            }
        }
        PopupWindow popupWindow = new PopupWindow (popView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable (new ColorDrawable (0x00000000));
        popupWindow.setOutsideTouchable (true);
        popupWindow.setFocusable (true);

        return popupWindow;
    }
}
