package com.teb.kilimanjaro.utils;

import android.widget.Toast;

import com.teb.kilimanjaro.App;

/**
 * Created by yangbin on 16/7/1.
 * Toast工具类
 */
public class ToastUtil {

    public static void showToast(int resId){
        Toast.makeText(App.getAppContext(),resId,Toast.LENGTH_SHORT).show();
    }

    public static void showToast(String res){
        Toast.makeText(App.getAppContext(),res,Toast.LENGTH_SHORT).show();
    }
}
