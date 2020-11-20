plugins {
    id("com.android.library")
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "1.4.10"
}

apply(plugin = "maven-publish")

group = "co.uk.mre-multiplatform-library"
version = "0.0.1"

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
    android {
        publishLibraryVariants("release", "debug")
        publishLibraryVariantsGroupedByFlavor = true
    }
    ios {
        binaries {
            framework {
                baseName = "MRELIB"
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val iosMain by getting {
            dependencies {

            }
        }

        val iosX64Main by getting {
            dependencies {

            }
        }

        val iosX64Test by getting {
            dependencies {

            }
        }

        val iosArm64Main by getting {
            dependencies {
                dependsOn(iosMain)
            }
        }

        val iosArm64Test by getting {
            dependencies {

            }
        }
    }
}