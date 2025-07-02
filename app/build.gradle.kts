plugins {
    id("com.android.application")
    // If you're using Kotlin for your Android app code (which is common)
    // you'll also need the Kotlin Android plugin:
    // id("org.jetbrains.kotlin.android")
}

android {
    // Your Android specific configurations will go here
    namespace = "com.example.simplequizapp" // Replace with your actual package name
    compileSdk = 34 // Or your desired SDK version

    defaultConfig {
        applicationId = "com.example.simplequizapp" // Replace with your actual package name
        minSdk = 24 // Or your desired minimum SDK
        targetSdk = 34 // Or your desired target SDK
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    // If you showed me your java version configuration,
    // it means you're likely using Java, so the compileOptions block is correct.
    // If you were to add Kotlin code later, you'd also add kotlinOptions.
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    // If you start using Kotlin:
    // kotlinOptions {
    //    jvmTarget = "11"
    // }
}

dependencies {
    // Your app's dependencies will be listed here, for example:
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}