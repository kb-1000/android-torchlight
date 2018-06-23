buildscript {
    repositories {
        jcenter()
        gradlePluginPortal()
        google()
    }

    dependencies {
        classpath(Deps.gradlePlugins.android)
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
