object Deps {
    object versions {
        const val androidGradlePlugin = "3.1.3"
        const val gradle = "4.8.1"
        const val androidSupport = "27.1.1"
        const val androidSdk = 27
        const val timber = "4.7.0"
        const val kotlin = "1.2.50"
    }

    object gradlePlugins {
        const val android = "com.android.tools.build:gradle:${versions.androidGradlePlugin}"
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${versions.kotlin}"
    }

    object libraries {
        const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${versions.kotlin}"
    }
}
