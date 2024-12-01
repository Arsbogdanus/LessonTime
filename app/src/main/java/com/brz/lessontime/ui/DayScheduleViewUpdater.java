package com.brz.lessontime.ui;

import android.widget.TextView;

import com.brz.lessontime.databinding.ActivityMainBinding;
import com.brz.lessontime.databinding.ViewDayBinding;

import java.util.ArrayList;

public class DayScheduleViewUpdater {
    private final ActivityMainBinding binding;

    public DayScheduleViewUpdater(ActivityMainBinding binding) {
        this.binding = binding;
    }

    public void updateDaySchedule(String day, ArrayList<String> lessons) {
        switch (day) {
            case "Monday":
                updateDay(binding.mainMonday, lessons);
                break;
            case "Tuesday":
                updateDay(binding.mainTuesday, lessons);
                break;
            case "Wednesday":
                updateDay(binding.mainWednesday, lessons);
                break;
            case "Thursday":
                updateDay(binding.mainThursday, lessons);
                break;
            case "Friday":
                updateDay(binding.mainFriday, lessons);
                break;
            case "Saturday":
                updateDay(binding.mainSaturday, lessons);
                break;
            case "Sunday":
                updateDay(binding.mainSunday, lessons);
                break;
        }
    }

    private void updateDay(ViewDayBinding dayBinding, ArrayList<String> lessons) {
        for (int i = 0; i < lessons.size(); i++) {
            int lessonTextViewId = dayBinding.getRoot().getResources()
                .getIdentifier("lessonText" + (i + 1), "id", dayBinding.getRoot().getContext().getPackageName());
            TextView lessonTextView = dayBinding.getRoot().findViewById(lessonTextViewId);
            if (lessonTextView != null) {
                lessonTextView.setText(lessons.get(i));
            }
        }
    }
}

