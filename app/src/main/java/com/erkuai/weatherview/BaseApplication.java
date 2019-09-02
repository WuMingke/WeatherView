package com.erkuai.weatherview;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2019/9/2.
 */

public class BaseApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getContext() {
        return context;
    }
}
