import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinCocoapods)
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
        summary = "Some description for the client Module"
        homepage = "Link to the client Module homepage"
        version = "1.0"
        ios.deploymentTarget = "16.0"
        podfile = project.file("../../iosApp/Podfile")
        framework {
            baseName = "client"
            isStatic = false
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.ktor.android)
        }
        commonMain.dependencies {
            implementation(libs.ktor.core)
            implementation(libs.ktor.serialization)
            implementation(libs.ktor.serialization.json)
            implementation(project(":common:data"))
        }
        iosMain.dependencies {
            implementation(libs.ktor.ios)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "io.lb.middleware.client"
}
