@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    // KSP
    alias(libs.plugins.ksp)
    // Hilt
    alias(libs.plugins.daggerHilt)
    // Sage Args
    alias(libs.plugins.safe.args)
}

android {
    namespace = "app.qup.srk_management"
}

dependencies {

    implementation(project(":core:ui"))
    implementation(project(":core:util"))
    implementation(project(":core:network"))

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Navigation
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    // ViewModel LiveData
    implementation(libs.androidx.lifecycle.livedata)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewmodel)
    // Dagger Hilt
    implementation(libs.dagger.hilt)
    ksp(libs.dagger.hilt.compiler)
    // Retrofit
    implementation(libs.squareup.retrofit2.retrofit)
    implementation(libs.squareup.retrofit2.converter.gson)
    // Logging Interceptor
    implementation(libs.squareup.logging.interceptor)
    // Paging
    implementation(libs.androidx.paging)
    // Joda Time
    implementation(libs.joda.time)
}