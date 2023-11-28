package com.example.grapefruit.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.grapefruit.ui.screens.HomeScreen
import com.example.grapefruit.ui.screens.QrScreen
import com.example.grapefruit.ui.screens.SpreadsheetToolsScreen
import com.example.grapefruit.ui.screens.TopicScreen

@Composable
fun AppNavigationGraph(){

    val navController : NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.HOME_SCREEN) {

        composable(Routes.HOME_SCREEN){
            HomeScreen(
                onNavigateToSpreadsheetTools = {navController.navigate(Routes.SPREADSHEET_TOOLS_SCREEN)}
            )
        }

        composable(Routes.SPREADSHEET_TOOLS_SCREEN){
            SpreadsheetToolsScreen(
                onNavigateToTopic = {navController.navigate(Routes.TOPIC_SCREEN)}
            )
        }

        composable(Routes.TOPIC_SCREEN){
            TopicScreen()
        }

        composable(Routes.QR_SCREEN){
            QrScreen()
        }
    }

}