plugins {
    kotlin("jvm") version "2.0.20" apply false
    kotlin("multiplatform") version "2.0.20" apply false
    kotlin("plugin.serialization") version "2.0.20" apply false
    id("com.android.library") version "8.2.2" apply false
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.dokka")
    id("org.jetbrains.kotlinx.binary-compatibility-validator")
    id("storagex.variables")
}

group = buildVariables.group
version = buildVariables.version

rootProject.plugins.withType<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin> {
    rootProject.the<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension>().nodeVersion = "16.0.0"
}

tasks.named<org.jetbrains.dokka.gradle.DokkaMultiModuleTask>("dokkaGfmMultiModule").configure {
    outputDirectory.set(file("${projectDir.path}/docs"))
}
