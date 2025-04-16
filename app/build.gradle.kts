plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.download)
}

android {
    namespace = "com.mangaversetest"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.mangaversetest"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    androidResources {
        noCompress += "tflite"
    }
}



dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.runtime.livedata)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)

    /* Hilt Dagger */
    implementation(libs.hilt.android)
    ksp(libs.hilt.kapt)
    implementation(libs.hilt.navigation.compose)


    implementation(libs.retrofit)
    implementation(libs.logging.interceptor)
    implementation(libs.converter.gson)

    /*RxJava lib*/
    implementation(libs.rxandroid)
    implementation(libs.adapter.rxjava3)

    implementation(libs.coil.compose)

    /* Room with RXJava*/
    implementation (libs.rxjava2)
    // See Add the KSP plugin to your project
    ksp(libs.androidx.room.compiler)


    // Camera
    implementation(libs.bundles.camera)

    //mediapipe Face Detection
    implementation(libs.mediapipe.vision)
}


// === Define Asset Directory ===
val assetDir = "${projectDir}/src/main/assets"

// Set the assetDir property so that it's available in the download_models.gradle file
ext["assetDir"] = assetDir

// Apply download_models.gradle
apply(from = "download_models.gradle")
