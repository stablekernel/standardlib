package com.rosshambrick.standardlib;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;

import java.lang.reflect.Field;

import static android.os.Build.VERSION.*;
import static android.os.Build.VERSION_CODES.*;

public class NetworkUtils {
    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return !(networkInfo == null || !networkInfo.isConnectedOrConnecting());
    }

    /**
     * Checks whether the "Avoid poor networks" setting (named "Auto network switch" on
     * some Samsung devices) is enabled, which can in some instances interfere with Wi-Fi.
     *
     * @return true if the "Avoid poor networks" or "Auto network switch" setting is enabled
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isWatchdogEnabled(Context context) {
        final int SETTING_UNKNOWN = -1;
        final int SETTING_ENABLED = 1;
        final String AVOID_POOR = "wifi_watchdog_poor_network_test_enabled";
        final String WATCHDOG_CLASS = "android.net.wifi.WifiWatchdogStateMachine";
        final String DEFAULT_ENABLED = "DEFAULT_POOR_NETWORK_AVOIDANCE_ENABLED";
        final ContentResolver contentResolver = context.getContentResolver();

        int result;

        switch (SDK_INT) {
            case JELLY_BEAN_MR1:
                //Setting was moved from Secure to Global as of JB MR1
                result = Settings.Global.getInt(contentResolver, AVOID_POOR, SETTING_UNKNOWN);
                break;
            case ICE_CREAM_SANDWICH_MR1:
                result = Settings.Secure.getInt(contentResolver, AVOID_POOR, SETTING_UNKNOWN);
                break;
            default:
                //Poor network avoidance not introduced until ICS MR1
                //See android.provider.Settings.java
                return false;
        }

        //Exit here if the setting value is known
        if (result != SETTING_UNKNOWN) {
            return (result == SETTING_ENABLED);
        }

        //Setting does not exist in database, so it has never been changed.
        //It will be initialized to the default value.
        if (SDK_INT >= JELLY_BEAN_MR1) {
            //As of JB MR1, a constant was added to WifiWatchdogStateMachine to determine
            //the default behavior of the Avoid Poor Networks setting.
            try {
                //In the case of any failures here, take the safe route and assume the
                //setting is disabled to avoid disrupting the user with false information
                Class wifiWatchdog = Class.forName(WATCHDOG_CLASS);
                Field defValue = wifiWatchdog.getField(DEFAULT_ENABLED);
                if (!defValue.isAccessible()) {
                    defValue.setAccessible(true);
                }
                return defValue.getBoolean(null);
            } catch (IllegalAccessException ex) {
                return false;
            } catch (NoSuchFieldException ex) {
                return false;
            } catch (ClassNotFoundException ex) {
                return false;
            } catch (IllegalArgumentException ex) {
                return false;
            }
        } else {
            //Prior to JB MR1, the default for the Avoid Poor Networks setting was
            //to enable it unless explicitly disabled
            return true;
        }
    }

    public static boolean activityExists (Context context, Intent intent) {
        final PackageManager mgr = context.getPackageManager();
        final ResolveInfo info = mgr.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return (info != null);
    }
}
