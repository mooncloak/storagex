import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("plugin.serialization")
    id("com.android.library")
    id("org.jetbrains.dokka")
    id("storagex.compose")
    id("storagex.publish")
}

kotlin {
    sourceSets {
        all {
            // Disable warnings and errors related to these expected @OptIn annotations.
            // See: https://kotlinlang.org/docs/opt-in-requirements.html#module-wide-opt-in
            languageSettings.optIn("kotlin.RequiresOptIn")
            languageSettings.optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
            languageSettings.optIn("kotlinx.coroutines.FlowPreview")
            languageSettings.optIn("kotlin.time.ExperimentalTime")
            languageSettings.optIn("-Xexpect-actual-classes")
        }

        val commonMain by getting {
            dependencies {
                api(project(":storagex-keyvalue"))

                // Coroutines
                // https://github.com/Kotlin/kotlinx.coroutines
                implementation(KotlinX.coroutines.core)

                // Serialization
                // https://github.com/Kotlin/kotlinx.serialization
                implementation(KotlinX.serialization.core)

                // Time
                // https://github.com/Kotlin/kotlinx-datetime
                implementation(KotlinX.datetime)

                // Multiplatform Key/Value Storage
                // https://github.com/russhwolf/multiplatform-settings
                // Apache 2.0: https://github.com/russhwolf/multiplatform-settings/blob/main/LICENSE.txt
                api(RussHWolf.multiplatformSettings.settings)
                api(RussHWolf.multiplatformSettings.noArg)
                implementation(RussHWolf.multiplatformSettings.coroutines)
                implementation(RussHWolf.multiplatformSettings.serialization)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

android {
    compileSdk = LibraryConstants.Android.compileSdkVersion
    namespace = "com.mooncloak.kodetools.storagex.keyvalue.settings"

    defaultConfig {
        minSdk = LibraryConstants.Android.minSdkVersion
        targetSdk = LibraryConstants.Android.targetSdkVersion
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            // Opt-in to experimental compose APIs
            freeCompilerArgs = listOf(
                "-Xopt-in=kotlin.RequiresOptIn"
            )
        }
    }
}
