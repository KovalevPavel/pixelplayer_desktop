@file:Suppress("UnstableApiUsage")

rootProject.name = "pixelplayer_desktop"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

class DirScope(val dir: String)

fun withDirectory(dir: String, action: DirScope.() -> Unit) {
    DirScope(dir).action()
}

fun DirScope.addModule(path: String) {
    include(":$path")
    project(":$path").projectDir = file(rootProject.projectDir.path.plus("/${this.dir}/$path"))
}

include(":composeApp")
include(":core_ui")
include(":core_network")

withDirectory("storage") {
    addModule("api_storage")
    addModule("core_storage")
}

withDirectory("credentials") {
    addModule("api_credentials")
    addModule("core_credentials")
}

withDirectory("login") {
    addModule("api_login")
    addModule("domain_login")
    addModule("feature_login")
}

withDirectory("main_flow") {
    addModule("api_main_flow")
    addModule("domain_main_flow")
    addModule("feature_main_flow")
}

withDirectory("tags") {
    addModule("api_tags")
    addModule("core_tags")
}
