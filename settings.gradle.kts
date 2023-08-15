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
        maven(url = "https://api.mapbox.com/downloads/v2/releases/maven") {
            authentication {
                create<BasicAuthentication>("basic")
            }
            credentials {
                val mapBoxMavenPass: String by settings

                // Do not change the username below.
                // This should always be `mapbox` (not your username).
                username = "mapbox"
                password = mapBoxMavenPass
            }
        }
    }
}

rootProject.name = "Sylph"
include(":app")
include(":componentlibrary")
include(":app:home")
include(":app:shared")
include(":app:events")
include(":app:init")
include(":app:navigation")
