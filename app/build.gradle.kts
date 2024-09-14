plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.kapt)
    alias(libs.plugins.google.ksp)
}

android {
    namespace = "com.terabyte.mediastorage"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.terabyte.mediastorage"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    //room local database
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)

    //chucker to debug https requests from retrofit
    debugImplementation(libs.chucker)
    releaseImplementation(libs.chucker.release)

    //preferences datastore to keep important user data in the phone memory
    implementation(libs.androidx.datastore.preferences)

    //moshi
    implementation(libs.moshi)
    ksp(libs.moshi.codegen)

    //navigation
    implementation(libs.androidx.navigation.compose)

    //dagger for DI
    implementation(libs.google.dagger)
    kapt(libs.google.dagger.compiler)

    //timber for smart logging
    implementation(libs.timber)
    
    //retrofit with moshi json converter
    implementation(libs.squareup.retrofit)
    implementation(libs.squareup.retrofit.moshi.converter)
    implementation("com.squareup.retrofit2:converter-gson:2.5.0")

    //constraintLayout in Compose
    implementation(libs.androidx.constraintLayout)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}