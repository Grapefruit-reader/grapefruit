package com.example.grapefruit.ui.common

import androidx.compose.material3.Button
import androidx.compose.runtime.Composable



@Composable
fun <T> ButtonList(
    list: Collection<T>,
    onButtonClick: (T) -> Unit,
    style: @Composable ((T)->Unit)?

){
    for (item in list) {
        Button(
            onClick = {
            onButtonClick(item)
        }) {
            if (style != null) {
                style(item)
            }
        }
    }
}