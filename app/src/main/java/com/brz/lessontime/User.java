package com.brz.lessontime;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("Email Address [Required]")
    private String email;

    @SerializedName("Password [Required]")
    private String password;

    @SerializedName("Org Unit Path [Required]")
    private String schoolClass;

    // Конструкторы, геттеры и сеттеры
    public User() {
    }

    public User(String email, String password, String schoolClass) {
        this.email = email;
        this.password = password;
        this.schoolClass = schoolClass;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getSchoolClass(){
        return schoolClass.replace("/Ученики/", "");
    }
}

