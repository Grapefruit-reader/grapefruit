package com.example.grapefruit.ui.screens

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.grapefruit.R
import com.example.grapefruit.data.viewmodel.DriveViewModel
import com.example.grapefruit.data.viewmodel.HomeViewModel
import com.example.grapefruit.model.FileInfo
import com.example.grapefruit.navigation.AppNavigationGraph
import com.example.grapefruit.navigation.Routes
import com.example.grapefruit.ui.common.ButtonList
import com.example.grapefruit.ui.common.NormalTextField
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import hu.blueberry.cloud.ResourceState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    homeviewModel: HomeViewModel = hiltViewModel(),
) {
    val fileResponse by homeviewModel.folder.collectAsState()
    val startNewActivityLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // Navigáció
    }
    var folderValue by remember { mutableStateOf("") }
    var isFolderValueError by remember { mutableStateOf(false) }
    var fileList = homeviewModel.spreadSheetsInFolder.collectAsState()



    LaunchedEffect(Unit){
        homeviewModel.init()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background), contentAlignment = Alignment.Center
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {


            Text(
                text = "Choose a Topic",
                fontSize = 25.sp
            )

            Spacer(modifier = Modifier.fillMaxHeight(0.05f))

            Column(
                modifier = Modifier
                    .fillMaxHeight(0.3f)
                    .verticalScroll(
                        rememberScrollState()
                    ), horizontalAlignment = Alignment.CenterHorizontally
            ) {


                when (fileList.value) {
                    is ResourceState.Success -> {

                        val fileInfoList =
                            (fileList.value as ResourceState.Success<List<FileInfo>>).data

                        if (fileInfoList.isNotEmpty()) {
                            ButtonList(list = fileInfoList, onButtonClick = {
                                homeviewModel.setSpreadSheetId(it.id)
                                navController.navigate(Routes.SPREADSHEET_TOOLS_SCREEN)
                            }) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                {
                                    Text(text = it.name)
                                }
                            }
                        } else {
                            Text("Created Spreadsheets will appear here")
                        }

                    }

                    is ResourceState.Loading -> {
                        Text("Loading spreadsheets...")
                    }

                    else -> {
                        Text("Created Spreadsheets will appear here")
                    }

                }
            }

            Spacer(modifier = Modifier.height(60.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Add spreadsheet:"
                )
                Spacer(modifier = Modifier.height(10.dp))
                NormalTextField(
                    value = folderValue,
                    label = stringResource(id = R.string.textfield_label_foldername),
                    onValueChange = { newValue ->
                        folderValue = newValue
                        isFolderValueError = false
                    },
                    isError = isFolderValueError,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Folder,
                            contentDescription = null
                        )
                    },
                    trailingIcon = { },
                    onDone = { }
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    when (fileResponse) {
                        is ResourceState.Initial -> {
                            Button(
                                onClick = {
                                    homeviewModel.upsertFolderFlow(folderValue)
                                },
                                modifier = Modifier.width(280.dp),
                            )
                            {
                                Text(text = "Create Spreadsheet")
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        is ResourceState.Success -> {
                            navController.navigate(Routes.SPREADSHEET_TOOLS_SCREEN)
                        }

                        is ResourceState.Loading -> {
                            CircularProgressIndicator(
                                modifier = Modifier.width(40.dp),
                                color = MaterialTheme.colorScheme.secondary,
                            )
                        }

                        is ResourceState.Error -> {
                            val error = fileResponse as ResourceState.Error<String?>
                            when (error.error) {
                                is UserRecoverableAuthIOException -> {
                                    val intent =
                                        (error.error as UserRecoverableAuthIOException).intent
                                    startNewActivityLauncher.launch(intent)
                                }

                                else -> {
                                    Log.d("InitTab", error.error.toString())

                                    //do something
                                }
                            }
                        }

                        else -> {}
                    }
                }

            }
        }
    }
}

//@Preview
//@Composable
//fun HomeScreenPreview(){
//    HomeScreen()
//}