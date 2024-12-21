# Android Componentization
1. `Directory Structure`
```kotlin
├───app
│   ├───.gradle
│   └───src
│       ├───androidTest
│       │   └───java
│       │       └───edu
│       │           └───tyut
│       │               └───component
│       ├───main
│       │   ├───java
│       │   │   └───edu
│       │   │       └───tyut
│       │   │           └───component
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
│                       └───component
├───buildSrc
│   ├───.gradle
│   └───src
│       ├───main
│       │   ├───kotlin
│       │   │   └───edu
│       │   │       └───tyut
│       │   │           ├───plugin
│       │   │           └───task
│       │   └───resources
│       └───test
│           └───kotlin
├───feature-home
│   ├───.gradle
│   └───src
│       ├───androidTest
│       │   └───java
│       │       └───edu
│       │           └───tyut
│       │               └───feature_home
│       ├───main
│       │   ├───java
│       │   │   └───edu
│       │   │       └───tyut
│       │   │           └───feature_home
│       │   ├───manifest
│       │   └───res
│       │       ├───layout
│       │       ├───mipmap-xxxhdpi
│       │       └───values
│       └───test
│           └───java
│               └───edu
│                   └───tyut
│                       └───feature_home
├───feature-user
│   ├───.gradle
│   └───src
│       ├───androidTest
│       │   └───java
│       │       └───edu
│       │           └───tyut
│       │               └───feature_user
│       ├───main
│       │   ├───java
│       │   │   └───edu
│       │   │       └───tyut
│       │   │           └───feature_user
│       │   ├───manifest
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
│       │       └───values-night
│       └───test
│           └───java
│               └───edu
│                   └───tyut
│                       └───feature_user
├───gradle
│   └───wrapper
└───repository
    └───component
        ├───feature-home
        │   └───0.0.1
        └───feature-user
            └───0.0.1
```

2. `isModule`
in root `gradle.properties`

```properties
isModule=false
```

**isModule**

`true`: use in development mode

`false` use in integration

3. `TODO`
The publishing aar functionality will be integrate into `edu.tyut.task.PublishTask`.
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

## Note
This plugin is for learning only and is prohibited for commercial use.
