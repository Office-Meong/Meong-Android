import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.aboutlibraries.android)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.devtools.ksp)
}

val properties = Properties().apply {
    load(project.rootProject.file("local.properties").inputStream())
}

android {
    namespace = "com.office.meong"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.office.meong"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "BASE_URL", properties["base.url"].toString())
        buildConfigField("String", "KAKAO_NATIVE_APP_KEY", "\"${properties["kakao.native.app.key"]}\"")
        manifestPlaceholders["kakaonativeappkey"] = properties["kakao.native.app.key"].toString()
    }
    signingConfigs {
        getByName("debug") {
            keyAlias = "androiddebugkey"
            keyPassword = "android"
            storeFile = File("${project.rootDir.absolutePath}/keystore/debug.keystore")
            storePassword = "android"
        }
        create("release") {
            // Credentials come from local.properties (git-ignored):
            //   release.store.password / release.key.alias / release.key.password
            storeFile = File("${project.rootDir.absolutePath}/keystore/office_meong_releaseKey.jks")
            storePassword = properties["release.store.password"].toString()
            keyAlias = properties["release.key.alias"].toString()
            keyPassword = properties["release.key.password"].toString()
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            // Android platform default rules. Project/library rules live in
            // src/main/keepRules/*.keep (auto-combined by AGP).
            // Switch to `optimization { enable = true }` after upgrading to AGP 9.3+.
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-opt-in=androidx.compose.foundation.style.ExperimentalFoundationStyleApi",
            "-XXLanguage:+PropertyParamAnnotationDefaultTargetMode"
        )
    }
}

composeCompiler {
    reportsDestination = layout.buildDirectory.dir("compose_compiler/reports")
    metricsDestination = layout.buildDirectory.dir("compose_compiler/metrics")
    stabilityConfigurationFiles.add(
        project.layout.projectDirectory.file("compose_compiler_config.conf")
    )
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(platform(libs.androidx.compose.bom))

    implementation(libs.bundles.androidx)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.bundles.okhttp)
    implementation(libs.bundles.retrofit)
    implementation(libs.bundles.kotlin)
    implementation(libs.bundles.hilt)
    ksp(libs.hilt.compiler)
    implementation(libs.bundles.coil)
    implementation(libs.compose.shimmer)

    implementation(libs.timber)
    implementation(libs.wheelpicker)
    implementation(libs.aboutlibraries.compose.m3)
    implementation(libs.lottie.compose)
    implementation(libs.tink.android)
    implementation(libs.kakao.auth)

    testImplementation(libs.bundles.unitTest)
    androidTestImplementation(libs.bundles.test)
    debugImplementation(libs.bundles.debug)
}
