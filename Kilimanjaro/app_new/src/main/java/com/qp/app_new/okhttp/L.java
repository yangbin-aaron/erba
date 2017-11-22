package com.qp.app_new.okhttp;

import com.qp.app_new.utils.LogUtil;

/**
 * Created by Andy on 15/11/9.
 */
public class L
{
    private static boolean debug = false;

    public static void e(String msg)
    {
        if (debug)
        {
            LogUtil.e("OkHttp", msg);
        }
    }

}

