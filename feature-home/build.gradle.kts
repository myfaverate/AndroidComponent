import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.component.plugin)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "edu.tyut.feature_home"
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = JvmTarget.JVM_21.target
    }
    buildFeatures {
        viewBinding = true
    }
}

afterEvaluate {
    publishing{
        repositories{
            maven {
                url = uri(path = "file://${rootDir}/repository")
            }
        }
        publications{
            val releaseComponent: SoftwareComponent? = components.findByName("release")
            logger.warn("user releaseComponent: $releaseComponent")
            if (releaseComponent == null){
                return@publications
            }
            create<MavenPublication>("release"){
                from(components["release"])
                groupId = project.group.toString().lowercase()
                artifactId = project.name
                version =  "0.0.1"
            }
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}