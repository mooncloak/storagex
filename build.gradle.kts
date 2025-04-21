plugins {
    kotlin("jvm") version "2.1.20" apply false
    kotlin("multiplatform") version "2.1.20" apply false
    kotlin("plugin.serialization") version "2.1.20" apply false
    id("com.android.library") version "8.7.3" apply false
    id("com.android.application") version "8.7.3" apply false
    id("org.jetbrains.dokka")
    id("org.jetbrains.kotlinx.binary-compatibility-validator")
    id("storagex.variables")
}

group = buildVariables.group
version = buildVariables.version

tasks.named<org.jetbrains.dokka.gradle.DokkaMultiModuleTask>("dokkaGfmMultiModule").configure {
    outputDirectory.set(file("${projectDir.path}/docs"))
}
