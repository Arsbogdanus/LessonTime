package com.brz.lessontime;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.brz.lessontime.databinding.ActivityMainBinding;
import com.brz.lessontime.databinding.ViewDayBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUi();
        initListeners();

        // Проверка аутентификации пользователя
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }

        // Установка цвета статус-бара
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.screenBackgroundColor));

            // Для темных значков на светлом фоне (Android 6.0+)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                View decor = window.getDecorView();
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }

        // Инициализация Firebase Database
        databaseReference = FirebaseDatabase.getInstance("https://lessontime-7570e-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("schedule");

        // Получаем расписание с Firebase
        loadScheduleFromFirebase();
    }

    private void initUi() {
        binding.mainMonday.dayTitle.setText(getString(R.string.monday));
        binding.mainTuesday.dayTitle.setText(getString(R.string.tuesday));
        binding.mainWednesday.dayTitle.setText(getString(R.string.wednesday));
        binding.mainThursday.dayTitle.setText(getString(R.string.thursday));
        binding.mainFriday.dayTitle.setText(getString(R.string.friday));
        binding.mainSaturday.dayTitle.setText(getString(R.string.saturday));
        binding.mainSunday.dayTitle.setText(getString(R.string.sunday));
    }

    private void initListeners() {
        // Здесь можно добавить обработчики событий для кнопок или других элементов UI
    }

    // Метод для загрузки расписания из Firebase
    private void loadScheduleFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Обрабатываем данные для каждого дня недели
                for (DataSnapshot daySnapshot : dataSnapshot.getChildren()) {
                    String day = daySnapshot.getKey();
                    ArrayList<String> lessons = new ArrayList<>();

                    // Заполняем список уроков для текущего дня
                    for (DataSnapshot lessonSnapshot : daySnapshot.getChildren()) {
                        String lessonDetails = lessonSnapshot.child("номер").getValue(Integer.class) + ": " +
                            lessonSnapshot.child("предмет").getValue(String.class);
                        lessons.add(lessonDetails);
                    }

                    // Обновляем UI с расписанием для этого дня
                    updateDaySchedule(day, lessons);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("MainActivity", "Failed to load schedule: " + databaseError.getMessage());
            }
        });
    }

    // Метод для обновления расписания для конкретного дня
    private void updateDaySchedule(String day, ArrayList<String> lessons) {
        switch (day) {
            case "Понедельник":
                updateDay(binding.mainMonday, lessons);
                break;
            case "Вторник":
                updateDay(binding.mainTuesday, lessons);
                break;
            case "Среда":
                updateDay(binding.mainWednesday, lessons);
                break;
            case "Четверг":
                updateDay(binding.mainThursday, lessons);
                break;
            case "Пятница":
                updateDay(binding.mainFriday, lessons);
                break;
            case "Суббота":
                updateDay(binding.mainSaturday, lessons);
                break;
            case "Воскресенье":
                updateDay(binding.mainSunday, lessons);
                break;
        }
    }

    // Метод для обновления расписания для конкретного дня
    private void updateDay(ViewDayBinding dayBinding, ArrayList<String> lessons) {
        // Привязываем TextView по порядку
        for (int i = 0; i < lessons.size(); i++) {
            // Получаем TextView для каждого урока (в соответствии с порядковым номером)
            int lessonTextViewId = getResources().getIdentifier("lessonText" + (i + 1), "id", getPackageName());
            TextView lessonTextView = dayBinding.getRoot().findViewById(lessonTextViewId);

            // Устанавливаем текст урока в TextView
            if (lessonTextView != null) {
                lessonTextView.setText(lessons.get(i));
            }
        }
    }
}
