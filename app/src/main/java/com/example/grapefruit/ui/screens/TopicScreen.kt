package com.example.grapefruit.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.grapefruit.data.viewmodel.DriveViewModel

@Composable
fun TopicScreen (driveViewModel: DriveViewModel = hiltViewModel()){
    val topicName = "Topic name" //TODO: viewmodelből szeretném megkapni

    Box(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background), contentAlignment = Alignment.Center
    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = topicName,
                fontSize = 25.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {}
            )
            {
                Text(text = topicName)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {}
            )
            {
                Text(text = "Back")
            }

        }
    }

}