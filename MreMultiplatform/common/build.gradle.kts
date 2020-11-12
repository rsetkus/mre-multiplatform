plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "1.4.10"
    id("com.android.library")
    id("com.squareup.sqldelight")
}

group = "co.uk.mre-multiplatform-library"
version = "0.0.1"

apply(plugin = "maven-publish")

android {
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    compileSdkVersion(29)
    defaultConfig {
        minSdkVersion(19)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
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
    android()

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
                implementation("com.squareup.sqldelight:runtime:1.4.3")
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
                implementation("io.ktor:ktor-client-mock-jvm:1.4.1")
                implementation("io.ktor:ktor-client-tests-jvm:1.4.1")
            }
        }

        val iosMain by getting {
            dependencies {
            }
        }
        val iosTest by getting {
            dependencies {
            }
        }

        val iosX64Main by getting {
            dependencies {
                dependsOn(iosMain)
                implementation("io.ktor:ktor-client-mock-iosx64:1.4.1")
                implementation("io.ktor:ktor-client-tests-iosx64:1.4.1")
            }
        }

        val iosX64Test by getting {
            dependencies {
                dependsOn(iosTest)
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
                implementation("io.ktor:ktor-client-mock-iosarm64:1.4.1")
                implementation("io.ktor:ktor-client-tests-iosarm64:1.4.1")
            }
        }
    }
}