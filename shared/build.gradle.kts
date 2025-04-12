import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

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
                    jvmTarget.set(JvmTarget.JVM_17)
                }
            }
        }
    }
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


    val xcf = XCFramework("Shared")

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = "Shared"
            binaryOption("bundleId", "io.lb.middleware.shared")
            binaryOption("bundleVersion", "0.0.1")
            embedBitcode("disable")
            xcf.add(this)
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
            implementation(libs.kotlinx.coroutines.test)
            implementation(kotlin("test"))
            implementation(libs.kotlin.test.annotations.common)
        }
        iosMain.dependencies {
            implementation(libs.ktor.ios)
            implementation(libs.sqldelight.native.driver)
        }
        commonTest.dependencies {
            implementation(libs.kotlinx.coroutines.test)
            implementation(kotlin("test"))
            implementation(libs.kotlin.test.annotations.common)
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
