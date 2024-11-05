package com.brz.lessontime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.brz.lessontime.data.SchoolData;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

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

        // Инициализация объектов
        emailEditText = findViewById(R.id.etEmail); // ID для поля email
        passwordEditText = findViewById(R.id.etPassword); // ID для поля пароля
        Button loginButton = findViewById(R.id.loginButton); // ID для кнопки входа

        // Инициализация объекта Gson
        gson = new Gson();

        // Настройка кнопки "Log in"
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Проверка введенной электронной почты
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String token = task.getResult();
                            Log.d("FCM", "Token: " + token);
                            // Отправьте токен на сервер или используйте его для других целей
                        } else {
                            Log.w("FCM", "Fetching FCM registration token failed", task.getException());
                        }
                    });

                if (!isValidEmail(email)) {
                    Toast.makeText(LoginActivity.this, "Введите корректный email", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Проверка учетных данных
                if (checkUserCredentials(email, password)) {
                    // Успешный вход
                    Toast.makeText(LoginActivity.this, "Вход успешен", Toast.LENGTH_SHORT).show();
                    // Переход к MainActivity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Неверные учетные данные
                    Toast.makeText(LoginActivity.this, "Неверные email или пароль", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean checkUserCredentials(String email, String password) {
        try (InputStream inputStream = getAssets().open("school.json");
             InputStreamReader reader = new InputStreamReader(inputStream)) {

            // Создаем тип для корневого JSON-объекта, который содержит массив users
            Type dataType = new TypeToken<SchoolData>() {
            }.getType();

            // Десериализуем JSON-файл
            SchoolData data = gson.fromJson(reader, dataType);

            if (data == null || data.users == null) {
                Toast.makeText(this, "Данные не найдены", Toast.LENGTH_SHORT).show();
                return false;
            }

            // Проверяем учетные данные
            for (User user : data.users) {
                if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка при загрузке данных", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private boolean isValidEmail(String email) {
        // Обновленный шаблон, который поддерживает домены с несколькими уровнями
        String emailPattern = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
