buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.google.gms:google-services:4.4.2") // Плагин для Firebase
    }
}

plugins {
    id("com.android.application") version "8.2.1" apply false // для проектов приложения
    id("com.google.gms.google-services") version "4.4.2" apply false // для Firebase плагина
}
