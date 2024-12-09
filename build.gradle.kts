// Top-level build file where you can add configuration options common to all sub-projects/modules.
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
}

buildscript {
    repositories {
    // Considera eliminar jcenter() si no es necesario
    }
}

allprojects {
    repositories {
        // No incluyas repositories aquí si ya están en settings.gradle.kts
    }
}