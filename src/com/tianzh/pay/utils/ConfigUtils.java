package com.tianzh.pay.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by pig on 2015-09-28.
 */
public class ConfigUtils {

    private static SharedPreferences sharedPreferences;
    private static String name = "TIANZHCONFIG";
    private static SharedPreferences.Editor editor;

    public static boolean setConfig(Context context, String key, String value) {
        if (editor == null) editor = context.getSharedPreferences(name, Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public static String getConfig(Context context, String key) {
        if (sharedPreferences == null) sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);

        return sharedPreferences.getString(key, "");
    }
}
