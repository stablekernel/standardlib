package com.stablekernel.standardlib;

import android.content.Context;

public class DisplayUtils {

    public static float dpFromPixels(Context context, int pixels) {
        return pixels / context.getResources().getDisplayMetrics().density;
    }

    public static float pixelsFromDp(Context context, int dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static int getScreenWidthInPixels(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeightInPixels(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }
}
