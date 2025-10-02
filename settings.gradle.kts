pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // Add JitPack for FFmpeg
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "super-ai-editor"
include(":app")
