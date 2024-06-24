plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
    maven("https://repo.repsy.io/mvn/mooncloak/public")
}

dependencies {
    implementation(Kotlin.stdlib)
    implementation(Kotlin.gradlePlugin)

    implementation("com.mooncloak.kodetools.kenv:kenv-core:_")
}

gradlePlugin {
    plugins.register("storagex.variables") {
        id = "storagex.variables"
        implementationClass = "BuildVariablesPlugin"
    }
}
