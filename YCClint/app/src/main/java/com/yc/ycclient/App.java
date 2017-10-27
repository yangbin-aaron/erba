package com.yc.ycclient;

import android.app.Application;
import android.content.Context;

/**
 * Created by Aaron on 17/10/25.
 */

public class App extends Application {
    private static Context mContext;

    public static Context getAppContext() {
        return mContext;
    }

    @Override
    public void onCreate () {
        super.onCreate ();
        mContext = this;
    }
}
