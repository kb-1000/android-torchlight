buildscript {
    repositories {
        jcenter()
        google()
        gradlePluginPortal()
    }

    dependencies {
        classpath(Deps.gradlePlugins.android)
        classpath(Deps.gradlePlugins.kotlin)
    }
}

allprojects {
    repositories {
        jcenter()
        google()
        mavenCentral()
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
