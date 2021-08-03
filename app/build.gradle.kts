plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
}

repositories {
    mavenCentral()
    google()
}

android {
    compileSdk = 27
    defaultConfig {
        applicationId = "com.fake.android.torchlight"
        minSdk = 16
        targetSdk = 27
        versionCode = 207
        versionName = "2.7"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            isDebuggable = false
            isJniDebuggable = false
            isRenderscriptDebuggable = false
            isMinifyEnabled = true
        }

        getByName("debug") {
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            isDebuggable = true
            isJniDebuggable = true
            isRenderscriptDebuggable = true
            isMinifyEnabled = false
        }
    }

    lint {
        isAbortOnError = false
    }
}

dependencies {
    implementation("com.android.support:appcompat-v7:27.1.1")
    implementation("com.android.support:design:27.1.1")
    implementation("com.android.support:support-v4:27.1.1")
    implementation("com.android.support:support-vector-drawable:27.1.1")

    implementation("com.jakewharton.timber:timber:4.7.1")
    implementation(kotlin("stdlib-jdk8"))
}
