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
include(":common:common_shared")
include(":common:state")
include(":impl:client")
include(":impl:database")
include(":middleware:middleware_data")
include(":middleware:middleware_domain")
include(":history:history_data")
include(":history:history_domain")
include(":user:user_data")
include(":user:user_domain")
include(":sign_up:sign_up_data")
include(":sign_up:sign_up_domain")
include(":splash:splash_data")
include(":splash:splash_domain")
include(":shared")
