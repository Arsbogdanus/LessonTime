package com.brz.lessontime;

import com.brz.lessontime.data.LessonData;

import java.util.List;
import java.util.Map;

public class SchoolSchedule {
    private String className;
    private Map<String, List<LessonData>> schedule;

    // Геттеры
    public String getClassName() {
        return className;
    }

    public Map<String, List<LessonData>> getSchedule() {
        return schedule;
    }
}