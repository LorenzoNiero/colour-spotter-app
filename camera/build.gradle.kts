plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.challenge.colour_spotter.camera"
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
    implementation(libs.hilt.android)
    implementation(project(":common"))
    implementation(project(":ui"))

    kapt(libs.hilt.android.compiler)
    implementation(libs.hilt.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    debugImplementation(libs.ui.tooling)

    implementation(libs.kotlinx.coroutines.android)

    implementation (libs.androidx.camera.core)
    implementation (libs.androidx.camera.view)
    implementation(libs.androidx.camera.camera2)
    implementation (libs.androidx.camera.lifecycle)
    implementation(libs.androidx.palette.ktx)

    //permission
    implementation (libs.accompanist.permissions)
}