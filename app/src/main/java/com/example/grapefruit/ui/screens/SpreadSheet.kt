package com.example.grapefruit.ui.screens

import android.view.Display.Mode
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.grapefruit.data.viewmodel.SpreadSheetViewModel
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import hu.blueberry.cloud.ResourceState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpreadSheet(spreadSheetViewModel: SpreadSheetViewModel) {
    val fileResponse by spreadSheetViewModel.sheet.collectAsState()
    val startNewActivityLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // Navigáció
    }

    Box(modifier = Modifier.fillMaxSize()){
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "sheetdata:")
            Spacer(modifier = Modifier.height(60.dp))
            Button(onClick = {spreadSheetViewModel.readSpreadSheet("A1:A6")}, modifier = Modifier.size(200.dp, 100.dp)) {
                Text(text = "Click")
            }
            when(fileResponse) {
                is ResourceState.Success -> {

                }
                is ResourceState.Loading -> {

                }
                is ResourceState.Error -> {
                    val error = fileResponse as ResourceState.Error<String?>
                    when (error.error) {
                        is UserRecoverableAuthIOException -> {
                            val intent = (error.error as UserRecoverableAuthIOException).intent
                            startNewActivityLauncher.launch(intent)
                        }

                        else -> {

                            //do something
                        }
                    }
                }
            }
        }
    }
}