import com.vallem.sylph.build_configuration.SylphDependencies

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp") version "1.8.10-1.0.9"
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.vallem.sylph"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.vallem.sylph"
        minSdk = 28
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
        kotlinCompilerExtensionVersion = SylphDependencies.Versions.Android.Compose.Compiler
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(platform(SylphDependencies.Libs.ThirdParty.Firebase.Bom))
    implementation(SylphDependencies.Libs.ThirdParty.Firebase.Analytics)
    implementation(SylphDependencies.Libs.ThirdParty.Firebase.Auth)
    implementation(SylphDependencies.Libs.ThirdParty.AWS.DynamoDb)

    implementation(SylphDependencies.Libs.Android.Hilt)
    implementation(project(mapOf("path" to ":app:events")))
    implementation(project(mapOf("path" to ":app:home")))
    kapt(SylphDependencies.Libs.Android.HiltKapt)

    implementation(SylphDependencies.Libs.Android.DataStore)
    implementation(SylphDependencies.Libs.ThirdParty.ComposeDestinations.Core)
    ksp(SylphDependencies.Libs.ThirdParty.ComposeDestinations.Ksp)

    implementation(SylphDependencies.Libs.Android.Core)
    implementation(SylphDependencies.Libs.Android.LifecycleRuntime)
    implementation(SylphDependencies.Libs.Android.Compose.Activity)

    testImplementation(SylphDependencies.Libs.ThirdParty.JUnit)
    androidTestImplementation(SylphDependencies.Libs.Android.JUnit)
    androidTestImplementation(SylphDependencies.Libs.Android.Espresso)
    androidTestImplementation(SylphDependencies.Libs.Android.Compose.UiTestJUnit4)

    implementation(project(":componentlibrary"))
    implementation(project(":app:shared"))
    implementation(project(":app:init"))
    implementation(project(":app:navigation"))
}
