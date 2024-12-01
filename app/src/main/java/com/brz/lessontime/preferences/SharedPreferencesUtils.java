package com.brz.lessontime.preferences;

public interface SharedPreferencesUtils {

    void saveSchedule(String json);

    String getSchedule();

    void removeSchedule();
}