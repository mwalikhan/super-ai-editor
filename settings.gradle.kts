pluginManagement {
repositories {
google()
mavenCentral()
gradlePluginPortal()
}
plugins {
id("com.android.application") version "8.5.2"
id("org.jetbrains.kotlin.android") version "1.9.24"
}
}

dependencyResolutionManagement {
repositories {
google()
mavenCentral()
}
}

rootProject.name = "super-ai-editor"
include(":app")
