package com.brz.lessontime;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
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
        emailEditText = findViewById(R.id.etEmail); // ID для вашего поля ввода email
        passwordEditText = findViewById(R.id.etPassword); // ID для вашего поля ввода пароля
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

                if (!isValidEmail(email)) {
                    Toast.makeText(LoginActivity.this, "Введите корректный email", Toast.LENGTH_SHORT).show();
                    return; // Если email некорректен, выходим из метода
                }

                // Проверка учетных данных
                if (checkUserCredentials(email, password)) {
                    // Успешный вход
                    Toast.makeText(LoginActivity.this, "Вход успешен", Toast.LENGTH_SHORT).show();
                    // Переход к MainActivity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Закрываем LoginActivity
                } else {
                    // Неверные учетные данные
                    Toast.makeText(LoginActivity.this, "Неверные email или пароль", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean checkUserCredentials(String email, String password) {
        try {
            // Чтение JSON файла из assets
            InputStream inputStream = getAssets().open("school.json");
            InputStreamReader reader = new InputStreamReader(inputStream);

            // Определяем тип данных
            Type userListType = new TypeToken<List<User>>() {}.getType();

            // Преобразуем JSON в список пользователей
            List<User> users = gson.fromJson(reader, userListType);

            // Закрываем InputStream
            inputStream.close();

            // Проверка учетных данных
            for (User user : users) {
                if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                    return true; // Учетные данные верны
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false; // Учетные данные неверны
    }

    private boolean isValidEmail(String email) {
        // Регулярное выражение для проверки формата электронной почты
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches(); // Возвращает true, если email соответствует паттерну
    }
}
