object Deps {
    object versions {
        const val androidGradlePlugin = "3.1.4"
        const val gradle = "4.10"
        const val androidSupport = "28.0.0-rc01"
        const val androidSdk = 28
        const val timber = "4.7.1"
        const val kotlin = "1.2.61"
    }

    object gradlePlugins {
        const val android = "com.android.tools.build:gradle:${versions.androidGradlePlugin}"
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${versions.kotlin}"
    }

    object libraries {
        const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${versions.kotlin}"
    }
}
