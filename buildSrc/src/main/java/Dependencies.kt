object Dependencies {

    val coreKtx by lazy { "androidx.core:core-ktx:${Versions.coreKtx}"}
    val composeBom by lazy { "androidx.compose:compose-bom:2023.01.00" }
    val appcompat by lazy { "androidx.appcompat:appcompat:${Versions.appcompat}" }
    val material by lazy { "androidx.compose.material3:material3" }
    val composeUi by lazy { "androidx.compose.ui:ui" }
    val composeUiToolingPreview by lazy { "androidx.compose.ui:ui-tooling-preview" }
    val composeMaterial by lazy { "androidx.compose.material:material-icons-extended" }
    val composeTestJUnit4 by lazy { "androidx.compose.ui:ui-test-junit4" }
    val composeTestManifest by lazy { "androidx.compose.ui:ui-test-manifest" }
    val composeUiTooling by lazy { "androidx.compose.ui:ui-tooling" }
    val junit by lazy { "junit:junit:${Versions.junit}" }
    val androidTest by lazy { "androidx.test.ext:junit:${Versions.androidTest}" }
    val androidTestEspresso by lazy { "androidx.test.espresso:espresso-core:${Versions.androidTestEspresso}" }
    val activityCompose by lazy { "androidx.activity:activity-compose:${Versions.activityCompose}" }
    val navigationCompose by lazy { "androidx.navigation:navigation-compose:${Versions.navigationCompose}" }

    val googleDrive by lazy { "com.google.apis:google-api-services-drive:${Versions.googleDrive}" }
    val googleSheets by lazy { "com.google.apis:google-api-services-sheets:${Versions.googleSheets}" }

    val googleApiClient by lazy { "com.google.api-client:google-api-client-android:${Versions.googleApiClient}" }
    val googleAuth by lazy { "com.google.auth:google-auth-library-oauth2-http:${Versions.googleAuth}" }

    val playServicesAuth by lazy { "com.google.android.gms:play-services-auth:${Versions.playServicesAuth}" }
    val playServicesDrive by lazy { "com.google.android.gms:play-services-drive:${Versions.playServicesDrive}" }

    val dagger by lazy { "com.google.dagger:hilt-android:${Versions.dagger}" }
    val androidCompiler by lazy { "com.google.dagger:hilt-android-compiler:${Versions.androidCompiler}" }
    val hiltCompiler by lazy { "androidx.hilt:hilt-compiler:${Versions.hiltCompiler}" }

    val hiltNavigationCompose by lazy { "androidx.hilt:hilt-navigation-compose:${Versions.hiltNavigationCompose}" }

    val coroutinesCore by lazy { "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}" }
    val coroutinesAndroid by lazy { "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}" }

    val splashScreen by lazy {"androidx.core:core-splashscreen:${Versions.splashScreen}"}
    val QRCode by lazy {"com.journeyapps:zxing-android-embedded:${Versions.QRCode}"}
}

object Modules{
    const val cloud = ":cloud"
}