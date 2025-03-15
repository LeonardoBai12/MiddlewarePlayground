enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Middleware_Playground"
include(":androidApp")
include(":common:data")
include(":common:state")
include(":impl:client")
include(":impl:database")
include(":middleware:data")
include(":middleware:domain")
include(":shared")
