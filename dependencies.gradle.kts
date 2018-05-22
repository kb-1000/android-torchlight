object versions {
    const val androidGradlePlugin = "3.1.2"
    const val gradle = "4.5.1"
    const val androidSupport = "27.1.1"
    const val androidSdk = 27
    const val timber = "4.7.0"
}

extra["versions"] = versions

object gradlePlugins {
    const val android = "com.android.tools.build:gradle:${versions.androidGradlePlugin}"
}

extra["gradlePlugins"] = gradlePlugins
