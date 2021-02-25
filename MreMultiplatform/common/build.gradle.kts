import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("org.jetbrains.dokka") version "1.4.10.2"
    kotlin("plugin.serialization") version "1.4.10"
    id("com.squareup.sqldelight")
}

group = "co.uk.mre-multiplatform-library"
version = "0.0.1"

apply(plugin = "maven-publish")

sqldelight {
    database("MreDatabase") {
        packageName = "co.uk.ee.mre"
    }
}

android {
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    compileSdkVersion(29)
    defaultConfig {
        minSdkVersion(19)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    sourceSets {
        val main by getting {
            java.srcDirs("src/androidMain/kotlin")
            res.srcDirs("src/androidMain/res")
        }
        val androidTest by getting {
            java.srcDirs("src/androidTest/kotlin")
            res.srcDirs("src/androidTest/res")
        }
    }
}

kotlin {
    android {
        publishLibraryVariants("release", "debug")
        publishLibraryVariantsGroupedByFlavor = true
    }

    ios {
        binaries {
            framework {
                baseName = "MRE"
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("io.ktor:ktor-client-core:1.4.1")
                implementation("io.ktor:ktor-client-json:1.4.1")
                implementation("io.ktor:ktor-client-serialization:1.4.1")
                implementation("io.ktor:ktor-client-logging:1.4.1")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9-native-mt-2")
                implementation("com.benasher44:uuid:0.2.2")
                implementation("com.squareup.sqldelight:runtime:1.4.3")
                api("org.koin:koin-core:3.0.0-alpha-4")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation("io.ktor:ktor-client-mock:1.4.1")
                implementation("io.ktor:ktor-client-tests:1.3.1")
            }
        }

        val androidMain by getting {
            dependencies {
            }
        }

        val androidTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
            }
        }

        val iosMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-ios:1.4.1")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-native:1.3.8")
                implementation("com.squareup.sqldelight:native-driver:1.4.3")
            }
        }
        val iosTest by getting {
            dependencies {
            }
        }

        val iosX64Main by getting {
            dependencies {
                dependsOn(iosMain)
            }
        }

        val iosX64Test by getting {
            dependencies {
                dependsOn(iosTest)
                implementation("io.ktor:ktor-client-tests-iosx64:1.4.1")
            }
        }

        val iosArm64Main by getting {
            dependencies {
                dependsOn(iosMain)
            }
        }

        val iosArm64Test by getting {
            dependencies {
                dependsOn(iosTest)
                implementation("io.ktor:ktor-client-tests-iosarm64:1.4.1")
            }
        }
    }
}

// Ktlint configuration
val ktlint by configurations.creating

dependencies {
    ktlint("com.pinterest:ktlint:0.39.0")
}

val outputDir = "${project.buildDir}/reports/ktlint/"
val inputFiles = project.fileTree(mapOf("dir" to "src", "include" to "**/*.kt"))

val ktlintCheck by tasks.creating(JavaExec::class) {
    group = "verification"
    description = "Check Kotlin code style."

    inputs.files(inputFiles)
    outputs.dir(outputDir)

    description = "Check Kotlin code style."
    classpath = ktlint
    main = "com.pinterest.ktlint.Main"
    args = listOf("src/**/*.kt")
}

val ktlintFormat by tasks.creating(JavaExec::class) {
    group = "formatting"
    description = "Fix Kotlin code style deviations."
    classpath = ktlint
    main = "com.pinterest.ktlint.Main"
    args = listOf("-F", "src/**/*.kt")
}

tasks.named("check") {
    dependsOn("ktlintCheck")
}

val packForXcode by tasks.creating(Sync::class) {
    group = "publishing"
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val sdkName = System.getenv("SDK_NAME") ?: "iphonesimulator"
    val targetName = "ios" + if (sdkName.startsWith("iphoneos")) "Arm64" else "X64"
    val framework =
            kotlin.targets.getByName<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>(targetName).binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)
    val targetDir = File(buildDir, "xcode-frameworks")
    from({ framework.outputDirectory })
    into(targetDir)
}
tasks.getByName("build").dependsOn(packForXcode)

// Set jvm target version so that Kotlin could inline bytecode built for Android
tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "1.8"
}