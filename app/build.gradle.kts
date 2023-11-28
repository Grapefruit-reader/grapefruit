plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.grapefruit"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.grapefruit"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/*"
        }
    }
}

dependencies {
    implementation("androidx.camera:camera-core:1.3.0")
    implementation("androidx.camera:camera-lifecycle:1.3.0")
    implementation("androidx.camera:camera-view:1.3.0")
    val composeBom = platform(Dependencies.composeBom)
    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation (Dependencies.material)
    implementation(Dependencies.composeUi)
    implementation(Dependencies.composeUiToolingPreview)
    implementation(Dependencies.composeMaterial)

    androidTestImplementation(Dependencies.composeTestJUnit4)
    debugImplementation(Dependencies.composeTestManifest)
    debugImplementation(Dependencies.composeUiTooling)

    implementation(Dependencies.coreKtx)
    implementation(Dependencies.activityCompose)

    implementation(Dependencies.navigationCompose)

    testImplementation(Dependencies.junit)
    androidTestImplementation(Dependencies.androidTest)
    androidTestImplementation(Dependencies.androidTestEspresso)


    //GoogleDrive
    implementation(Dependencies.googleDrive)

    //GoogleSheets
    implementation(Dependencies.googleSheets)


    //GoogleDependecies
    implementation(Dependencies.googleApiClient)
    implementation(Dependencies.googleAuth)
    //implementation("com.google.oauth-client:google-oauth-client-jetty:1.34.1")


    implementation(Dependencies.playServicesAuth)
    implementation(Dependencies.playServicesDrive)

    //dependency injection
    implementation(Dependencies.dagger)
    kapt(Dependencies.androidCompiler)
    kapt(Dependencies.hiltCompiler)

    //navigation compose
    implementation(Dependencies.hiltNavigationCompose)

    implementation(Dependencies.coroutinesCore)
    implementation(Dependencies.coroutinesAndroid)

    implementation(Dependencies.splashScreen)
    implementation(Dependencies.QRCode)

    //Modules
    implementation(project(Modules.cloud))

}

kapt{
    correctErrorTypes = true
}