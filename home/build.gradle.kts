plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    // Sage Args
    alias(libs.plugins.safe.args)
}

android {
    namespace = "app.qup.home"
}

dependencies {

    implementation(project(":core:ui"))
    implementation(project(":feature:doctor_management"))
    implementation(project(":feature:reception_management"))
    implementation(project(":feature:srk_management"))
    implementation(project(":feature:entity_management"))
    implementation(project(":feature:summary"))
    implementation(project(":feature:commcredits"))
    implementation(project(":feature:indiapps"))

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Navigation
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
}