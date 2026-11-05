
plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "af.alireza.appsaz"

    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "af.alireza.appsaz"
        minSdk = 28
        targetSdk = 37

        versionCode = 351
        versionName = "3.5.1"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("release") {
            storeFile = file(
                project.findProperty("KEYSTORE_FILE")
                    ?: "default/keystore/path"
            )

            storePassword =
                project.findProperty("KEYSTORE_PASSWORD")
                    ?.toString() ?: ""

            keyAlias =
                project.findProperty("KEY_ALIAS")
                    ?.toString() ?: ""

            keyPassword =
                project.findProperty("KEY_PASSWORD")
                    ?.toString() ?: ""
        }
    }

    buildTypes {
        getByName("release") {
            signingConfig =
                signingConfigs.getByName("release")

            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
                "proguard-rules.pro"
            )

            optimization {
                enable = true
                packageScope = setOf(
                    "androidx.**",
                    "kotlin.**",
                    "kotlinx.**"
                )
            }
        }

        getByName("debug") {
            // Optional release signing
            // signingConfig =
            //     signingConfigs.getByName("release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        viewBinding = false
    }

    androidResources {
        noCompress += listOf("jar", "apk", "jks")
    }
}

dependencies {

    // Local JAR files
    implementation(
        fileTree(
            mapOf(
                "dir" to "libs",
                "include" to listOf("*.jar")
            )
        )
    )

    // AndroidX
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.activity.ktx)

    // Material Design
    implementation(libs.material)

    // Preferences
    implementation(libs.preference)

    // Glide
    implementation(libs.glide)

    // Color Picker
    implementation(libs.hsv.color.picker)

    // Roundable Layout
    implementation(libs.roundable.layout)

    // Play / Pause Button
    implementation(libs.play.pause.button)

    // Glide Transformations
    implementation(libs.glide.transformations)

    // ZIP4J
    implementation(libs.zip4j)

    // Optional dependencies
    // implementation("com.github.bumptech.glide:glide:5.0.0-rc01")
    // implementation("com.github.apk-editor:aXML:0381822ba2")

    // Unit tests
    testImplementation(libs.junit)

    // Android tests
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
}
