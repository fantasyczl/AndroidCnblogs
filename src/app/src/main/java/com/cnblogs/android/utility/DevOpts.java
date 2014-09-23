package com.cnblogs.android.utility;

import android.os.StrictMode;

/**
 * 开发选项
 * Created by ygl on 14-9-16.
 */
public class DevOpts {
    /**
     * 是否开启严格模式
     */
    private static final boolean DEVELOPER_MODE = false;

    public static boolean isDeveloperMode() {
        return DEVELOPER_MODE;
    }

    public static void setThreadPolicy() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
        .detectDiskReads()
        .detectDiskWrites()
        .detectNetwork()
        .detectAll()
        .penaltyLog()
                .penaltyDeath()
                .penaltyDropBox()
        .build());
    }

    public static void setVmPolicy() {
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
        .detectAll()
        .penaltyLog()
        .penaltyDeath()
        .build());
    }
}
