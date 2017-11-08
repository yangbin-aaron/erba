package com.qp.app_new.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.qp.app_new.App;

import java.security.MessageDigest;

/**
 * Created by yangbin on 16/7/5.
 * 网络相关的工具类
 */
public class NetUtil {

    /**
     * 判断网络是否连接
     */
    public static boolean checkNet () {
        ConnectivityManager cm = (ConnectivityManager) App.mContext
                .getSystemService (Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo ();
        return info != null;// 网络是否连接
    }

    /**
     * Md5
     */
    public static String MD5(String s) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            byte[] strTemp = s.getBytes("UTF-8");
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
    
}
