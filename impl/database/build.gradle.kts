import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinCocoapods)
    alias(libs.plugins.serialization)
    alias(libs.plugins.sqldelight)
    id("io.lb.android.library")
}

kotlin {
    androidTarget {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_17)
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
            baseName = "implDatabase"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.sqldelight.android.driver)
        }
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.sqldelight.runtime)
            implementation(libs.sqldelight.coroutines.extensions)
            implementation(libs.kotlin.datetime)
            implementation(project(":common:local"))
            implementation(project(":common:common_shared"))
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.kotlin.test.annotations.common)
        }
        iosMain.dependencies {
            implementation(libs.sqldelight.native.driver)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.kotlin.test.annotations.common)
        }
    }
}

android {
    namespace = "io.lb.middleware.impl.client"
}

sqldelight {
    databases {
        create("MiddlewareDatabase") {
            packageName = "io.lb.middleware.impl.client"
        }
    }
}
