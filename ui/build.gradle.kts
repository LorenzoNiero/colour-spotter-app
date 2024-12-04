plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.challenge.colour_spotter.ui"

    flavorDimensions += "environment"
    productFlavors {
        create("dev") {
            dimension = "environment"
        }
        create("prod") {
            dimension = "environment"
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.kotlinCompilerExtension.get().toString()
    }
}

dependencies {
    implementation(project(":common"))

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    debugImplementation(libs.ui.tooling)
}