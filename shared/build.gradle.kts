plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.sqldelight)
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
            // This `shared` framework is exported for app-ios-swift
            it.binaries.framework {
                baseName = "shared" // Used in app-ios-swift

                export(libs.decompose.decompose)
                export(libs.essenty.lifecycle)
            }
        }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.decompose.decompose)
                api(libs.essenty.lifecycle)
                implementation(libs.kotlinx.coroutines.core)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.kotest.framework)
                implementation(libs.kotest.assertion)
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.sqldelight.coroutines)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(libs.kotest.junit)
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(libs.sqldelight.sqlite)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.sqldelight.android)
            }
        }
        val nativeMain by getting {
            dependencies {
                implementation(libs.sqldelight.native)
            }
        }
    }
}

android {
    namespace = "com.example.myapplication.shared"
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

sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("io.amirhparhizgar")
        }
    }
}
