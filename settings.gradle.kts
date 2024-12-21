pluginManagement {
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "edu.tyut.component-plugin") {
                println("root settings.gradle.kts requested.id.id: ${requested.id.id}, version: ${requested.version}")
            }
        }
    }
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven { url = uri(path = "file://${rootDir}/repository").apply {
            logger.lifecycle("root settings.gradle.kts this: $this")
        } } // 只存储aar包的本地仓库地址
        google()
        mavenCentral()
    }
}

rootProject.name = "Component"
include(":app")
include(":feature-user")
include(":feature-home")
