package com.skysoul.buildsrc

object Versions {
    const val ktlint = "0.45.1"
}

object Libs {
    const val androidGradlePlugin = "com.android.tools.build:gradle:7.1.2"
    const val jdkDesugar = "com.android.tools:desugar_jdk_libs:1.1.5"

    object Accompanist {
        const val version = "0.24.6-alpha"
        const val pager = "com.google.accompanist:accompanist-pager:$version"
        const val systemuicontroller = "com.google.accompanist:accompanist-systemuicontroller:$version"
        const val flowlayouts = "com.google.accompanist:accompanist-flowlayout:$version"
    }

    object Kotlin {
        private const val version = "1.6.21"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
        const val extensions = "org.jetbrains.kotlin:kotlin-android-extensions:$version"
    }

    object Coroutines {
        private const val version = "1.6.4"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
    }

    object OkHttp {
        private const val version = "4.9.1"
        const val okhttp = "com.squareup.okhttp3:okhttp:$version"
        const val logging = "com.squareup.okhttp3:logging-interceptor:$version"
    }

    object JUnit {
        private const val version = "4.13"
        const val junit = "junit:junit:$version"
    }

    object AndroidX {
        const val appcompat = "androidx.appcompat:appcompat:1.4.1"
        const val palette = "androidx.palette:palette:1.0.0"

        const val coreKtx = "androidx.core:core-ktx:1.7.0"

        object Activity {
            const val activityCompose = "androidx.activity:activity-compose:1.4.0"
        }

        object Constraint {
            const val constraintLayoutCompose = "androidx.constraintlayout:constraintlayout-compose:1.0.0"
        }

        object Compose {
            const val snapshot = ""
            const val version = "1.2.0-alpha07"

            @get:JvmStatic
            val snapshotUrl: String
                get() = "https://androidx.dev/snapshots/builds/$snapshot/artifacts/repository/"

            const val runtime = "androidx.compose.runtime:runtime:$version"
            const val foundation = "androidx.compose.foundation:foundation:${version}"
            const val layout = "androidx.compose.foundation:foundation-layout:${version}"

            const val ui = "androidx.compose.ui:ui:${version}"
            const val material = "androidx.compose.material:material:${version}"
            const val materialIconsExtended = "androidx.compose.material:material-icons-extended:${version}"

            const val tooling = "androidx.compose.ui:ui-tooling:${version}"
            const val toolingPreview = "androidx.compose.ui:ui-tooling-preview:${version}"
        }

        object Lifecycle {
            private const val version = "2.4.1"
            const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
            const val viewModelCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:$version"
            const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
        }

        object Navigation {
            const val navigation = "androidx.navigation:navigation-compose:2.4.1"
        }

        object Room {
            private const val version = "2.4.2"
            const val runtime = "androidx.room:room-runtime:${version}"
            const val ktx = "androidx.room:room-ktx:${version}"
            const val compiler = "androidx.room:room-compiler:${version}"
        }

        object Window {
            const val window = "androidx.window:window:1.0.0"
        }



    }

    object Koin {
        const val version = "2.0.1"
        const val andriod = "org.koin:koin-android:${version}"
        const val scope = "org.koin:koin-androidx-scope:$version"
        const val viewmodel = "org.koin:koin-androidx-viewmodel:$version"
    }

    object Gson{
        const val version = "2.8.6"
        const val gson = "com.google.code.gson:gson:$version"
    }
}
