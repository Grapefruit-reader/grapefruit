package com.example.grapefruit.data.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grapefruit.data.handleWithFlow
import com.example.grapefruit.data.repository.DriveRepository
import com.example.grapefruit.data.repository.SpreadSheetRepository
import com.example.grapefruit.model.MemoryDatabase
import com.example.grapefruit.model.StringValues
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.blueberry.cloud.ResourceState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    var memoryDatabase: MemoryDatabase,
    var driveRepository: DriveRepository,
    var googleSheetRepository: SpreadSheetRepository
) : ViewModel(){

    companion object{
        const val TAG = "HomeViewModel"
    }

    private var _folder: MutableStateFlow<ResourceState<String>> = MutableStateFlow(ResourceState.Initial())
    val folder : StateFlow<ResourceState<String>> = _folder

    private suspend fun upsertFolder():String {
        return driveRepository.searchFolderBlocking(StringValues.BASE_FOLDER_NAME) ?: driveRepository.createFolderBlocking(
            StringValues.BASE_FOLDER_NAME
        )
    }

    private  suspend fun createSpreadSheet(name:String): String? {
        return if(memoryDatabase.folderId != null) {
            driveRepository.createSpreadSheetInFolderBlocking(memoryDatabase.folderId!!,name)
        }else {
            null
        }
    }

    fun upsertFolderFlow(name:String) {
        viewModelScope.launch {
            handleWithFlow {
                val upsertFolder = upsertFolder()
                memoryDatabase.folderId = upsertFolder
                val createSpreadSheet = createSpreadSheet(name)
                memoryDatabase.spreadsheetId = createSpreadSheet
                googleSheetRepository.initializeFirstTab(memoryDatabase.spreadsheetId!!,StringValues.FIRST_PAGE_NAME)
            }.collectLatest {
                when(it) {
                    is ResourceState.Success -> {
                        _folder.value = ResourceState.Success(data = "Success")
                    }
                    is ResourceState.Loading -> {
                        _folder.value = ResourceState.Loading()
                    }
                    else ->{
                        Log.e(TAG, it.toString())
                        _folder.value = ResourceState.Error((it as ResourceState.Error).error)
                    }
                }
            }
        }
    }
}