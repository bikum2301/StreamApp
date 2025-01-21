plugins {
    alias(libs.plugins.android.application) // Sử dụng plugin Android Application
}

android {
    namespace = "tdp.bikum.myapplication" // Namespace của ứng dụng
    compileSdk = 34                       // Phiên bản SDK để biên dịch

    defaultConfig {
        applicationId = "tdp.bikum.myapplication" // Application ID
        minSdk = 21                                // Min SDK version
        targetSdk = 34                             // Target SDK version
        versionCode = 1                            // Version code
        versionName = "1.0"                        // Version name

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" // Test runner
    }

    buildTypes {
        release {
            isMinifyEnabled = false // Tắt minify (rút gọn code) trong bản release
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            ) // Sử dụng file Proguard mặc định và file tùy chỉnh
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11 // Java 11 compatibility
        targetCompatibility = JavaVersion.VERSION_11
    }

    viewBinding {
        enable = true // Bật ViewBinding
    }

    // Bật tính năng build features (nếu cần)
    buildFeatures {
        viewBinding = true
        dataBinding = false // Tắt DataBinding nếu không sử dụng
    }

    // Cấu hình lint để bỏ qua một số cảnh báo không cần thiết
    lint {
        abortOnError = false // Không dừng build khi gặp lỗi lint
        disable += listOf("MissingTranslation", "ExtraTranslation") // Vô hiệu hóa một số cảnh báo lint
    }
}

dependencies {
    // Core dependencies
    implementation(libs.appcompat)             // AndroidX AppCompat
    implementation(libs.material)              // Material Design
    implementation(libs.activity)              // AndroidX Activity
    implementation(libs.constraintlayout)      // ConstraintLayout

    // Testing dependencies
    testImplementation(libs.junit)             // JUnit for unit testing
    androidTestImplementation(libs.ext.junit)  // AndroidX JUnit for instrumentation testing
    androidTestImplementation(libs.espresso.core) // Espresso for UI testing

    // Networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")         // Retrofit
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")   // Gson converter for Retrofit
    implementation("com.google.code.gson:gson:2.8.8")               // Gson

    // UI
    implementation("androidx.recyclerview:recyclerview:1.3.1")      // RecyclerView
    implementation("com.google.android.material:material:1.9.0")    // Material Components

    // Splash Screen API (cho Android 12+)
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("com.google.android.material:material:1.10.0")

}