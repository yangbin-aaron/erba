package com.qp.app_new.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.qp.app_new.App;

import java.security.MessageDigest;
import java.util.List;

/**
 * App 版本名称等
 * Created by Andy on 15/7/23.
 */
public class AppUtils {
    private AppUtils() {
        throw new UnsupportedOperationException("AppUtil cannot instantiated");
    }

    /**
     * 获取应用程序版本信息
     */
    public static PackageInfo getAppPackageInfo(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取app版本名
     */
    public static String getAppVersionName() {
        PackageManager pm = App.mContext.getPackageManager();
        PackageInfo pi;
        try {
            pi = pm.getPackageInfo(App.mContext.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取app版本号
     */
    public static int getAppVersionCode(Context context) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi;
        try {
            pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取应用签名
     *
     * @param context
     * @param pkgName
     */
    public static String getSign(Context context, String pkgName) {
        try {
            PackageInfo pis = context.getPackageManager().getPackageInfo(
                    pkgName, PackageManager.GET_SIGNATURES);
            return hexdigest(pis.signatures[0].toByteArray());
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("AppUtils  the " + pkgName + "'s application not found");
        }
    }

    /**
     * 将签名字符串转换成需要的32位签名
     */
    private static String hexdigest(byte[] paramArrayOfByte) {
        final char[] hexDigits = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97,
                98, 99, 100, 101, 102};
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramArrayOfByte);
            byte[] arrayOfByte = localMessageDigest.digest();
            char[] arrayOfChar = new char[32];
            for (int i = 0, j = 0; ; i++, j++) {
                if (i >= 16) {
                    return new String(arrayOfChar);
                }
                int k = arrayOfByte[i];
                arrayOfChar[j] = hexDigits[(0xF & k >>> 4)];
                arrayOfChar[++j] = hexDigits[(k & 0xF)];
            }
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * 判断某APP是否安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppAvilible(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断微信是否已经安装
     *
     * @param context
     * @return
     */
    public static boolean isWeixinAvilible(Context context) {
        return isAppAvilible(context, "com.tencent.mm");
    }

    /**
     * 判断qq是否可用
     *
     * @param context
     * @return
     */
    public static boolean isQQClientAvailable(Context context) {
        return isAppAvilible(context, "com.tencent.mobileqq");
    }

    /**
     * 修改清单文件里面的meta-data里面的值
     *
     * @param context
     * @param metaDataKey
     * @param metaDataValue
     */
    public static void setManifestMetaDataValue(Context context, String metaDataKey, String metaDataValue) {
        ApplicationInfo appi;
        try {
            appi = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            appi.metaData.putString(metaDataKey, metaDataValue);
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("exp", e1.getMessage());
            e1.printStackTrace();
        }
    }
}
