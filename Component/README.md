# Android Componentization

1.   **`Introduction`**

     The purpose of this plugin is to reduce the packaging time of Android projects during development and improve development efficiency.

     And the vision of this plug-in is to better manage Android projects and promote team collaboration.
     It can be summed up in two words: Save Time and Promote Collaboration.

2.   **Spend Time**

     **empty android project:**

     ```bash
     ./grawdle clean
     ./grawdle assembleRelease
     BUILD SUCCESSFUL in 1m 7s
     44 actionable tasks: 44 executed
     ```

     **In very large projects project:**

     The time spent will reach an hour to two hours when you need to use the android release apk package during development.

     **Therefore**

     So we had to split the project to save a lot of time during development.

3.   **How to Use?**

     (1) New empty Android Project

     (2) Add Android Library Module

     	* **`featureHome`**
     	* **`featureUser`**

     (3) New `manifest` directory, add `AndroidManifest.xml` and add `HomeApplication`, `HomeActivity` and `UserApplication`, `UserActivity` to `featureHome` and `featureUser` respectively.

     **`featureHome/src/main/manifest`**

     ```xml
     featureHome/src/main/manifest
     <?xml version="1.0" encoding="utf-8"?>
     <manifest xmlns:android="http://schemas.android.com/apk/res/android"
         xmlns:tools="http://schemas.android.com/tools">
     
         <!-- copy from app -->
         <application
             android:name=".HomeApplication"
             android:allowBackup="true"
             android:icon="@mipmap/ic_launcher"
             android:label="@string/app_name"
             android:roundIcon="@mipmap/ic_launcher_round"
             android:supportsRtl="true"
             android:theme="@style/Theme.ComponentPluginUse"
             tools:targetApi="31">
             <activity
                 android:name=".HomeActivity"
                 android:exported="true">
                 <intent-filter>
                     <action android:name="android.intent.action.MAIN" />
     
                     <category android:name="android.intent.category.LAUNCHER" />
                 </intent-filter>
             </activity>
         </application>
     
     </manifest>
     ```

     **`featureUser/src/main/manifest`**

     ```xml
     <?xml version="1.0" encoding="utf-8"?>
     <manifest xmlns:android="http://schemas.android.com/apk/res/android"
         xmlns:tools="http://schemas.android.com/tools">
     
         <!-- copy from app -->
         <application
             android:name=".UserApplication"
             android:allowBackup="true"
             android:icon="@mipmap/ic_launcher"
             android:label="@string/app_name"
             android:roundIcon="@mipmap/ic_launcher_round"
             android:supportsRtl="true"
             android:theme="@style/Theme.ComponentPluginUse"
             tools:targetApi="31">
             <activity
                 android:name=".UserActivity"
                 android:exported="true">
                 <intent-filter>
                     <action android:name="android.intent.action.MAIN" />
     
                     <category android:name="android.intent.category.LAUNCHER" />
                 </intent-filter>
             </activity>
         </application>
     
     </manifest>
     ```

     (4) **Apply Plugin**

     **libs.versions.toml**

     ```toml
     [versions]
     # component plugin version
     componentVersion = "0.0.1"
     
     [libraries]
     
     [plugins]
     # add component plugin
     component-plugin = { id = "io.github.itguoke.component-plugin", version.ref = "componentVersion" }
     ```

     **root `build.gradle.kts`**

     ```kotlin
     plugins {
         alias(libs.plugins.android.application) apply false
         alias(libs.plugins.kotlin.android) apply false
         alias(libs.plugins.android.library) apply false
         alias(libs.plugins.component.plugin) apply false // use 
         alias(libs.plugins.plugin.publish) apply false
     }
     ```

     **`featureHome` and `featureUser` `build.gradle.kts`**

     ```kotlin
     import org.jetbrains.kotlin.gradle.dsl.JvmTarget
     
     plugins {
         alias(libs.plugins.kotlin.android)
         alias(libs.plugins.component.plugin)
     }
     
     android {
         namespace = "edu.tyut.featureuser"
         compileSdk = 34
     
         defaultConfig {
             minSdk = 24
     
             testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
             consumerProguardFiles("consumer-rules.pro")
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
     ```

     (5) **use plugin**

     ```bash
     ./gradle :featureHome:publishReleasePublicationToMavenRepository
     ./gradle :featureUser:publishReleasePublicationToMavenRepository
     ```

     **app module** **`build.gradle.kts`**

     ```kotlin
     dependencies {
     
         implementation("componentpluginuse:featureHome:0.0.1")
         implementation("componentpluginuse:featureUser:0.0.1")
     
         implementation(libs.androidx.core.ktx)
         implementation(libs.androidx.appcompat)
         implementation(libs.material)
         implementation(libs.androidx.activity)
         implementation(libs.androidx.constraintlayout)
         testImplementation(libs.junit)
         androidTestImplementation(libs.androidx.junit)
         androidTestImplementation(libs.androidx.espresso.core)
     }
     ```

     `root` **settings.gradle.kts**

     ```kotlin
     pluginManagement {
         repositories {
             google {
                 content {
                     includeGroupByRegex("com\\.android.*")
                     includeGroupByRegex("com\\.google.*")
                     includeGroupByRegex("androidx.*")
                 }
             }
             mavenLocal() // current replace gradlePluginPortal
             mavenCentral()
             gradlePluginPortal()
         }
     }
     dependencyResolutionManagement {
         repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
         repositories {
             // note
             maven { url = uri(path = "file://${rootDir}/repository") }
             google()
             mavenCentral()
         }
     }
     
     rootProject.name = "ComponentPluginUse"
     include(":app")
     include(":featureHome")
     include(":featureUser")
     ```

     **app MainActivity**

     ```kotlin
     class MainActivity : AppCompatActivity() {
     
         private val binding: ActivityMainBinding by lazy {
             ActivityMainBinding.inflate(layoutInflater)
         }
     
         override fun onCreate(savedInstanceState: Bundle?) {
             super.onCreate(savedInstanceState)
             enableEdgeToEdge()
             setContentView(binding.root)
             ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                 val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                 v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                 insets
             }
             initView()
         }
     
         private fun initView() {
             binding.btnHome.setOnClickListener{
                 UserActivity.startActivity(this, "home")
             }
             binding.btnUser.setOnClickListener{
                 HomeActivity.startActivity(this, "user")
             }
         }
     }
     ```

     **app MainApplication**

     ```kotlin
     private const val TAG: String = "MainApplication"
     
     class MainApplication : Application() {
         override fun onCreate() {
             super.onCreate()
             Log.i(TAG, "onCreate...")
             HomeApplication().onCreate()
             UserApplication().onCreate()
         }
     }
     /*
     2024-12-23 22:49:26.880 26734-26734 MainApplication         edu.tyut.componentpluginuse          I  onCreate...
     2024-12-23 22:49:26.880 26734-26734 HomeApplication         edu.tyut.componentpluginuse          I  onCreate...
     2024-12-23 22:49:26.880 26734-26734 UserApplication         edu.tyut.componentpluginuse          I  onCreate...
     */
     ```

