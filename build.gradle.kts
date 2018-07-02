buildscript {
    repositories {
        jcenter()
        gradlePluginPortal()
        google()
    }

    dependencies {
        classpath(Deps.gradlePlugins.android)
        classpath(Deps.gradlePlugins.kotlin)
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
