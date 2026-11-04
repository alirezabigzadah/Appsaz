
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

        versionCode = 350
        versionName = "3.5.0-trial"

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
    implementation("androidx.preference:preference:1.2.1")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.15.1")

    // Color Picker
    implementation(
        "com.github.martin-stone:hsv-alpha-color-picker-android:3.0.1"
    )

    // Roundable Layout
    implementation("com.github.zladnrms:RoundableLayout:1.1.4")

    // Play / Pause Button
    implementation(
        "com.github.Romancha:android-material-play-pause-view-button:2.3"
    )

    // Glide Transformations
    implementation("com.github.wasabeef:glide-transformations:4.3.0")

    // ZIP4J
    implementation("net.lingala.zip4j:zip4j:2.11.5")

    // Optional dependencies
    // implementation("com.github.bumptech.glide:glide:5.0.0-rc01")
    // implementation("com.github.apk-editor:aXML:0381822ba2")

    // Unit tests
    testImplementation(libs.junit)

    // Android tests
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
}
