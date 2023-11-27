package com.example.grapefruit.test.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.grapefruit.test.screens.HomeScreen

@Composable
fun AppNavigationGraph(){

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.HOME_SCREEN) {

        composable(Routes.HOME_SCREEN){
            HomeScreen()
        }
    }

}