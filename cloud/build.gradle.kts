plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "hu.blueberry.cloud"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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


    implementation(Dependencies.coreKtx)
    implementation(Dependencies.appcompat)
    //implementation(Dependencies.material) valami rossz ezzel idk mi
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
}

kapt{
    correctErrorTypes = true
}