package edu.tyut.config

enum class ModuleType(
    private val moduleTypeName: String,
){
    ANDROID_LIBRARY("library"),
    ANDROID_APPLICATION("application"),
    ANDROID_DYNAMIC("dynamic")
}