package com.example.grapefruit.data.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grapefruit.data.handleResponse
import com.example.grapefruit.data.repository.SpreadSheetRepository
import com.example.grapefruit.model.MemoryDatabase
import com.example.grapefruit.model.StringValues
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.blueberry.cloud.ResourceState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject
import kotlinx.coroutines.launch


@HiltViewModel
class TopicViewModel @Inject constructor(
    var spreadSheetRepository: SpreadSheetRepository,
    var memoryDatabase: MemoryDatabase
) : ViewModel() {



    companion object {
        const val TAG = "TopicViewModel"
    }


    var voteWorksheets: MutableStateFlow<ResourceState<MutableList<String>>> = MutableStateFlow(ResourceState.Initial())

    fun chooseWorksheet(name: String){
        memoryDatabase.workSheet = name
    }

    fun createNewWorkSheet(name: String) {
        viewModelScope.launch {
            spreadSheetRepository.createWorksheet(memoryDatabase.spreadsheetId!!, name)
                .collectLatest {
                    handleResponse(it,
                        onSuccess = {
                            chooseWorksheet(name)
                            Log.d(TAG, "Successfully added new worksheet: $name") },
                        onError = {error -> Log.d(TAG, error.toString()) }
                    )
                }

        }
    }


    fun listWorkSheets(){
        viewModelScope.launch {
            spreadSheetRepository.listWorksheetNames(memoryDatabase.spreadsheetId!!).collectLatest {
                handleResponse(it,
                    onSuccess = { data ->
                        val mutableList = mutableListOf<String>()
                        mutableList.addAll(data.filter{ tab -> tab != StringValues.FIRST_PAGE_NAME})
                        voteWorksheets.value = ResourceState.Success(mutableList)
                    },
                    onError = { error -> Log.d(TAG, error.toString()) }
                )
            }
        }
    }
}