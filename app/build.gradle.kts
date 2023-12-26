import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.abhi.networkinfofortasker"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.abhi.networkinfofortasker"
        minSdk = 23
        targetSdk = 34
        versionCode = 10
        versionName = "0.1"
        archivesName = "NetworkInfo4Tasker-v$versionName"
    }

    buildTypes {
        debug {
            isMinifyEnabled = true
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
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("com.google.android.material:material:1.11.0")
    implementation("com.joaomgcd:taskerpluginlibrary:0.4.10")
    implementation("com.google.code.gson:gson:2.10")
}