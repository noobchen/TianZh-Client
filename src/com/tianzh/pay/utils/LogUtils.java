package com.tianzh.pay.utils;

import android.util.Log;
import com.tianzh.pay.constant.Constant;

/**
 * Created by pig on 2015-11-12.
 */
public class LogUtils {

    private static boolean logSwitch = true;
    public static String TAG = "TIANZHDEBUG";

    public static void d(String log) {
        if (logSwitch)
            Log.i(TAG, log);
    }

    public static void w(String log) {
        if (logSwitch)
            Log.w(TAG, log);
    }

    public static void e(String log) {
        if (logSwitch)
            Log.e(TAG, log);
    }
}
