plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.challenge.colour_spotter.data"
    flavorDimensions += "environment"
    productFlavors {
        create("dev") {
            dimension = "environment"
        }
        create("prod") {
            dimension = "environment"
        }
    }

}

dependencies {
    implementation(libs.hilt.android)
    implementation(project(":common"))
    api(project(":network"))
    api(project(":database"))

    kapt(libs.hilt.android.compiler)
    implementation(libs.hilt.compose)

    implementation(libs.kotlinx.coroutines.android)

    testImplementation(libs.junit)
    testImplementation (libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
}