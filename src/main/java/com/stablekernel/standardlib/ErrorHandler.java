package com.stablekernel.standardlib;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

public class ErrorHandler {
    public static void handleError(Context context, Throwable e) {
        ErrorHandler.handleError(context, e.getLocalizedMessage(), e);
    }

    public static void handleError(Context context, @NonNull String message, Throwable e) {
        Log.e(context.getClass().getSimpleName(), e.getLocalizedMessage(), e);
        if (BuildConfig.DEBUG ) {
           Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }
}
