package com.example.grapefruit.navigation

const val ROOT_GRAPH_ROUTE = "root"
const val AUTH_GRAPH_ROUTE = "auth"
const val MAIN_GRAPH_ROUTE = "main"

sealed class Screen(val route: String) {
    object Login: Screen(route = "login")
    object Home: Screen(route = "home/{${Args.username}}") {
        fun passUsername(username: String) = "home/$username"
        object Args {
            const val username = "username"
        }
    }
    object Profile: Screen(route = "profile")
    object Settings: Screen(route = "settings")
}