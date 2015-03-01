package com.rosshambrick.standardlib;

public class Log {
    public static void d(String tag, String msg) {
        if (BuildConfig.DEBUG) android.util.Log.d(tag, msg);
    }

    public static void e(String tag, String msg, Throwable e) {
        android.util.Log.e(tag, msg, e);
    }

    public static void w(String tag, String msg) {
        android.util.Log.w(tag, msg);
    }
}
