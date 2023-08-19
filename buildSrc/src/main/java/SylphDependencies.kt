package com.vallem.sylph.build_configuration

object SylphDependencies {
    object Versions {
        object Android {
            object Compose {
                const val Bom = "2023.08.00"
                const val Activity = "1.7.1"
                const val Foundation = "1.5.0-rc01"
                const val MaterialIconsExtended = "1.6.0-alpha02"
                const val Accompanist = "0.31.6-rc"
                const val HiltNavigation = "1.0.0"
            }

            const val Core = "1.10.0"
            const val LifecycleRuntime = "2.6.1"
            const val DataStore = "1.0.0"
            const val JUnit = "1.1.5"
            const val Espresso = "3.5.1"
            const val GmsLocation = "21.0.1"
            const val Hilt = "2.44"
        }

        object ThirdParty {
            const val ComposeDestinations = "1.8.36-beta"
            const val Jackson = "2.14.2"
            const val MapBox = "10.15.0"
            const val FirebaseBom = "31.3.0"
            const val JUnit = "4.13.2"
        }

        object Kotlin {
            const val CoroutinesPlayServices = "1.7.3"
        }
    }

    object Libs {
        object Android {
            object Compose {
                object Accompanist {
                    const val SystemUiController = "com.google.accompanist:accompanist-systemuicontroller:${Versions.Android.Compose.Accompanist}"
                    const val Permissions = "com.google.accompanist:accompanist-permissions:${Versions.Android.Compose.Accompanist}"
                }

                const val Bom = "androidx.compose:compose-bom:${Versions.Android.Compose.Bom}"
                const val Activity = "androidx.activity:activity-compose:${Versions.Android.Compose.Activity}"
                const val Foundation = "androidx.compose.foundation:foundation:${Versions.Android.Compose.Foundation}"
                const val MaterialIconsExtended = "androidx.compose.material:material-icons-extended:${Versions.Android.Compose.MaterialIconsExtended}"
                const val Material3 = "androidx.compose.material3:material3"
                const val Ui = "androidx.compose.ui:ui"
                const val UiGraphics = "androidx.compose.ui:ui-graphics"
                const val UiToolingPreview = "androidx.compose.ui:ui-tooling-preview"
                const val UiTooling = "androidx.compose.ui:ui-tooling"
                const val UiTestManifest = "androidx.compose.ui:ui-test-manifest"
                const val UiTestJUnit4 = "androidx.compose.ui:ui-test-junit4"
                const val HiltNavigation = "androidx.hilt:hilt-navigation-compose:${Versions.Android.Compose.HiltNavigation}"
            }

            const val Core = "androidx.core:core-ktx:${Versions.Android.Core}"
            const val LifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.Android.LifecycleRuntime}"
            const val GmsLocation = "com.google.android.gms:play-services-location:${Versions.Android.GmsLocation}"
            const val DataStore = "androidx.datastore:datastore:${Versions.Android.DataStore}"
            const val Hilt = "com.google.dagger:hilt-android:${Versions.Android.Hilt}"
            const val HiltKapt = "com.google.dagger:hilt-compiler:${Versions.Android.Hilt}"
            const val JUnit = "androidx.test.ext:junit:${Versions.Android.JUnit}"
            const val Espresso = "androidx.test.espresso:espresso-core:${Versions.Android.Espresso}"
        }

        object ThirdParty {
            object ComposeDestinations {
                const val Core = "io.github.raamcosta.compose-destinations:core:${Versions.ThirdParty.ComposeDestinations}"
                const val Ksp = "io.github.raamcosta.compose-destinations:ksp:${Versions.ThirdParty.ComposeDestinations}"
            }

            object Firebase {
                const val Bom = "com.google.firebase:firebase-bom:${Versions.ThirdParty.FirebaseBom}"
                const val Analytics = "com.google.firebase:firebase-analytics-ktx"
                const val Auth = "com.google.firebase:firebase-auth-ktx"
            }

            const val Jackson = "com.fasterxml.jackson.core:jackson-databind:${Versions.ThirdParty.Jackson}"
            const val MapBox = "com.mapbox.maps:android:${Versions.ThirdParty.MapBox}"
            const val JUnit = "junit:junit:${Versions.ThirdParty.JUnit}"
        }

        object Kotlin {
            const val CoroutinesPlayServices = "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:${Versions.Kotlin.CoroutinesPlayServices}"
        }
    }
}