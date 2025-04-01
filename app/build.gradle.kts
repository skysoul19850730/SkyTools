import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}
android {
    namespace = "com.skysoul.accountremebercompose"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.skysoul.accountremebercompose"
        minSdk = 24
        targetSdk = 35
        versionCode = 8
        versionName = "1.0.${autoAddVersionCode()}"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {

//       getByName("debug").apply{
//            versionCode = 11
//        }

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
        kotlinCompilerExtensionVersion = "1.5.15"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

fun autoAddVersionCode():Int {
    val pFile = file("../gradle.properties")
    val props = Properties()
    props.load(FileInputStream(pFile))
    var code = props["versionCodeDebug"].toString().toInt()
    code++
    props["versionCodeDebug"] = code.toString()
    props.store(pFile.writer(), null)
    return code
}

dependencies {


    implementation(project(":ComposeLib"))
    implementation(project(":appAssistant"))


    implementation(libs.gson)
    implementation(libs.bundles.room)


    implementation(platform("androidx.compose:compose-bom:2024.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation(libs.bundles.compose)
    implementation(libs.bundles.lifecycle)
    implementation(libs.corektx)
    implementation(libs.appCompat)
    implementation(libs.bundles.accompanist)

    api("androidx.core:core-ktx:1.15.0")
    api("androidx.appcompat:appcompat:1.7.0")
    api("com.google.android.material:material:1.12.0")



    kapt("androidx.room:room-compiler:2.6.1")


}