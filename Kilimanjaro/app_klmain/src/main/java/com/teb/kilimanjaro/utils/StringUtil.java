package com.teb.kilimanjaro.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.teb.kilimanjaro.App;
import com.teb.kilimanjaro.httpnetwork.JsonEntry;
import com.teb.kilimanjaro.httpnetwork.NetWorkManager;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yangbin on 16/7/4.
 */
public class StringUtil {

    /**
     * 封装json
     *
     * @param agrs
     * @return
     */
    public static String getJson(HashMap<String, Object> agrs) {
        String json = null;
        JsonEntry jsonEntry = new JsonEntry();
        jsonEntry.setArgs(agrs);
        jsonEntry.setSession(NetWorkManager.getInstance().getSession());
        json = new Gson().toJson(jsonEntry);
        return json;
    }

    /**
     * 封装一个空得json请求对象
     *
     * @return
     */
    public static String getNullJson() {
        Log.e("aaa", getJson(new HashMap<String, Object>()));
        return getJson(new HashMap<String, Object>());
    }

    /**
     * 获取版本信息的时候使用
     * @return
     */
    public static String getVersionJson(){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("versionName", AppUtils.getAppVersionName());
        hashMap.put("type", 0);
        return getJson(hashMap);
    }

    /**
     * 验证是否为手机号码
     *
     * @return
     */
    public static boolean isPhone(String phone) {
        // 国内手机号码
        String cn_str = "^(((13[0-9])|(15([0-4]|[5-9]))|(18[0,0-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$";
        Pattern regex_cn = Pattern.compile(cn_str);
        Matcher matcher_cn = regex_cn.matcher(phone);
        return matcher_cn.matches();
    }

    /**
     * 验证是否为邮箱
     *
     * @return
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static final String DATA_FORMAT_YMDHMSZZZZZ = "yyyy-MM-dd'T'HH:mm:ssZ";// 时间格式
    public static final String DATA_FORMAT_HM = "HH:mm";// 时间格式
    public static final String DATA_FORMAT_YMDHMS = "yy/MM/dd HH:mm:ss";// 时间格式

    /**
     * Date转化为String
     *
     * @param format
     * @param time
     * @return
     */
    public static Date stringToDate(String format, String time) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * string转化为Date
     *
     * @param format
     * @param date
     * @return
     */
    public static String dateToString(String format, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * String  到  String
     *
     * @param fromFormat
     * @param toFormat
     * @return
     */
    public static String stringToString(String fromFormat, String toFormat, String time) {
        return dateToString(toFormat, stringToDate(fromFormat, time));
    }

    /**
     * 读取Assets里面的文件的内容
     *
     * @param fileName
     * @return
     */
    public static String getAssetsFileContent(String fileName) {
        String text = "";
        try {
            InputStream is = App.getAppContext().getAssets().open(fileName);
            int size = is.available();

            // Read the entire asset into a local byte buffer.  
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            // Convert the buffer into a string.  
            text = new String(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }
}
