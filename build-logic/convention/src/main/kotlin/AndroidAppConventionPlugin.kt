import com.android.build.api.dsl.ApplicationExtension
import extensions.androidTestImplementation
import extensions.configureKotlinAndroid
import extensions.debugImplementation
import extensions.implementation
import extensions.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import provider.libs

/**
 * Plugin to apply Android app conventions.
 */
class AndroidAppConventionPlugin : Plugin<Project> {
    /**
     * Applies the Android app conventions to the project.
     *
     * @param target The project to apply the conventions to.
     */
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.plugin.serialization")
                apply("org.jetbrains.kotlin.android")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            extensions.configure<ApplicationExtension> {
                buildFeatures {
                    compose = true
                }
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = libs.findVersion("targetSdk")
                    .get()
                    .toString()
                    .toInt()
            }

            dependencies {
                with(libs) {
                    implementation(findLibrary("androidx-activity-compose").get())
                    implementation(findLibrary("compose-ui").get())
                    implementation(findLibrary("compose-foundation").get())
                    implementation(findLibrary("compose-material3").get())
                    implementation(findLibrary("compose-icons-extended").get())
                    testImplementation(findLibrary("turbine").get())
                    testImplementation(findLibrary("assertk").get())
                    testImplementation(findLibrary("kotlinx-coroutines-test").get())
                    debugImplementation(findLibrary("compose-ui-tooling-preview").get())
                    debugImplementation(findLibrary("compose-ui-tooling").get())
                }
            }
        }
    }
}
