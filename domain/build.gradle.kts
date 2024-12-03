plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.challenge.colour_spotter.domain"
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
    implementation(project(":common"))
    implementation(project(":data"))

    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation(libs.hilt.compose)

    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.work.runtime.ktx)
    kapt(libs.androidx.hilt.compiler)

    testImplementation(libs.junit)
    testImplementation (libs.mockk)
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.9.10")
    testImplementation(libs.kotlinx.coroutines.test)
}