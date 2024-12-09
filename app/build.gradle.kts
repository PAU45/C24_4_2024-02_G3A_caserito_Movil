plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("kapt")
}

android {
    namespace = "com.melendez.paulo.frontend_proyecto"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.melendez.paulo.frontend_proyecto"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("com.caverock:androidsvg:1.4")
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("androidx.fragment:fragment-ktx:1.3.6")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation("com.github.denzcoskun:ImageSlideshow:0.1.0")
    implementation("com.google.android.gms:play-services-maps:19.0.0")
    implementation("com.github.bumptech.glide:glide:4.15.0")
    implementation("com.auth0.android:jwtdecode:2.0.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation(libs.androidx.activity)
    implementation("androidx.cardview:cardview:1.0.0")
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    implementation ("androidx.recyclerview:recyclerview:1.2.1")
    implementation ("androidx.viewpager2:viewpager2:1.0.0")
    implementation("androidx.security:security-crypto:1.1.0-alpha03")
    implementation("androidx.security:security-crypto:1.0.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("com.github.bumptech.glide:glide:4.12.0")
    kapt("com.github.bumptech.glide:compiler:4.12.0")
    // Material Components
    implementation ("com.google.android.material:material:1.9.0")
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")
    // Glide for image loading
    implementation ("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.15.1")

    // Lottie for animations
    implementation ("com.airbnb.android:lottie:6.0.0")

    // Shimmer for loading effects
    implementation ("com.facebook.shimmer:shimmer:0.5.0")
}