package com.example.grapefruit.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.grapefruit.data.viewmodel.DriveViewModel
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import hu.blueberry.cloud.ResourceState


@Composable
fun HomeScreen(
    driveViewModel: DriveViewModel = hiltViewModel()
){
    val fileResponse by driveViewModel.filename.collectAsState()
    val startNewActivityLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // Navigáció
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ){

        Button(onClick = { 
            driveViewModel.createFolder("New Folder")
        }) {
            Text(text = "Create 'New Folder'")
        }
        
        when(fileResponse){
            is ResourceState.Loading -> { Text(text = "Loading")}
            is ResourceState.Success -> { Text(text = "Success")}
            is ResourceState.Error -> {
                val error = fileResponse as ResourceState.Error<String?>
                when(error.error){
                    is UserRecoverableAuthIOException -> {
                        val intent = (error.error as UserRecoverableAuthIOException).intent
                        startNewActivityLauncher.launch(intent)
                    }
                    else -> {

                        //do something
                    }
                }
            }

            else -> {}
        }

    }
}

@Preview
@Composable
fun HomeScreenPreview(){
    HomeScreen()
}