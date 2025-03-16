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
include(":common:remote")
include(":common:local")
include(":common:shared")
include(":common:state")
include(":impl:client")
include(":impl:database")
include(":middleware:data")
include(":middleware:domain")
include(":history:data")
include(":history:domain")
include(":user:data")
include(":user:domain")
include(":sign_up:data")
include(":sign_up:domain")
include(":splash:data")
include(":splash:domain")
include(":shared")
