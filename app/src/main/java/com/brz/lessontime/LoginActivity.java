package com.brz.lessontime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.brz.lessontime.databinding.LoginActivityBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private LoginActivityBinding binding;
    private DatabaseReference databaseReference;

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

        // Получаем ссылку на базу данных
        databaseReference = FirebaseDatabase.getInstance("https://lessontime-7570e-default-rtdb.europe-west1.firebasedatabase.app").getReference("users");

        // Обработчик нажатия кнопки "Войти"
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.etEmail.getText().toString().trim();
                String password = binding.etPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.fields_cannot_be_empty, Toast.LENGTH_SHORT).show();
                    return;
                }

                validateUser(email, password);
            }
        });
    }

    private void validateUser(String email, String password) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean userFound = false;

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String dbEmail = userSnapshot.child("email").getValue(String.class);
                    String dbPassword = userSnapshot.child("password").getValue(String.class);

                    if (email.equals(dbEmail) && password.equals(dbPassword)) {
                        userFound = true;
                        break;
                    }
                }

                if (userFound) {
                    Toast.makeText(LoginActivity.this, R.string.login_successful, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, R.string.incorrect_login_or_password, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, R.string.database_connection_error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
