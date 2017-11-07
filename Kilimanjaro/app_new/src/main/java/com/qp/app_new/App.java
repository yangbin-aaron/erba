package com.qp.app_new;

import android.app.Application;
import android.content.Context;

/**
 * Created by Aaron on 17/11/7.
 */

public class App extends Application {
    public static Context mContext;

    @Override
    public void onCreate () {
        super.onCreate ();
        mContext = this;
    }
}
