package com.cnblogs.android;

import android.app.Application;

import com.cnblogs.android.utility.DevOpts;

/**
 * 重写程序类
 * Created by ygl on 14-9-16.
 */
public class GlobalContext extends Application {

    private static GlobalContext mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        if (DevOpts.isDeveloperMode()) {
            DevOpts.setThreadPolicy();
            DevOpts.setVmPolicy();
        }
    }

    public static GlobalContext getInstance() {
        return mInstance;
    }
}
