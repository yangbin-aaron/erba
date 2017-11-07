package com.qp.app_new.utils;

import android.widget.Toast;

import com.qp.app_new.App;


/**
 * Created by yangbin on 16/7/1.
 * Toast工具类
 */
public class ToastUtil {

    public static void showToast (int resId) {
        Toast.makeText (App.mContext, resId, Toast.LENGTH_SHORT).show ();
    }

    public static void showToast (String res) {
        Toast.makeText (App.mContext, res, Toast.LENGTH_SHORT).show ();
    }
}
