import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("plugin.serialization")
    id("com.android.library")
    id("org.jetbrains.dokka")
    id("storagex.multiplatform")
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
            languageSettings.optIn("com.chrynan.navigation.ExperimentalNavigationApi")
            languageSettings.optIn("-Xexpect-actual-classes")
        }

        val commonMain by getting {
            dependencies {
                // Coroutines
                // https://github.com/Kotlin/kotlinx.coroutines
                implementation(KotlinX.coroutines.core)

                // Serialization
                // https://github.com/Kotlin/kotlinx.serialization
                implementation(KotlinX.serialization.json)

                // Time
                // https://github.com/Kotlin/kotlinx-datetime
                implementation(KotlinX.datetime)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val jvmMain by getting {
            dependencies {
                // Settings - Multi-platform settings
                // https://github.com/russhwolf/multiplatform-settings
                api(RussHWolf.multiplatformSettings.settings)
                api(RussHWolf.multiplatformSettings.noArg)
                api(RussHWolf.multiplatformSettings.serialization)
            }
        }

        val androidMain by getting {
            dependencies {
                // Settings - Multi-platform settings
                // https://github.com/russhwolf/multiplatform-settings
                api(RussHWolf.multiplatformSettings.settings)
                api(RussHWolf.multiplatformSettings.noArg)
                api(RussHWolf.multiplatformSettings.serialization)
            }
        }

        val iosMain by getting {
            dependencies {
                // Settings - Multi-platform settings
                // https://github.com/russhwolf/multiplatform-settings
                api(RussHWolf.multiplatformSettings.settings)
                api(RussHWolf.multiplatformSettings.noArg)
                api(RussHWolf.multiplatformSettings.serialization)
            }
        }

        val watchosMain by getting {
            dependencies {
                // Settings - Multi-platform settings
                // https://github.com/russhwolf/multiplatform-settings
                api(RussHWolf.multiplatformSettings.settings)
                api(RussHWolf.multiplatformSettings.noArg)
                api(RussHWolf.multiplatformSettings.serialization)
            }
        }

        val tvosMain by getting {
            dependencies {
                // Settings - Multi-platform settings
                // https://github.com/russhwolf/multiplatform-settings
                api(RussHWolf.multiplatformSettings.settings)
                api(RussHWolf.multiplatformSettings.noArg)
                api(RussHWolf.multiplatformSettings.serialization)
            }
        }

        val macosMain by getting {
            dependencies {
                // Settings - Multi-platform settings
                // https://github.com/russhwolf/multiplatform-settings
                api(RussHWolf.multiplatformSettings.settings)
                api(RussHWolf.multiplatformSettings.noArg)
                api(RussHWolf.multiplatformSettings.serialization)
            }
        }
    }
}

android {
    compileSdk = LibraryConstants.Android.compileSdkVersion
    namespace = "com.mooncloak.kodetools.storagex.keyvalue"

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

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].java.srcDirs("src/androidMain/kotlin")
    sourceSets["main"].res.srcDirs("src/androidMain/res")

    sourceSets["test"].java.srcDirs("src/androidTest/kotlin")
    sourceSets["test"].res.srcDirs("src/androidTest/res")
}
