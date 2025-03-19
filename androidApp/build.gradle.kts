plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.sqldelight)
    kotlin("kapt")
}

android {
    namespace = "io.lb.middleware.android"
    compileSdk = 35
    defaultConfig {
        applicationId = "io.lb.middleware.android"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(projects.shared)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.hilt.android)
    implementation(libs.ktor.core)
    implementation(libs.ktor.serialization)
    implementation(libs.ktor.serialization.json)
    implementation(libs.sqldelight.runtime)
    implementation(libs.sqldelight.coroutines.extensions)
    debugImplementation(libs.compose.ui.tooling)
    kapt(libs.hilt.compiler)
    kapt(libs.hilt.android.compiler)
    implementation(project(":common:remote"))
    implementation(project(":common:local"))
    implementation(project(":common:shared"))
    implementation(project(":common:state"))
    implementation(project(":impl:client"))
    implementation(project(":impl:database"))
    implementation(project(":middleware:data"))
    implementation(project(":middleware:domain"))
    implementation(project(":history:data"))
    implementation(project(":history:domain"))
    implementation(project(":user:data"))
    implementation(project(":user:domain"))
    implementation(project(":sign_up:data"))
    implementation(project(":sign_up:domain"))
    implementation(project(":splash:data"))
    implementation(project(":splash:domain"))
}