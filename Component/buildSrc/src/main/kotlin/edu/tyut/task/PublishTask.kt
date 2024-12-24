package edu.tyut.task

import org.gradle.api.DefaultTask
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.net.URI

private const val TAG: String = "PublishTask"

abstract class PublishTask : DefaultTask() {

    init {
        group = "publishing"
        description = "publish aar package to MavenRepository(rootDir/repository)"
    }

    @get:InputDirectory
    abstract val inputDirectory: DirectoryProperty

    @get:Input
    abstract val pluginArgs: ListProperty<String>

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    @TaskAction
    fun taskAction() {

        logger.error("$TAG -> taskAction inputDirectory: ${inputDirectory.orNull}")
        logger.warn("$TAG -> taskAction pluginArgs: ${pluginArgs.orNull}")
        logger.warn("$TAG -> taskAction outputDirectory: ${outputDirectory.orNull}")
        logger.warn("$TAG -> project logger publishTask...")
        logger.lifecycle("$TAG -> Custom path URI: ${project.uri("file://${project.rootDir}/customDirectory")}")
        project.components.forEach {
            logger.lifecycle("$TAG -> Component: ${it.name}")
        }
        project.extensions.configure(PublishingExtension::class.java) {
            publications {
                create("release", MavenPublication::class.java) {
                    from(project.components.getByName("release"))
                    groupId = project.group.toString().lowercase()
                    artifactId = project.name.apply {
                        logger.warn("$TAG -> project name: ${project.name}")
                    }
                    version = "0.0.1"
                }
            }
            repositories {
                maven {
                    val myUrl = project.file("repository").toURI()
                    logger.lifecycle("$TAG -> Maven repository path: $myUrl")
                    url = project.uri("file://${project.rootDir}/repository").apply {
                        val repository = File(this.path.apply {
                            logger.lifecycle("$TAG -> repository path: $this")
                        })
                        if (!repository.exists()){
                            val isSuccess: Boolean = repository.mkdirs()
                            logger.lifecycle("$TAG -> repository isSuccess: $isSuccess")
                        }
                    }
                }
            }
        }
    }
}