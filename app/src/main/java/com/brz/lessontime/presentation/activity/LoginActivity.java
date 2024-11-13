package com.brz.lessontime.presentation.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.brz.lessontime.R;
import com.brz.lessontime.service.UserService;
import com.brz.lessontime.databinding.LoginActivityBinding;

public class LoginActivity extends AppCompatActivity {

    private LoginActivityBinding binding;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LoginActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Устанавливаем цвет статус-бара
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.screenBackgroundColor));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }

        // Инициализация сервиса
        userService = new UserService();

        // Обработчик нажатия кнопки "Войти"
        binding.loginButton.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getApplicationContext(), R.string.fields_cannot_be_empty, Toast.LENGTH_SHORT).show();
            } else {
                validateUser(email, password);
            }
        });
    }

    private void validateUser(String email, String password) {
        userService.validateUser(email, password, new UserService.UserValidationCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(LoginActivity.this, R.string.login_successful, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(LoginActivity.this, R.string.incorrect_login_or_password, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(LoginActivity.this, R.string.database_connection_error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
