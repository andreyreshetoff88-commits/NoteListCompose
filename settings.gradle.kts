pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "NoteListCompose"
include(":app")
include(":app:app-data")
include(":app:app-domain")
include(":core:database")
include(":core:network")
include(":core:common")
include(":feature:register:register-data")
include(":feature:register:register-domain")
include(":feature:register:register-presentation")
include(":feature:login:login-data")
include(":feature:login:login-domain")
include(":feature:login:login-presentation")
include(":feature:verification:verification-data")
include(":feature:verification:verification-presentation")
include(":feature:verification:verification-domain")
include(":feature:groups:groups-data")
include(":feature:groups:groups-domain")
include(":feature:groups:groups-presentation")
include(":feature:profile:profile-data")
include(":feature:profile:profile-domain")
include(":feature:profile:profile-presentation")
