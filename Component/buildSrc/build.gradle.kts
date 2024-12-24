import org.gradle.kotlin.dsl.gradlePlugin

plugins {
    `kotlin-dsl`
    `maven-publish`
    alias(libs.plugins.plugin.publish)
}

group = "edu.tyut"
version = "1.0.0"

repositories {
    google() // 千万不能很少这一行，否则
    mavenCentral()
    gradlePluginPortal()
}

@Suppress("UnstableApiUsage")
gradlePlugin{
    website.set("https://github.com/myfaverate/AndroidComponent")
    vcsUrl.set("https://github.com/myfaverate/AndroidComponent.git")
    plugins{
        create("ComponentPlugin"){
            id = "io.github.itguoke.component-plugin"
            implementationClass = "edu.tyut.plugin.ComponentPlugin"
            version = "0.0.1"
            displayName = "Gradle Android Componentization plugin"
            description = "Gradle plugin to Android Componentization!"
            tags.set(listOf("Android", "Componentization", "Kotlin"))
        }
    }
}

publishing{
    publications {
        create<MavenPublication>("plugin") {
            from(components["kotlin"])
            groupId = "io.github.itguoke"
            artifactId = "component-plugin"
            version = "0.0.1"
        }
    }
    repositories{
        maven {
            url = uri(path = "file://${rootDir.parent}/repository")
        }
    }
}

dependencies{
    // yaml
    implementation(libs.snakeyaml)
    implementation(libs.gradle.api)
}

println("buildSrc build.gradle.src...")