package com.brz.lessontime;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("Email Address [Required]")
    private String email;

    @SerializedName("Password [Required]")
    private String password;

    // Конструкторы, геттеры и сеттеры
    public User() {}

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}

