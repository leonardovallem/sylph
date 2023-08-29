import com.vallem.sylph.build_configuration.SylphDependencies

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp") version "1.8.10-1.0.9"
}

android {
    namespace = "com.vallem.componentlibrary"
    compileSdk = 34

    defaultConfig {
        minSdk = 28
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
    implementation(SylphDependencies.Libs.Android.Core)
    implementation(SylphDependencies.Libs.Android.LifecycleRuntime)
    implementation(SylphDependencies.Libs.Android.Compose.Activity)
    implementation(platform(SylphDependencies.Libs.Android.Compose.Bom))
    implementation(SylphDependencies.Libs.Android.Compose.Material3)
    implementation(SylphDependencies.Libs.Android.Compose.MaterialIconsExtended)
    implementation("androidx.compose.animation:animation:1.5.0-rc01")
    implementation(SylphDependencies.Libs.Android.Compose.Ui)
    implementation(SylphDependencies.Libs.Android.Compose.UiGraphics)
    implementation(SylphDependencies.Libs.Android.Compose.UiToolingPreview)

    implementation(SylphDependencies.Libs.Android.Compose.Accompanist.SystemUiController)

    implementation(SylphDependencies.Libs.ThirdParty.ComposeDestinations.Core)
    ksp(SylphDependencies.Libs.ThirdParty.ComposeDestinations.Ksp)

    testImplementation(SylphDependencies.Libs.ThirdParty.JUnit)
    androidTestImplementation(SylphDependencies.Libs.Android.JUnit)
    androidTestImplementation(SylphDependencies.Libs.Android.Espresso)
    androidTestImplementation(platform(SylphDependencies.Libs.Android.Compose.Bom))
    androidTestImplementation(SylphDependencies.Libs.Android.Compose.UiTestJUnit4)
    debugImplementation(SylphDependencies.Libs.Android.Compose.UiTooling)
    debugImplementation(SylphDependencies.Libs.Android.Compose.UiTestManifest)
}