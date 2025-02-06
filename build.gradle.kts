import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.0.21"
    id("org.jetbrains.intellij.platform")
}

group = "org.example"
version = "1.0-SNAPSHOT"

dependencies {
    testImplementation(kotlin("test"))
    // need some investigation into classpath issue if bundled with extension
    compileOnly("org.eclipse.lsp4j:org.eclipse.lsp4j:0.24.0")

    intellijPlatform {
        intellijIdeaUltimate("2024.3")
        plugin("com.redhat.devtools.lsp4ij", "0.8.1")

        instrumentationTools()
    }
}

tasks.test {
    useJUnitPlatform()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    jvmToolchain(21)
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}
