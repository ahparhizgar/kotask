import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree
import org.jetbrains.kotlin.incremental.deleteDirectoryContents

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.roborazzi)
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
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        instrumentedTestVariant.sourceSetTree.set(KotlinSourceSetTree.test)
    }

    @OptIn(ExperimentalComposeLibrary::class)
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":shared"))

                implementation(compose.ui)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.uiTest)
                api(libs.decompose.decompose)
                implementation(libs.decompose.extensionsComposeJetbrains)
                implementation(libs.decompose.extensionsComposeJetbrains.experimental)
                implementation(libs.koin.core)
                implementation(libs.kotlinx.datetime)
                implementation(compose.components.uiToolingPreview)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-annotations-common"))
                implementation(libs.kotest.framework)
                implementation(libs.kotest.assertion)
                implementation(libs.roborazzi)
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
                implementation(libs.previewScanner)
                implementation(libs.roborazzi.compose.desktop)
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
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    packagingOptions {
        resources {
            // Merge the contents of the files (if possible)
            merges.add("META-INF/AL2.0")
            merges.add("META-INF/LGPL2.1")
        }
    }
}

dependencies {
    androidTestImplementation("androidx.compose.ui:ui-test-junit4-android:1.7.8")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.7.8")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

afterEvaluate {
    tasks.named("testDebugUnitTest") {
        enabled = false
    }
    tasks.named("testReleaseUnitTest") {
        enabled = false
    }
    tasks.named("compileReleaseUnitTestKotlinAndroid") {
        enabled = false
    }
}

tasks.register("cleanScreenshots") {
    doFirst {
        val isRecord = providers
            .gradleProperty("roborazzi.test.record")
            .orElse("false")
            .get() == "true"
        if (isRecord) {
            for (file in listOf("outputs/roborazzi", "intermediates/roborazzi")) {
                fileTree(layout.buildDirectory.dir(file)).dir.run {
                    if (exists()) {
                        deleteDirectoryContents()
                    }
                }
            }
        } else {
            println("Skipping deletion as roborazzi.test.record is not true")
        }
    }
}

tasks.named("jvmTest") {
    dependsOn(tasks.named("cleanScreenshots"))
}
