package com.brz.lessontime.ui;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.brz.lessontime.R;
import com.brz.lessontime.databinding.ActivityMainBinding;
import com.brz.lessontime.domain.services.ScheduleManager;
import com.brz.lessontime.preferences.NetworkUtils;
import com.brz.lessontime.preferences.Utils;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ScheduleManager scheduleManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Utils.init(this); // Инициализация Utils

        initUi();
        initListeners();

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

        setUpStatusBar();

        // Проверка подключения к интернету
        if (NetworkUtils.isNetworkAvailable(this)) {
            // Если интернет доступен, загружаем расписание с сервера
            Toast.makeText(this, "ЕСТЬ ИНЕТ", Toast.LENGTH_LONG).show();
            scheduleManager = new ScheduleManager(this, binding);
            scheduleManager.loadSchedule();
        } else {
            // Если интернет отсутствует, загружаем расписание из SharedPreferences
            Toast.makeText(this, "НЕТУ ИНЕТА", Toast.LENGTH_LONG).show();
            loadScheduleFromPreferences();
        }
    }

    private void loadScheduleFromPreferences() {
        String savedSchedule = Utils.getPreferencesHelper().getSchedule();

        if (savedSchedule != null) {
            // Если расписание найдено в SharedPreferences, обновляем UI
            Log.d("MainActivity", "Loading schedule from SharedPreferences");
            scheduleManager = new ScheduleManager(this, binding);
            scheduleManager.loadScheduleFromJson(savedSchedule);  // Метод для загрузки расписания из JSON
            Toast.makeText(this, "Offline schedule loaded", Toast.LENGTH_SHORT).show();
        } else {
            // Если расписание не найдено
            Toast.makeText(this, "No saved schedule found", Toast.LENGTH_SHORT).show();
        }
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
        // Обработчики событий
    }

    private void setUpStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.screenBackgroundColor));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                View decor = window.getDecorView();
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }
}
