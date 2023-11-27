package com.example.grapefruit.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.grapefruit.R
import com.example.grapefruit.data.viewmodel.DriveViewModel
import com.example.grapefruit.ui.common.NormalTextField
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import hu.blueberry.cloud.ResourceState


@OptIn(ExperimentalMaterial3Api::class)
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
    var folderValue by remember { mutableStateOf("") }
    var isFolderValueError by remember { mutableStateOf(false) }


    Box(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background), contentAlignment = Alignment.Center
    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Add folder:"
            )
            Spacer(modifier = Modifier.height(60.dp))
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
            Button(
            onClick = {
                driveViewModel.createFolder(folderValue)
            },
            modifier = Modifier.width(280.dp),
            )
            {
                Text(text = "Create 'New Folder'")
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Status:")
                when (fileResponse) {
                    is ResourceState.Loading -> {
                        Text(text = "Loading")
                    }

                    is ResourceState.Success -> {
                        Text(text = "Success")
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

                    else -> {}
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