import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.daggerHilt) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.safe.args) apply false
}
true // Needed to make the Suppress annotation work for the plugins block

fun BaseExtension.baseConfig() {

    compileSdkVersion(34/*AppConfig.compileSdk*/)

    defaultConfig.apply {
        minSdk = 21 // AppConfig.minSdk
        targetSdk = 34 // AppConfig.targetSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions.apply {
        sourceCompatibility = JavaVersion.VERSION_17 // AppConfig.CompileOptions.javaSourceCompatibility
        targetCompatibility = JavaVersion.VERSION_17 //AppConfig.CompileOptions.javaSourceCompatibility
    }

    buildFeatures.apply {
        buildConfig = true
        viewBinding = true
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "17" //AppConfig.CompileOptions.kotlinJvmTarget
        }
    }
}

/**
 * Apply configuration settings that are shared across all modules.
 */
fun PluginContainer.applyBaseConfig(project: Project) {
    whenPluginAdded {
        when (this) {
            is AppPlugin -> {
                project.extensions
                    .getByType<AppExtension>()
                    .apply {
                        baseConfig()
                    }
            }
            is LibraryPlugin -> {
                project.extensions
                    .getByType<LibraryExtension>()
                    .apply {
                        baseConfig()
                    }
            }
        }
    }
}

subprojects {
    project.plugins.applyBaseConfig(project)
}