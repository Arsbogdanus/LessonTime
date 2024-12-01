package com.brz.lessontime.domain.services;

import android.content.Context;
import android.util.Log;

import com.brz.lessontime.databinding.ActivityMainBinding;
import com.brz.lessontime.preferences.Utils;
import com.brz.lessontime.ui.DayScheduleViewUpdater;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScheduleManager {
    private final DatabaseReference databaseReference;
    private final DayScheduleViewUpdater viewUpdater;
    private final Gson gson;

    public ScheduleManager(Context context, ActivityMainBinding binding) {
        databaseReference = FirebaseDatabase.getInstance("https://lessontime-7570e-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("schedule");
        viewUpdater = new DayScheduleViewUpdater(binding);
        gson = new Gson();
    }

    public void loadSchedule() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Собираем данные с Firebase
                Map<String, ArrayList<String>> scheduleMap = new HashMap<>();

                for (DataSnapshot daySnapshot : dataSnapshot.getChildren()) {
                    String day = daySnapshot.getKey();
                    ArrayList<String> lessons = new ArrayList<>();
                    for (DataSnapshot lessonSnapshot : daySnapshot.getChildren()) {
                        String lessonDetails = lessonSnapshot.child("number").getValue(Integer.class) + ": " +
                            lessonSnapshot.child("item").getValue(String.class);
                        lessons.add(lessonDetails);
                    }
                    scheduleMap.put(day, lessons);
                    viewUpdater.updateDaySchedule(day, lessons);
                }

                // Сохраняем данные в SharedPreferences
                String scheduleJson = gson.toJson(scheduleMap);
                Utils.getPreferencesHelper().saveSchedule(scheduleJson);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ScheduleManager", "Failed to load schedule: " + databaseError.getMessage());
            }
        });
    }

    public void loadScheduleFromJson(String jsonSchedule) {
        // Преобразуем JSON в Map и обновляем UI
        Map<String, ArrayList<String>> scheduleMap = gson.fromJson(jsonSchedule, Map.class);
        for (Map.Entry<String, ArrayList<String>> entry : scheduleMap.entrySet()) {
            String day = entry.getKey();
            ArrayList<String> lessons = entry.getValue();
            viewUpdater.updateDaySchedule(day, lessons);
        }
    }
}
