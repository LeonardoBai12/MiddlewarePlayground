plugins {
    //trick: for the same plugin versions in all sub-modules
    alias(libs.plugins.androidApplication).apply(false)
    alias(libs.plugins.androidLibrary).apply(false)
    alias(libs.plugins.kotlinAndroid).apply(false)
    alias(libs.plugins.kotlinMultiplatform).apply(false)
    alias(libs.plugins.kotlinCocoapods).apply(false)
    alias(libs.plugins.compose.compiler).apply(false)
    alias(libs.plugins.serialization).apply(false)
    alias(libs.plugins.ktor.plugin).apply(false)
    alias(libs.plugins.sqldelight).apply(false)
    alias(libs.plugins.hilt.android) apply false
}
