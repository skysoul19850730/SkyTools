import java.io.FileInputStream
import java.util.Properties
import org.gradle.kotlin.dsl.implementation
import org.gradle.kotlin.dsl.invoke

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}
android {
    namespace = "com.skysoul.accountremebercompose"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.skysoul.accountremebercompose"
        minSdk = 24
        targetSdk = 35
        versionCode = 8
        versionName = "1.0.7"
//        versionName = "1.0.${autoAddVersionCode()}"
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
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }
    buildFeatures {
        compose = true
    }
//    composeOptions {
//        kotlinCompilerExtensionVersion = "1.5.15"
//    }
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
    implementation(fileTree("libs") {
        include("*.jar", "*.aar")
    })
    implementation(project(":ComposeLib"))  // ComposeLib 已经用 api 暴露了所有 Compose 依赖
    implementation(project(":appAssistant"))


    implementation(libs.gson)
    implementation(libs.bundles.room)

    // 移除重复的 Compose 依赖，全部由 ComposeLib 提供
    // implementation(platform("androidx.compose:compose-bom:2026.03.01"))
    // implementation("androidx.compose.ui:ui")
    // implementation("androidx.compose.ui:ui-graphics")
    // implementation("androidx.compose.ui:ui-tooling-preview")
    // implementation("androidx.compose.material3:material3")
    // implementation(libs.bundles.compose)


    kapt(libs.roomkapt)


}