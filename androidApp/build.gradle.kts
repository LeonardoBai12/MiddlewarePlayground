plugins {
    id("io.lb.android.app")
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.sqldelight)
    kotlin("kapt")
}

android {
    namespace = "io.lb.middleware.android"
}

dependencies {
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.androidx.activity.compose)
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.ktor.core)
    implementation(libs.ktor.serialization)
    implementation(libs.ktor.serialization.json)
    implementation(libs.sqldelight.runtime)
    implementation(libs.sqldelight.coroutines.extensions)
    debugImplementation(libs.compose.ui.tooling)
    kapt(libs.hilt.compiler)
    kapt(libs.hilt.android.compiler)
    implementation(projects.shared)
    implementation(projects.splash.domain)
    implementation(projects.splash.data)
    implementation(projects.common.remote)
    implementation(projects.common.local)
    implementation(projects.common.shared)
    implementation(projects.common.state)
    implementation(projects.impl.client)
    implementation(projects.impl.database)
    implementation(projects.middleware.data)
    implementation(projects.middleware.domain)
    implementation(projects.history.data)
    implementation(projects.history.domain)
    implementation(projects.user.data)
    implementation(projects.user.domain)
    implementation(projects.signUp.data)
    implementation(projects.signUp.domain)
}