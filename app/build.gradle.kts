plugins {
    id("com.android.application")
    id("kotlin-android")
}

repositories {
    mavenCentral()
    google()
}

android {
    compileSdk = 30
    defaultConfig {
        applicationId = "de.kb1000.flashlight"
        minSdk = 16
        targetSdk = 27
        versionCode = 207
        versionName = "2.7"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            isDebuggable = false
            isJniDebuggable = false
            isRenderscriptDebuggable = false
            isMinifyEnabled = true
        }

        debug {
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
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.vectordrawable:vectordrawable:1.1.0")

    implementation("com.jakewharton.timber:timber:4.7.1")
    implementation(kotlin("stdlib-jdk8"))
}
