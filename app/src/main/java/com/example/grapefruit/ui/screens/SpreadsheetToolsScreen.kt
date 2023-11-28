package com.example.grapefruit.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.grapefruit.data.viewmodel.SpreadSheetViewModel
import com.example.grapefruit.model.StringValues
import com.example.grapefruit.navigation.Routes
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import hu.blueberry.cloud.ResourceState

@Composable
fun SpreadsheetToolsScreen (
    navController: NavController,
    spreadSheetViewModel: SpreadSheetViewModel = hiltViewModel(),
){
    val sheet by spreadSheetViewModel.sheet.collectAsState()
    val startNewActivityLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        navController.navigate(Routes.HOME_SCREEN)
    }

    val name = "Spreadsheet name" //TODO: viewmodelből szeretném megkapni
    val context = LocalContext.current
    var hasReadSheet by remember {
        mutableStateOf(false)
    }

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
                    navController.navigate(Routes.TOPIC_SCREEN)
                }
            )
            {
                Text(text = "Topic")
            }
            Spacer(modifier = Modifier.height(10.dp))
            if(!hasReadSheet) {
                Button(
                    onClick = {
                        hasReadSheet = true
                        spreadSheetViewModel.readSpreadSheet("${StringValues.FIRST_PAGE_NAME}!A2:B")
                    }
                )
                {
                    Text(text = "Read")
                }
            }
            if(hasReadSheet) {
                Button(
                    onClick = {
                        hasReadSheet = false
                        Log.d("Sheet", "generate")
                        spreadSheetViewModel.uploadPdf()
                    }
                )
                {
                    Text(text = "Print")
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {
                    hasReadSheet = false
                    navController.popBackStack()
                }
            )
            {
                Text(text = "Back")
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
                            Toast.makeText(context, error.error.toString(), Toast.LENGTH_LONG).show()
                        }
                    }
                }
                else -> {}
            }
        }
    }
}

