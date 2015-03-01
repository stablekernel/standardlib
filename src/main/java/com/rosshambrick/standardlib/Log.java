package com.rosshambrick.standardlib;

/**
 * This Log class is a proxy to the standard android.util.Log except
 * that it will disable debug logging for release builds.
 */
public class Log {
    private static boolean DEBUG = true;

    /**
     * Since gradle does not support passing the BuildConfig.DEBUG value
     * down to library modules, you will need to call this this method in
     * your Application.onCreate() method like so:
     *
     *      Log.setDebug(BuildConfig.DEBUG);
     *
     * @param isDebug
     */
    public static void setDebug(boolean isDebug) {
        DEBUG = isDebug;
    }

    public static void d(String tag, String msg) {
        if (DEBUG) android.util.Log.d(tag, msg);
    }

    public static void e(String tag, String msg, Throwable e) {
        android.util.Log.e(tag, msg, e);
    }

    public static void w(String tag, String msg) {
        android.util.Log.w(tag, msg);
    }
}
