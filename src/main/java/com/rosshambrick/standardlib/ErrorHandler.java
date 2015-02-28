package com.rosshambrick.standardlib;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class ErrorHandler {
    public static void handleError(Context context, Throwable e) {
        ErrorHandler.handleError(context, e.getLocalizedMessage(), e);
    }

    public static void handleError(Context context, String message, Throwable e) {
        Log.e(context.getClass().getSimpleName(), e.getLocalizedMessage(), e);
        if (BuildConfig.DEBUG || message != null) {
            Toast.makeText(context, message == null ? e.getLocalizedMessage() : message, Toast.LENGTH_LONG).show();
        }
    }
}
