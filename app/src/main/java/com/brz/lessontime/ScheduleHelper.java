package com.brz.lessontime;

import android.content.Context;
import android.util.Log;

import com.brz.lessontime.data.LessonData;
import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class ScheduleHelper {
    private static final String TAG = "ScheduleHelper";

    public static SchoolSchedule loadSchedule(Context context) {
        try {
            InputStream inputStream = context.getAssets().open("schedule.json");
            InputStreamReader reader = new InputStreamReader(inputStream);

            Gson gson = new Gson();
            SchoolSchedule schoolSchedule = gson.fromJson(reader, SchoolSchedule.class);

            inputStream.close();
            return schoolSchedule;
        } catch (Exception e) {
            Log.e(TAG, "Ошибка при загрузке расписания", e);
            return null;
        }
    }

    public static List<LessonData> getScheduleForClass(SchoolSchedule schoolSchedule, String schoolClass) {
        if (schoolSchedule != null && schoolSchedule.getClassName().equals(schoolClass)) {
            return (List<LessonData>) schoolSchedule.getSchedule();
        }
        return null; // Если класс не найден
    }
}

