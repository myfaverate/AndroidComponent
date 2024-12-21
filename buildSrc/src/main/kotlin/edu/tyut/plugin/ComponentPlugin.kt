package edu.tyut.plugin

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.ApplicationVariant
import com.android.build.api.variant.LibraryVariant
import com.android.build.api.variant.ManifestFiles
import com.android.build.api.variant.SourceDirectories
import com.android.build.gradle.AppPlugin
import edu.tyut.task.PublishTask
import org.apache.tools.ant.types.resources.MultiRootFileSet.SetType.file
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.PluginManager
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.internal.cc.base.logger
import org.gradle.internal.declarativedsl.schemaBuilder.isPublic
import org.yaml.snakeyaml.Yaml
import java.io.File
import kotlin.jvm.optionals.getOrNull
import kotlin.math.log

private const val isModuleKey: String = "isModule"
private const val TAG: String = "ComponentPlugin"
private const val ANDROID_APPLICATION_ALIAS: String = "android.application"
private const val ANDROID_LIBRARY_ALIAS: String = "android.library"

/**
 * logger.error("Hello")
 * logger.info("Hello")
 * error("Error") // 直接结束插件
 */
class ComponentPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        // 依赖以下插件、 应用我们的自定义插件相当于已经依赖了maven-publish
        project.pluginManager.apply("maven-publish") // 应用我们的自定义插件相当于已经依赖了maven-publish

        val isModule: Boolean = if (project.hasProperty(isModuleKey)) project.property(isModuleKey).toString().toBoolean() else false
        println("$TAG -> isModule: $isModule")
        val pluginManager: PluginManager = project.pluginManager
        val versionCatalog: VersionCatalog = project.rootProject.extensions.getByType(VersionCatalogsExtension::class.java).named("libs")
        if (isModule){
            val applicationId: String? = versionCatalog.findPlugin(ANDROID_APPLICATION_ALIAS).getOrNull()?.orNull?.pluginId
            println("ComponentPlugin -> applicationId: $applicationId")
            if (!applicationId.isNullOrEmpty()){
                pluginManager.apply(applicationId)
            }
        }else{
            val libraryId: String? = versionCatalog.findPlugin(ANDROID_LIBRARY_ALIAS).getOrNull()?.orNull?.pluginId
            println("ComponentPlugin -> libraryIdId: $libraryId")
            if (!libraryId.isNullOrEmpty()){
                pluginManager.apply(libraryId)
            }
        }
        manifestSet(project = project, isModule = isModule)
    }
    private fun manifestSet(project: Project, isModule: Boolean){

        // 获取 sourceSets
        // val sourceSets: SourceSetContainer = project.extensions.getByType(org.gradle.api.tasks.SourceSetContainer::class.java)
        // val mainSourceSet: SourceSet = sourceSets.getByName("main")
        // // 设置 manifest 文件路径
        // if (isModule) {
        //     mainSourceSet.java.srcDir("src/main/java")
        // } else {
        //     mainSourceSet.java.srcDir("src/main/kotlin")
        // }

        // === 可以使用第三方库 ===
        val yamlContent = """
            name: Project
            age: 18
        """.trimIndent()
        val yaml = Yaml()
        val data: Map<String, Any> = yaml.load(yamlContent)
        println("$TAG -> data: $data")
        // === 可以使用第三方库 ===

        /**
         * https://williamkingsley.medium.com/custom-gradle-plugins-in-android-23342b98e721
         * https://github.com/myfaverate/AndroidComponent
         * https://docs.gradle.org/current/userguide/publishing_gradle_plugins.html
         */
        project.plugins.withType(AppPlugin::class.java) {
            println("$TAG -> AppPlugin...")
            val androidComponents: ApplicationAndroidComponentsExtension = project.extensions.getByType(ApplicationAndroidComponentsExtension::class.java)
            androidComponents.onVariants{ variants:  ApplicationVariant ->
                val manifestFiles: ManifestFiles = variants.sources.manifests
                val main: SourceDirectories.Flat = variants.sources.getByName("main")
                println("$TAG -> manifestFiles: $manifestFiles")
                println("$TAG -> main: $main")
            }
        }
        val androidExtension: CommonExtension<*, *, *, *, *, *>? = project.extensions.findByType(CommonExtension::class.java) ?: run{
            logger.error("$TAG -> This plugin only use in Android Project...")
            error("$TAG -> This plugin only use in Android Project...")
        }

        println("$TAG -> androidExtension: ${androidExtension?.javaClass}")
        androidExtension?.apply {
            if (this is LibraryExtension){
                publishTask(project = project) // only in LibraryExtension
                defaultConfig{
                    println("$TAG -> consumer-rules.pro...")
                    consumerProguardFiles("consumer-rules.pro") // 1
                }
            }
            sourceSets.getByName("main"){
                if (isModule){
                    manifest.srcFile(srcPath = "src/main/manifest/AndroidManifest.xml")
                }else{
                    manifest.srcFile(srcPath = "src/main/AndroidManifest.xml")
                }
            }
        }
    }

    /**
     * 必须确保有这个
     * buildTypes {
     *         release {
     *             isMinifyEnabled = false
     *             proguardFiles(
     *                 getDefaultProguardFile("proguard-android-optimize.txt"),
     *                 "proguard-rules.pro"
     *             )
     *         }
     *     }
     */
    private fun publishTask(project: Project){
        logger.warn("$TAG -> logger publishTask...")
        project.tasks.register("publishTask", PublishTask::class.java){
            dependsOn("assembleRelease")
            inputDirectory.set(project.layout.buildDirectory.dir("input").apply {
                this.orNull?.apply {
                    val inputFile = File(this.toString())
                    if (!inputFile.exists()){
                        val isSuccess: Boolean = inputFile.mkdirs()
                        logger.warn("$TAG -> input path: ${inputFile.absoluteFile}, isSuccess: $isSuccess")
                    }
                }
            })
            outputDirectory.set(project.layout.buildDirectory.dir("output").apply {
                this.orNull?.apply {
                    val outputFile = File(this.toString())
                    if (!outputFile.exists()){
                        val isSuccess: Boolean = outputFile.mkdirs()
                        logger.warn("$TAG -> input path: ${outputFile.absoluteFile}, isSuccess: $isSuccess")
                    }
                }
            })
            pluginArgs.set(listOf("Tom", "Jack", "Bom"))
        }
    }
}
/*
// 2
afterEvaluate {
    publishing{
        publications{
            create<MavenPublication>("release"){
                from(components["release"])
                groupId = project.group.toString().lowercase()
                artifactId = project.name
                version =  "0.0.1"
            }
        }
        repositories{
            maven {
                url = uri(path = "file://${rootDir}/repository")
            }
        }
    }
}
 */