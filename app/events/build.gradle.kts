import com.vallem.sylph.build_configuration.SylphDependencies

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.devtools.ksp") version "1.8.10-1.0.9"
    id("kotlin-parcelize")
}

ksp {
    arg("compose-destinations.mode", "destinations")
    arg("compose-destinations.moduleName", "events")
}

android {
    namespace = "com.vallem.sylph.events"
    compileSdk = 34

    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = SylphDependencies.Versions.Android.Compose.Compiler
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
}

dependencies {
    implementation(project(":componentlibrary"))
    implementation(project(":app:shared"))

    implementation(SylphDependencies.Libs.Android.LifecycleRuntime)
    implementation(platform(SylphDependencies.Libs.Android.Compose.Bom))
    implementation(SylphDependencies.Libs.Android.Compose.Material3)
    implementation(SylphDependencies.Libs.Android.Compose.MaterialIconsExtended)
    implementation(SylphDependencies.Libs.Android.Compose.UiToolingPreview)

    implementation(SylphDependencies.Libs.Android.Hilt)
    implementation(SylphDependencies.Libs.Android.Compose.HiltNavigation)
    kapt(SylphDependencies.Libs.Android.HiltKapt)

    implementation(SylphDependencies.Libs.ThirdParty.MapBox)
    implementation(platform(SylphDependencies.Libs.ThirdParty.Firebase.Bom))
    implementation(SylphDependencies.Libs.ThirdParty.Firebase.Auth)
    implementation(SylphDependencies.Libs.ThirdParty.Jackson)

    implementation(SylphDependencies.Libs.ThirdParty.ComposeDestinations.Core)
    implementation(SylphDependencies.Libs.ThirdParty.ComposeDestinations.Animations)
    ksp(SylphDependencies.Libs.ThirdParty.ComposeDestinations.Ksp)

    androidTestImplementation(platform(SylphDependencies.Libs.Android.Compose.Bom))
}