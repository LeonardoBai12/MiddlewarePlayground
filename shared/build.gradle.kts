import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinCocoapods)
    alias(libs.plugins.sqldelight)
    id("io.lb.android.library")
}

kotlin {
    androidTarget {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_1_8)
                }
            }
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        ios.deploymentTarget = "16.0"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
            isStatic = false
            export(project(":common:remote"))
            export(project(":common:local"))
            export(project(":common:common_shared"))
            export(project(":common:state"))
            export(project(":impl:client"))
            export(project(":impl:database"))
            export(project(":middleware:middleware_data"))
            export(project(":middleware:middleware_domain"))
            export(project(":history:history_data"))
            export(project(":history:history_domain"))
            export(project(":user:user_data"))
            export(project(":user:user_domain"))
            export(project(":sign_up:sign_up_data"))
            export(project(":sign_up:sign_up_domain"))
            export(project(":splash:splash_data"))
            export(project(":splash:splash_domain"))
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.kotlinx.coroutines.android)
        }
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlin.datetime)
            implementation(project(":common:remote"))
            implementation(project(":common:local"))
            implementation(project(":common:common_shared"))
            implementation(project(":common:state"))
            implementation(project(":impl:client"))
            implementation(project(":impl:database"))
            implementation(project(":middleware:middleware_data"))
            implementation(project(":middleware:middleware_domain"))
            implementation(project(":history:history_data"))
            implementation(project(":history:history_domain"))
            implementation(project(":user:user_data"))
            implementation(project(":user:user_domain"))
            implementation(project(":sign_up:sign_up_data"))
            implementation(project(":sign_up:sign_up_domain"))
            implementation(project(":splash:splash_data"))
            implementation(project(":splash:splash_domain"))
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        iosMain.dependencies {
            implementation(libs.ktor.ios)
            implementation(libs.sqldelight.native.driver)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "io.lb.middleware.shared"
}

sqldelight {
    databases {
        create("TranslateDatabase") {
            packageName = "io.lb.middleware.shared"
        }
    }
}
