package com.example.grapefruit.ui.permisions

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState

@Composable
fun ManagePermissionsWithPermissionManager(permissionManager: PermissionRequestManager) {

    val permission = permissionManager.permissionState.collectAsState()
    // Needed if something changes during the other activity, just in case...
    val onResult = permissionManager.onResult

    when (permission.value) {
        PermissionRequest.NO_REQUESTS -> {}

        PermissionRequest.NEEDS_PERMISSION -> {


            val activityLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult()
            )
            {
                onResult()
            }
            SideEffect {
                activityLauncher.launch(permissionManager.intent)
            }

        }
    }


}