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
    gradleVersion = "5.4"
    distributionSha256Sum = "f177768e7a032727e4338c8fd047f8f263e5bd283f67a7766c1ba4182c8455a6"
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}
