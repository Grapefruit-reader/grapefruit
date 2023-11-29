package com.example.grapefruit.data.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grapefruit.data.handleResponse
import com.example.grapefruit.data.handleWithFlow
import com.example.grapefruit.data.repository.DriveRepository
import com.example.grapefruit.data.repository.SpreadSheetRepository
import com.example.grapefruit.model.FileInfo
import com.example.grapefruit.model.MemoryDatabase
import com.example.grapefruit.model.StringValues
import com.example.grapefruit.ui.permisions.PermissionRequestManager
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.blueberry.cloud.ResourceState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private var memoryDatabase: MemoryDatabase,
    private var driveRepository: DriveRepository,
    private var googleSheetRepository: SpreadSheetRepository,
) : ViewModel() {

    var spreadSheetsInFolder: MutableStateFlow<ResourceState<List<FileInfo>>> =
        MutableStateFlow(ResourceState.Initial())
    private var _folder: MutableStateFlow<ResourceState<String>> =
        MutableStateFlow(ResourceState.Initial())
    val folder: StateFlow<ResourceState<String>> = _folder

    val permissionManager = PermissionRequestManager()


    companion object {
        const val TAG = "HomeViewModel"
    }

    fun resetFolder(){
        _folder.value = ResourceState.Initial()
    }

    fun setSpreadSheetId(id: String) {
        memoryDatabase.spreadsheetId = id
    }

    private suspend fun upsertFolder(): String {
        return driveRepository.searchFolderBlocking(StringValues.BASE_FOLDER_NAME)
            ?: driveRepository.createFolderBlocking(
                StringValues.BASE_FOLDER_NAME
            )
    }

    private suspend fun createSpreadSheet(name: String): String? {
        return if (memoryDatabase.folderId != null) {
            driveRepository.createSpreadSheetInFolderBlocking(memoryDatabase.folderId!!, name)
        } else {
            null
        }
    }

    fun upsertFolderFlow(name: String) {
        viewModelScope.launch {
            handleWithFlow {
                val upsertFolder = upsertFolder()
                memoryDatabase.folderId = upsertFolder
                val createSpreadSheet = createSpreadSheet(name)
                memoryDatabase.spreadsheetId = createSpreadSheet
                googleSheetRepository.initializeFirstTab(
                    memoryDatabase.spreadsheetId!!,
                    StringValues.FIRST_PAGE_NAME
                )
            }.collectLatest {
                handleResponse(it,
                    onSuccess = { _folder.value = ResourceState.Success(data = "Success") },
                    onLoading = { _folder.value = ResourceState.Loading() },
                    onError = {
                        Log.e(TAG, it.toString())
                        _folder.value = ResourceState.Error(it)
                        if (it is UserRecoverableAuthIOException) {
                            permissionManager.requestPermissionAndRepeatRequest(it.intent) {
                                upsertFolderFlow(name)
                            }
                        }
                    })
            }
        }
    }


    fun init() {
        viewModelScope.launch {
            driveRepository.upsertFolder().collectLatest {
                handleResponse(it,
                    onSuccess = {
                        memoryDatabase.folderId = it
                        getAllSpreadSheetsInFolder()
                    },
                    onError = {
                        if (it is UserRecoverableAuthIOException) {
                            permissionManager.requestPermissionAndRepeatRequest(it.intent) {
                                init()
                            }
                        }
                    })
            }

        }
    }

    private fun getAllSpreadSheetsInFolder() {
        viewModelScope.launch {
            driveRepository.getAllSpreadSheetsInFolder(listOf(memoryDatabase.folderId!!))
                .collectLatest { fileList ->
                    handleResponse(
                        fileList,
                        onSuccess = { data ->
                            spreadSheetsInFolder.value = ResourceState.Success(data)
                        },
                        onError = { error ->
                            Log.d(TAG, "Error: $error")
                        },
                        onLoading = { spreadSheetsInFolder.value = ResourceState.Loading() },

                        )
                }
        }
    }
}