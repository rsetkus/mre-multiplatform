plugins {
    id("com.android.library")
    kotlin("multiplatform")
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
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
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
            }
        }
    }
}