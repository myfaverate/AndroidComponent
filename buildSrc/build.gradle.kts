import org.gradle.kotlin.dsl.gradlePlugin

plugins {
    `kotlin-dsl`
    `maven-publish`
}

group = "edu.tyut"
version = "1.0.0"

repositories {
    google() // 千万不能很少这一行，否则
    mavenCentral()
    gradlePluginPortal()
}

gradlePlugin{
    plugins{
        create("ComponentPlugin"){
            id = "edu.tyut.component-plugin"
            implementationClass = "edu.tyut.plugin.ComponentPlugin"
            version = "0.0.1"
        }
    }
}

publishing{
    publications {
        create<MavenPublication>("plugin") {
            from(components["kotlin"])
            groupId = "edu.tyut"
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