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
        maven { url = uri(path = "file://${rootDir}/repository") }
        google()
        mavenCentral()
    }
}

rootProject.name = "ComponentPluginUse"
include(":app")
include(":featureHome")
include(":featureUser")
include(":featureShop")
include(":featureRead")
