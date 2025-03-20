@file:Suppress("DSL_SCOPE_VIOLATION")

import org.jetbrains.compose.ExperimentalComposeLibrary


plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kotlin.compose)
}

kotlin {
    applyDefaultHierarchyTemplate()

    jvm()

    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = libs.versions.jvmTarget.get()
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).takeIf { "XCODE_VERSION_MAJOR" in System.getenv().keys } // Export the framework only for Xcode builds
        ?.forEach {
            // This `shared` framework is exported for app-ios-compose
            it.binaries.framework {
                baseName = "shared" // Used in app-ios-compose

                export(project(":shared"))
                export(libs.decompose.decompose)
                export(libs.essenty.lifecycle)
            }
        }

    @OptIn(ExperimentalComposeLibrary::class)
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":shared"))

                // Compose Libraries
                implementation(compose.ui)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.uiTest)

                // Decompose Libraries
                api(libs.decompose.decompose)
                implementation(libs.decompose.extensionsComposeJetbrains)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.kotest.framework)
                implementation(libs.kotest.assertion)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(libs.kotest.junit)
            }
        }
    }
}

android {
    namespace = "com.example.myapplication.compose"
    compileSdk =
        libs.versions.android.compileSdk
            .get()
            .toInt()

    defaultConfig {
        minSdk =
            libs.versions.android.minSdk
                .get()
                .toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
