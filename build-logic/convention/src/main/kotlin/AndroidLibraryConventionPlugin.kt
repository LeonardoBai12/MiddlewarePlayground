import com.android.build.api.dsl.LibraryExtension
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
 * Plugin to apply Android library conventions.
 */
class AndroidLibraryConventionPlugin : Plugin<Project> {
    /**
     * Applies the Android library conventions to the project.
     *
     * @param target The project to apply the conventions to.
     */
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
            }

            dependencies {
                with(libs) {
                    testImplementation(findLibrary("turbine").get())
                    testImplementation(findLibrary("assertk").get())
                    testImplementation(findLibrary("kotlinx-coroutines-test").get())
                }
            }
        }
    }
}