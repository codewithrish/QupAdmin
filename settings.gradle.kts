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
    }
}

rootProject.name = "Qup Admin"
include(":app")
include(":authentication")
include(":core:network")
include(":core:util")
include(":core:ui")
include(":home")
include(":feature:doctor_management")
include(":feature:reception_management")
include(":feature:srk_management")
include(":feature:entity_management")
include(":feature:summary")
include(":feature:commcredits")
include(":feature:indiapps")
