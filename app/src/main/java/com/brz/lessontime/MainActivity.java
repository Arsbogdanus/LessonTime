package com.brz.lessontime;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.brz.lessontime.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUi();
        initListeners();


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

    }
}
