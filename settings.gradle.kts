import org.jetbrains.intellij.platform.gradle.extensions.intellijPlatform

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
    id("org.jetbrains.intellij.platform.settings") version "2.1.0"
}
rootProject.name = "lsp-exp"

dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS

    repositories {
        mavenCentral()

        intellijPlatform {
            defaultRepositories()
            marketplace()
        }
    }
}