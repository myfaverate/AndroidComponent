dependencyResolutionManagement {
    versionCatalogs {
        create("libs"){
            from(files("../gradle/libs.versions.toml"))
        }
    }
}
println("buildSrc settings.gradle.src...")