package com.brz.lessontime.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtilsImpl implements SharedPreferencesUtils {

    private static final String PREFERENCES_NAME = "LessonTimePreferences";
    private static final String SCHEDULE_KEY = "schedule_key";

    private final SharedPreferences sharedPreferences;

    public SharedPreferencesUtilsImpl(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public void saveSchedule(String json) {
        sharedPreferences.edit().putString(SCHEDULE_KEY, json).apply();
    }

    @Override
    public String getSchedule() {
        return sharedPreferences.getString(SCHEDULE_KEY, null);
    }

    @Override
    public void removeSchedule() {
        sharedPreferences.edit().remove(SCHEDULE_KEY).apply();
    }
}