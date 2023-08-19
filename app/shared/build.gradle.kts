import com.vallem.sylph.build_configuration.SylphDependencies
import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp") version "1.8.10-1.0.9"
}

android {
    namespace = "com.vallem.sylph.shared"
    compileSdk = 33

    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        val properties = Properties().apply {
            load(FileInputStream(File(rootProject.rootDir, "local.properties")))
        }

        buildConfigField(
            "String",
            "MAP_BOX_API_TOKEN",
            "\"${properties.getProperty("MAP_BOX_API_TOKEN")}\""
        )
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

    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
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
    implementation(SylphDependencies.Libs.Android.Core)
    implementation(SylphDependencies.Libs.Android.LifecycleRuntime)
    implementation(platform(SylphDependencies.Libs.Android.Compose.Bom))
    implementation(SylphDependencies.Libs.Android.Compose.UiTestJUnit4)
    implementation(SylphDependencies.Libs.Android.Compose.Activity)
    implementation(SylphDependencies.Libs.Android.Compose.Material3)
    implementation(SylphDependencies.Libs.Android.Compose.MaterialIconsExtended)
    implementation(SylphDependencies.Libs.Android.Compose.UiToolingPreview)
    implementation(SylphDependencies.Libs.Android.GmsLocation)
    implementation(SylphDependencies.Libs.ThirdParty.MapBox)
    implementation(SylphDependencies.Libs.ThirdParty.ComposeDestinations.Core)
    implementation(platform(SylphDependencies.Libs.ThirdParty.Firebase.Bom))
    implementation(SylphDependencies.Libs.ThirdParty.Firebase.Auth)
    ksp(SylphDependencies.Libs.ThirdParty.ComposeDestinations.Ksp)

    implementation(SylphDependencies.Libs.Kotlin.CoroutinesPlayServices)

    implementation(SylphDependencies.Libs.Android.DataStore)
    implementation(SylphDependencies.Libs.ThirdParty.Jackson)

    implementation(project(":componentlibrary"))

    androidTestImplementation(platform(SylphDependencies.Libs.Android.Compose.Bom))
}