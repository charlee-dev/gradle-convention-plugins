@file:Suppress("UNUSED_VARIABLE")

import com.copperleaf.gradle.ConventionConfig
import com.copperleaf.gradle.nativeTargetGroup
import org.gradle.api.tasks.compile.JavaCompile

plugins {
    kotlin("multiplatform")
}

val subprojectInfo = ConventionConfig.subprojectInfo(project)

kotlin {

    if (subprojectInfo.explicitApi) {
        explicitApi()
    }

    if (subprojectInfo.kotlinJvm) {
        jvm {
            if (subprojectInfo.kotlinJvmWithJava) {
                withJava()
            }
        }
    }
    if (subprojectInfo.kotlinAndroid) {
        androidTarget {
            publishAllLibraryVariants()
        }
    }
    if (subprojectInfo.kotlinJs) {
        js(IR) {
            browser {
                commonWebpackConfig {
                    devServer?.`open` = false
                }
            }
            if (subprojectInfo.kotlinJsExecutable) {
                binaries.executable()
            }
        }
    }
    if (subprojectInfo.kotlinIos) {
        nativeTargetGroup(
            "ios",
            iosArm64(),
            iosX64(),
            iosSimulatorArm64(),
        )
    }

    sourceSets {
        all {
            languageSettings.apply {
            }
        }

        // Common Sourcesets
        val commonMain by getting {
            dependencies { }
        }
        val commonTest by getting {
            dependencies { }
        }

        if (subprojectInfo.kotlinJvm) {
            val jvmMain by getting {
                dependencies { }
            }
            val jvmTest by getting {
                dependencies { }
            }
        }

        if (subprojectInfo.kotlinAndroid) {
            val androidMain by getting {
                dependencies { }
            }
            val androidUnitTest by getting {
                dependencies { }
            }
        }

        if (subprojectInfo.kotlinJs) {
            val jsMain by getting {
                dependencies { }
            }
            val jsTest by getting {
                dependencies { }
            }
        }

        if (subprojectInfo.kotlinIos) {
            val iosMain by getting {
                dependencies { }
            }
            val iosTest by getting {
                dependencies { }
            }
        }
    }
}

tasks.withType<JavaCompile> {
    sourceCompatibility = ConventionConfig.repoInfo(project).javaVersion
    targetCompatibility = ConventionConfig.repoInfo(project).javaVersion
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = ConventionConfig.repoInfo(project).javaVersion
        if (subprojectInfo.contextReceivers) {
            freeCompilerArgs = freeCompilerArgs + "-Xcontext-receivers"
        }
    }
}

