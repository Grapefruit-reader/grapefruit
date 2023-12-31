package com.example.grapefruit.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.grapefruit.R
import com.example.grapefruit.data.viewmodel.TopicViewModel
import com.example.grapefruit.navigation.Routes
import com.example.grapefruit.ui.common.ButtonList
import com.example.grapefruit.ui.common.NormalTextField
import hu.blueberry.cloud.ResourceState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicScreen(
    navController: NavController,
    topicViewModel: TopicViewModel = hiltViewModel()
) {

    var createWorksheetValue by remember { mutableStateOf("") }
    var isWorksheetValueError by remember { mutableStateOf(false) }

    val worksheetList = topicViewModel.voteWorksheets.collectAsState()

    LaunchedEffect(Unit){
        topicViewModel.listWorkSheets()
    }

    @Composable
    fun ScrollableList() {

        Column (horizontalAlignment = Alignment.CenterHorizontally){


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


                when (worksheetList.value) {
                    is ResourceState.Success -> {

                        ButtonList(
                            list = (worksheetList.value as ResourceState.Success<MutableList<String>>).data,
                            onButtonClick = {
                                topicViewModel.chooseWorksheet(it)
                                navController.navigate(Routes.QR_SCREEN)
                            },
                            style = {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                {
                                    Text(text = it)
                                }
                            })
                    }

                    else -> {
                        Text(text = "Loading Tabs...")
                    }
                }

            }
        }
    }

    @Composable
    fun WorksheetInputTextField() {
        NormalTextField(
            value = createWorksheetValue,
            label = stringResource(id = R.string.textfield_label_worksheetname),
            onValueChange = { newValue ->
                isWorksheetValueError =
                    (worksheetList.value as ResourceState.Success<MutableList<String>>).data.contains(
                        newValue
                    )
                createWorksheetValue = newValue
            },
            isError = isWorksheetValueError,
            errorMessage = stringResource(id = R.string.error_message_name_taken),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Folder,
                    contentDescription = null
                )
            },
            trailingIcon = { },
            onDone = { }
        )
    }

    @Composable
    fun CreateTopicButton() {
        Button(
            enabled = !isWorksheetValueError,
            onClick = {
                topicViewModel.createNewWorkSheet(createWorksheetValue)
                navController.navigate(Routes.QR_SCREEN)
            }
        )
        {
            Text(text = "Create Topic")
        }
    }

    @Composable
    fun Createtopic() {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                text = "Create Topic",
                fontSize = 25.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            WorksheetInputTextField()

            Spacer(modifier = Modifier.height(10.dp))

            CreateTopicButton()

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { navController.popBackStack() }
            )
            {
                Text(text = "Back")
            }

        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background), contentAlignment = Alignment.Center
    ) {
        Column(
            Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            ScrollableList()

            Createtopic()

        }
    }

}
