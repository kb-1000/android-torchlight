plugins {
    id("com.android.application")
    id("org.gradle.android.cache-fix") version "0.5.1"
    id("kotlin-android")
    id("kotlin-android-extensions")
}

android {
    compileSdkVersion(Deps.versions.androidSdk)
    buildToolsVersion = "27.0.3"
    defaultConfig {
        applicationId = "com.fake.android.torchlight"
        minSdkVersion(16)
        targetSdkVersion(Deps.versions.androidSdk)
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
            isZipAlignEnabled = true
        }

        getByName("debug") {
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            isDebuggable = true
            isJniDebuggable = true
            isRenderscriptDebuggable = true
            isMinifyEnabled = false
            isZipAlignEnabled = true
        }
    }

    lintOptions {
        isAbortOnError = false
    }
}

dependencies {
    implementation("com.android.support:appcompat-v7:${Deps.versions.androidSupport}")
    implementation("com.android.support:design:${Deps.versions.androidSupport}")
    implementation("com.android.support:support-v4:${Deps.versions.androidSupport}")
    implementation("com.android.support:support-vector-drawable:${Deps.versions.androidSupport}")

    implementation("com.jakewharton.timber:timber:${Deps.versions.timber}")
    implementation(Deps.libraries.kotlinStdlib)
}