4.   **set `isModule` = true Separate development module**

     **`isModule`**

     `true`: use in development mode

     `false` use in integration

     `gradle.properties`

     ```properties
     isModule=true
     ```

5.   **`Directory Structure`**

```bash
├───app
│   ├───.gradle
│   └───src
│       ├───androidTest
│       │   └───java
│       │       └───edu
│       │           └───tyut
│       │               └───componentpluginuse
│       ├───main
│       │   ├───java
│       │   │   └───edu
│       │   │       └───tyut
│       │   │           └───componentpluginuse
│       │   └───res
│       │       ├───drawable
│       │       ├───layout
│       │       ├───mipmap-anydpi-v26
│       │       ├───mipmap-hdpi
│       │       ├───mipmap-mdpi
│       │       ├───mipmap-xhdpi
│       │       ├───mipmap-xxhdpi
│       │       ├───mipmap-xxxhdpi
│       │       ├───values
│       │       ├───values-night
│       │       └───xml
│       └───test
│           └───java
│               └───edu
│                   └───tyut
│                       └───componentpluginuse
├───featureHome
│   ├───.gradle
│   └───src
│       ├───androidTest
│       │   └───java
│       │       └───edu
│       │           └───tyut
│       │               └───featurehome
│       ├───main
│       │   ├───java
│       │   │   └───edu
│       │   │       └───tyut
│       │   │           └───featurehome
│       │   ├───manifest
│       │   └───res
│       │       ├───layout
│       │       ├───mipmap-xxxhdpi
│       │       └───values
│       └───test
│           └───java
│               └───edu
│                   └───tyut
│                       └───featurehome
├───featureUser
│   ├───.gradle
│   └───src
│       ├───androidTest
│       │   └───java
│       │       └───edu
│       │           └───tyut
│       │               └───featureuser
│       ├───main
│       │   ├───java
│       │   │   └───edu
│       │   │       └───tyut
│       │   │           └───featureuser
│       │   ├───manifest
│       │   └───res
│       │       ├───layout
│       │       ├───mipmap-xxxhdpi
│       │       └───values
│       └───test
│           └───java
│               └───edu
│                   └───tyut
│                       └───featureuser
├───gradle
│   └───wrapper
└───repository
    └───componentpluginuse
        ├───featureHome
        │   └───0.0.1
        └───featureUser
            └───0.0.1
```

6.   **`TODO`**
     The publishing `aar` functionality will be integrate into `edu.tyut.task.PublishTask`.
     replace

```kotlin
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
```

# Copyright

This plugin is for learning only and is prohibited for commercial use.
