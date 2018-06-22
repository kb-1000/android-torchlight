buildscript {
    repositories {
        jcenter()
        gradlePluginPortal()
        google()
    }

    dependencies {
        classpath(Deps.gradlePlugins.android)
        classpath("gradle.plugin.org.gradle.android:android-cache-fix-gradle-plugin:+")
    }
}

allprojects {
    repositories {
        mavenCentral()
        jcenter()
        google()
    }
}

val wrapper: Wrapper by tasks
wrapper {
    distributionType = Wrapper.DistributionType.ALL
    gradleVersion = Deps.versions.gradle
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}
