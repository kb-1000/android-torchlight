buildscript {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        google()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.0.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.21")
    }
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}

tasks.named<Wrapper>("wrapper") {
    distributionType = Wrapper.DistributionType.BIN
    gradleVersion = "7.1.1"
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}
