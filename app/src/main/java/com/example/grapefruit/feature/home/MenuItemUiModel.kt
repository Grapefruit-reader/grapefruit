package com.example.grapefruit.feature.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.grapefruit.R
import com.example.grapefruit.navigation.Screen

enum class MenuItemUiModel(
    val text: @Composable () -> Unit,
    val icon: @Composable () -> Unit,
    val screenRoute: String
) {
    PROFILE(
        text = { Text(text = stringResource(id = R.string.dropdown_menu_item_label_profile))},
        icon = {
            Icon(imageVector = Icons.Default.Person, contentDescription = null)
        },
        screenRoute = Screen.Profile.route
    ),
    SETTINGS(
        text = { Text(text = stringResource(id = R.string.dropdown_menu_item_label_settings))},
        icon = {
            Icon(imageVector = androidx.compose.material.icons.Icons.Default.Settings, contentDescription = null)
        },
        screenRoute = Screen.Settings.route
    )
}