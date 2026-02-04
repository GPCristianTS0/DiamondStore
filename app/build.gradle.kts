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
    // ---- Dependencias base de AndroidX (versiones más recientes) ----
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.activity:activity-ktx:1.8.0")
    implementation(libs.constraintlayout) // Asumo que libs.constraintlayout apunta a una versión estable
    implementation(libs.recyclerview)     // Asumo que libs.recyclerview apunta a una versión estable

    // ---- Gráficos ----
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // ---- Escáner de código de barras (ZXing y ML Kit) ----
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    // Nota: 'com.google.zxing:core' ya viene incluida en zxing-android-embedded, por lo que no necesitas declararla de nuevo.
    implementation(libs.play.services.mlkit.barcode.scanning)

    // ---- Carga de imágenes (Glide) ----
    implementation("com.github.bumptech.glide:glide:4.15.1")
    implementation(libs.activity)
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")

    // ---- Componentes de ciclo de vida (Lifecycle) ----
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)

    // ---- Dependencias de Testing (sin cambios) ----
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    // 'junit:junit' está duplicado si libs.junit ya es 'junit:junit'. Lo puedes eliminar si es el caso.
    // androidTestImplementation(libs.junit.junit)
}
