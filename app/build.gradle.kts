plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
}

android {
    namespace = "com.example.store2"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.store2"
        minSdk = 26
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
}
hilt {
    enableExperimentalClasspathAggregation = true
}
dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("com.google.firebase:firebase-firestore-ktx:25.0.0")
    implementation ("com.google.firebase:firebase-storage:21.0.0")
    implementation("com.google.firebase:firebase-firestore:25.0.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")



    //dependency for ciruclar imageView
    implementation("de.hdodenhof:circleimageview:3.1.0")

// Glide
    implementation("com.github.bumptech.glide:glide:4.13.0")

    implementation ("androidx.fragment:fragment-ktx:1.7.0")

// Viewpager2 indicator
    implementation("androidx.viewpager2:viewpager2:1.0.0")


// Define the navigation version
    val navVersion = "2.7.7"

// Implementation for navigation-fragment-ktx
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")

// Implementation for navigation-ui-ktx
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

    implementation("br.com.simplepass:loading-button-android:2.2.0")
// Dagger Hilt
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-compiler:2.48")
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.48")
    implementation("androidx.fragment:fragment-ktx:1.7.0")
    kapt("androidx.hilt:hilt-compiler:1.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0")

    implementation("com.shuhart.stepview:stepview:1.5.1")
// Firebase
    implementation("com.google.firebase:firebase-auth:23.0.0")

// Coroutines with Firebase
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")
}

kapt {
    correctErrorTypes = true
}
