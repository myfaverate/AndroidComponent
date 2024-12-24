import edu.tyut.config.ModuleType

plugins {
    alias(libs.plugins.component.plugin)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "edu.tyut.feature_shop"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
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
    buildFeatures{
        viewBinding = true
    }
}

moduleConfig{
    moduleType = ModuleType.ANDROID_DYNAMIC
}

dependencies {
    implementation(project(":app"))
    implementation(libs.material)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}