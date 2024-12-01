package com.brz.lessontime.preferences;

import android.content.Context;

public class Utils {
    private static SharedPreferencesUtils sharedPreferencesUtils;

    public static void init(Context context) {
        if (sharedPreferencesUtils == null) {
            sharedPreferencesUtils = new SharedPreferencesUtilsImpl(context);
        }
    }
    public static SharedPreferencesUtils getPreferencesHelper() {
        if (sharedPreferencesUtils == null) {
            throw new IllegalStateException("Utils not initialized. Call Utils.init(context) first.");
        }
        return sharedPreferencesUtils;
    }
}

