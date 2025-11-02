plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.Clover.prueba"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.Clover.prueba"
        minSdk = 33
        targetSdk = 36
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
}

dependencies {
    //implementation("con.airbnb.android:lottie:3.0.1")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)// CameraX
    implementation("androidx.activity:activity-ktx:1.8.0")
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation("com.google.zxing:core:3.4.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation ("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.15.1")
    implementation(libs.constraintlayout)
    implementation(libs.play.services.mlkit.barcode.scanning)
    implementation(libs.firebase.crashlytics.buildtools)
    implementation(libs.recyclerview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.junit.junit)
}