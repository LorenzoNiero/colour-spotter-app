import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.hilt) apply false
}

allprojects {
    afterEvaluate {
        if (hasProperty("android")) {

            configure<com.android.build.gradle.BaseExtension> {
                buildFeatures.buildConfig = true
                compileSdkVersion(extra["android.compileSdkVersion"].toString().toInt())
                defaultConfig {
                    minSdk = extra["android.minSdk"].toString().toInt()
                    targetSdk = extra["android.targetSdk"].toString().toInt()
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_11
                    targetCompatibility = JavaVersion.VERSION_11
                }

                tasks.withType<KotlinCompile>().all {
                    kotlinOptions {
                        jvmTarget = JavaVersion.VERSION_11.toString()
                    }
                }
            }
        }
    }
}
