package com.example.grapefruit.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.grapefruit.R
import com.example.grapefruit.feature.home.MenuItemUiModel
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
@Composable
fun HomeScreen(
    argument: String,
    modifier: Modifier = Modifier,
    onLogout: () -> Unit,
    onMenuItemClick: (String) -> Unit
) {

    val snackbarHostState = remember { SnackbarHostState() }

    var expandedMenu by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.top_app_bar_title_home))
                },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(imageVector = Icons.Default.Logout, contentDescription = null)
                    }
                    IconButton(onClick = { expandedMenu = !expandedMenu }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                scope.launch {
                    snackbarHostState.showSnackbar(message = context.getString(R.string.snackbar_message_this_is_a))
                }
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        },
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
        ) {
            Text(
                text = "Hello, $argument!",
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
            Box(modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.TopEnd).padding(5.dp)) {
                Menu(
                    expanded = expandedMenu,
                    items = MenuItemUiModel.values(),
                    onDismissRequest = { expandedMenu = false },
                    onClick = {
                        onMenuItemClick(it)
                        expandedMenu = false
                    },
                )
            }
        }
    }
}
@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
fun HomeScreen_Preview() {
    HomeScreen(
        argument = "Felhasználó",
        onLogout = {},
        onMenuItemClick = {}
    )
}