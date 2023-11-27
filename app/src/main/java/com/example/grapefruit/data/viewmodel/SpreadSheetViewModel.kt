package com.example.grapefruit.data.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grapefruit.data.handleWithFlow
import com.example.grapefruit.data.repository.SpreadSheetRepository
import com.example.grapefruit.model.MemoryDatabase
import com.google.api.services.sheets.v4.model.ValueRange
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.blueberry.cloud.ResourceState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpreadSheetViewModel @Inject constructor(
    var memoryDatabase: MemoryDatabase,
    var spreadSheetRepository: SpreadSheetRepository
): ViewModel(){

    private var _sheet: MutableStateFlow<ResourceState<String>> = MutableStateFlow(ResourceState.Loading())
    val sheet : StateFlow<ResourceState<String>> = _sheet

     fun readSpreadSheet( range:String) {
         viewModelScope.launch {
             spreadSheetRepository.readSpreadSheet(memoryDatabase.spreadsheetId ?: "", range ).collectLatest {
                 when(it) {
                     is ResourceState.Success-> {
                         it.data?.values?.forEach{
                             Log.d("Sheet", it.toString())
                         }
                     }
                     is ResourceState.Loading -> {
                         Log.i("Sheet", "Loading")
                     }
                     is ResourceState.Error -> {
                         _sheet.value = ResourceState.Error((it).error)
                     }

                     else -> {
                         Log.i("Sheet" , "Else")
                     }
                 }
             }
         }

    }

    fun readSpreadSheetFlow(range:String) {
        viewModelScope.launch {
            handleWithFlow {
                val res = readSpreadSheet(range)

            }.collectLatest {
                when(it) {
                    is ResourceState.Success -> {
                        it.data
                    }
                    else -> {}
                }
            }
        }
    }

}