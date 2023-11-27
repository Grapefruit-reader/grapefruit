package com.example.grapefruit.data.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grapefruit.data.handleWithFlow
import com.example.grapefruit.data.repository.SpreadSheetRepository
import com.example.grapefruit.model.MemoryDatabase
import com.example.grapefruit.model.User
import com.example.grapefruit.utils.generatePdf
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.blueberry.cloud.ResourceState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpreadSheetViewModel @Inject constructor(
    private var memoryDatabase: MemoryDatabase,
    private var spreadSheetRepository: SpreadSheetRepository
): ViewModel(){

    private var _sheet: MutableStateFlow<ResourceState<String>> = MutableStateFlow(ResourceState.Initial())
    val sheet : StateFlow<ResourceState<String>> = _sheet

     fun readSpreadSheet( range:String) {
         viewModelScope.launch {
             spreadSheetRepository.readSpreadSheet(memoryDatabase.spreadsheetId ?: "", range ).collectLatest { it ->
                 when(it) {
                     is ResourceState.Success-> {
                         val rawData = it.data?.values.toString()

                         val innerLists = rawData
                             .substringAfter("[[")
                             .substringBeforeLast("]]]")
                             .split("], [")

                         val userList = innerLists.map { row ->
                             val (name,share) = row.split(",")
                             User(name, share.toDoubleOrNull() ?:0.0)
                         }

                         memoryDatabase.userList.addAll(userList)
                         _sheet.value = ResourceState.Success(data = "Success")

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

    fun generatePdf(title:String, fileName:String) {
        generatePdf(memoryDatabase.userList, title, fileName)
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