plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}
android {
    namespace = "com.skysoul.accountremebercompose"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.skysoul.accountremebercompose"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}


dependencies {
    implementation(libs.bundles.lifecycle)
    implementation(libs.corektx)
    implementation(libs.gson)
    implementation(libs.appCompat)
    implementation(libs.bundles.room)
    implementation(libs.bundles.accompanist)


    implementation(platform("androidx.compose:compose-bom:2024.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation(libs.bundles.compose)

    kapt("androidx.room:room-compiler:2.6.1")
//    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.22";
//    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4"
//
//
//    implementation "androidx.appcompat:appcompat:1.6.1"
//    implementation "com.google.accompanist:accompanist-systemuicontroller:0.24.6-alpha"
//
//    implementation "androidx.core:core-ktx:1.10.1"
//    implementation "androidx.palette:palette-ktx:1.0.0"
//
//    implementation "androidx.activity:activity-compose:1.7.2"
//
//    implementation "androidx.constraintlayout:constraintlayout-compose:1.0.1"
//
//    implementation "androidx.compose.foundation:foundation:1.4.3"
//    implementation "androidx.compose.material:material:1.4.3"
//    implementation "androidx.compose.material:material-icons-extended:1.4.3"
//    implementation "androidx.compose.ui:ui-tooling-preview:1.4.3"
//    implementation "androidx.compose.ui:ui:$compose_version"
//    implementation "androidx.compose.material:material:$compose_version"
//    implementation "androidx.compose.ui:ui-tooling-preview:$compose_version"
//
//    implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.6.1'
//    implementation 'androidx.activity:activity-compose:1.8.0-alpha06'
//    androidTestImplementation 'androidx.compose.ui:ui-test-junit4:1.6.0-alpha01'
//    debugImplementation "androidx.compose.ui:ui-tooling:1.4.3"
//
//    implementation "androidx.lifecycle:lifecycle-runtime-ktx:2.6.1"
//    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1"
//    implementation "androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1"
//    implementation "androidx.navigation:navigation-compose:2.6.0"
//
//    implementation "androidx.window:window:1.1.0"
//
////    implementation Libs.Accompanist.pager //类似viewpager，使用再打开
//
//    implementation "androidx.room:room-runtime:2.5.2"
//    implementation "androidx.room:room-ktx:2.5.2"
//
//    implementation "org.koin:koin-android:2.2.1"
//    implementation "org.koin:koin-androidx-scope:2.2.1"
//    implementation "org.koin:koin-androidx-viewmodel:2.2.1"
//    implementation "com.google.code.gson:gson:2.9.0"
//    debugImplementation "androidx.compose.ui:ui-tooling:$compose_version"
//
//    kapt "androidx.room:room-compiler:2.5.2"

//    coreLibraryDesugaring Libs.jdkDesugar


}