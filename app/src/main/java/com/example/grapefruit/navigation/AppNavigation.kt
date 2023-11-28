package com.example.grapefruit.navigation

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.grapefruit.ui.screens.HomeScreen
import com.example.grapefruit.ui.screens.QrScreen
import com.example.grapefruit.ui.screens.SpreadsheetToolsScreen
import com.example.grapefruit.ui.screens.TopicScreen

@OptIn(ExperimentalGetImage::class) @Composable
fun AppNavigationGraph(){

    val navController : NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.HOME_SCREEN) {

        composable(Routes.HOME_SCREEN){
            HomeScreen(navController)
        }

        composable(Routes.SPREADSHEET_TOOLS_SCREEN){
            SpreadsheetToolsScreen(navController)
        }

        composable(Routes.TOPIC_SCREEN){
            TopicScreen(navController)
        }

        composable(Routes.QR_SCREEN){
            QrScreen(navController)
        }
    }

}