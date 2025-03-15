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
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.ktor.android)
            implementation(libs.sqldelight.android.driver)
        }
        commonMain.dependencies {
            implementation(libs.ktor.core)
            implementation(libs.ktor.serialization)
            implementation(libs.ktor.serialization.json)
            implementation(libs.sqldelight.runtime)
            implementation(libs.sqldelight.coroutines.extensions)
            implementation(libs.kotlin.datetime)
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
