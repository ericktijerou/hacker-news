import Dependencies.androidAppCompat
import Dependencies.androidConstraintLayout
import Dependencies.androidCore
import Dependencies.androidCoreTesting
import Dependencies.androidDataBindingCompiler
import Dependencies.androidKtx
import Dependencies.androidLiveData
import Dependencies.androidTestEspressoCore
import Dependencies.androidTestExtJunit
import Dependencies.androidTestRules
import Dependencies.androidTestRunner
import Dependencies.androidViewModel
import Dependencies.archCoreTest
import Dependencies.junit
import Dependencies.koinAndroidScope
import Dependencies.koinAndroidViewModel
import Dependencies.koinTest
import Dependencies.kotlinCoroutinesAndroid
import Dependencies.kotlinCoroutinesCore
import Dependencies.kotlinCoroutinesTest
import Dependencies.kotlinSerializationRuntime
import Dependencies.kotlinStdLib
import Dependencies.lifecycle
import Dependencies.lottie
import Dependencies.materialDesign
import Dependencies.mockk
import Dependencies.okHttp
import Dependencies.okHttpInterceptor
import Dependencies.okhttpMockWebServer
import Dependencies.okhttpTls
import Dependencies.paging
import Dependencies.retrofit
import Dependencies.retrofitGsonConverter
import Dependencies.retrofitKotlixConverter
import Dependencies.retrofitScalarsConverter
import Dependencies.roomCompiler
import Dependencies.roomKtx
import Dependencies.roomRuntime
import java.io.FileInputStream
import java.util.*

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlinx-serialization")
}

android {
    val keystorePropertiesFile = rootProject.file("keystore.properties")
    val keystoreProperties = Properties()
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))

    signingConfigs {
        create("release") {
            keyAlias = keystoreProperties.getProperty("keyAlias")
            keyPassword = keystoreProperties.getProperty("keyPassword")
            storeFile = file(keystoreProperties.getProperty("storeFile"))
            storePassword = keystoreProperties.getProperty("storePassword")
        }
    }

    compileSdkVersion(30)

    defaultConfig {
        applicationId = "erick.tijerou.hackernews"
        minSdkVersion(19)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
            isTestCoverageEnabled = true

            buildConfigField(
                "String",
                "API_URL",
                "\"https://hn.algolia.com/\""
            )
        }

        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            isZipAlignEnabled = true
            signingConfig = signingConfigs.getByName("release")

            proguardFile(getDefaultProguardFile("proguard-android.txt"))
            proguardFile(file("proguard-rules.pro"))

            buildConfigField(
                "String",
                "API_URL",
                "\"https://hn.algolia.com/\""
            )
        }
    }

    viewBinding {
        isEnabled = true
    }
    dataBinding {
        isEnabled = true
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }

    compileOptions {
        targetCompatibility = JavaVersion.VERSION_1_8
        sourceCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
        getByName("test").java.srcDirs("src/test/kotlin")
        getByName("androidTest").java.srcDirs("src/androidTest/kotlin")
    }
}

kapt {
    useBuildCache = true
}

dependencies {
    //Android
    implementation(androidAppCompat)
    implementation(androidConstraintLayout)
    implementation(androidCore)
    kapt(androidDataBindingCompiler)
    implementation(androidLiveData)
    implementation(androidViewModel)
    implementation(androidTestEspressoCore)
    implementation(androidKtx)
    implementation(paging)
    implementation(lifecycle)

    implementation(okHttp)
    implementation(okHttpInterceptor)

    //Room
    implementation(roomRuntime)
    kapt(roomCompiler)
    implementation(roomKtx)

    // Retrofit
    implementation(retrofit)
    implementation(retrofitGsonConverter)
    implementation(retrofitScalarsConverter)
    implementation(retrofitKotlixConverter)

    // Google Material Design
    implementation(materialDesign)

    // Koin
    implementation(koinAndroidScope)
    implementation(koinAndroidViewModel)

    // Kotlin
    implementation(kotlinStdLib)
    implementation(kotlinCoroutinesAndroid)
    implementation(kotlinCoroutinesCore)
    implementation(kotlinSerializationRuntime)

    // Lottie
    implementation(lottie)

    // Tests
    testImplementation(junit)
    testImplementation(mockk)
    testImplementation(okhttpTls)
    testImplementation(androidCoreTesting)
    testImplementation(okhttpMockWebServer)
    testImplementation(kotlinCoroutinesTest)

    androidTestImplementation(koinTest)
    androidTestImplementation(archCoreTest)
    androidTestImplementation(okhttpTls)
    androidTestImplementation(androidTestRules)
    androidTestImplementation(androidTestRunner)
    androidTestImplementation(androidTestExtJunit)
    androidTestImplementation(okhttpMockWebServer)
}
