package com.example.grapefruit.ui.screens

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.grapefruit.data.viewmodel.SpreadSheetViewModel
import com.example.grapefruit.model.MemoryDatabase
import com.example.grapefruit.utils.generatePdf
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import hu.blueberry.cloud.ResourceState

@Composable
fun SpreadsheetToolsScreen (
    spreadSheetViewModel: SpreadSheetViewModel = hiltViewModel(),
    onNavigateToTopic: () -> Unit
){
    val sheet by spreadSheetViewModel.sheet.collectAsState()
    val startNewActivityLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // Navigáció
    }

    val name = "Spreadsheet name" //TODO: viewmodelből szeretném megkapni
    var rows = "0"

    Box(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background), contentAlignment = Alignment.Center
    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = name,
                fontSize = 25.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {
                    onNavigateToTopic()
                }
            )
            {
                Text(text = "Topic")
            }
            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    spreadSheetViewModel.readSpreadSheet("A2:B")
                }
            )
            {
                Text(text = "Read")
            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {
                    Log.d("Sheet", "generate")
                    spreadSheetViewModel.uploadPdf()
                }
            )
            {
                Text(text = "Print")
            }
            when (sheet) {
                is ResourceState.Success -> {

                }
                is ResourceState.Loading -> {
                }
                is ResourceState.Error -> {
                    val error = sheet as ResourceState.Error<String?>
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

                else -> {}
            }
        }
    }
}

