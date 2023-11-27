package com.example.grapefruit.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grapefruit.data.repository.VoteRepository
import com.example.grapefruit.model.MemoryDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.blueberry.cloud.ResourceState
import hu.blueberry.cloud.google.GoogleSheetsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WriteVoteViewModel @Inject constructor(
    private var voteRepository: VoteRepository,
    private var memoryDatabase: MemoryDatabase
): ViewModel(){
    object Ranges{
        const val YES_RANGE = "A:B"
        const val NO_RANGE = "D:E"
        const val RESIDE_RANGE = "G:H"
    }

    private var voteWriting : MutableStateFlow<ResourceState<String>> = MutableStateFlow(ResourceState.Initial())


    fun writeYes(name:String, share:Double){
        val values = mutableListOf(mutableListOf<Any>(name, share))
        val range = memoryDatabase.workSheet + Ranges.YES_RANGE

        viewModelScope.launch {
            voteRepository.appendToSpreadSheet(memoryDatabase.spreadsheetId!!,range, values).collectLatest {
                it
            }
        }
    }

    fun writeNo(name:String, share:Double){
        val values = mutableListOf(mutableListOf<Any>(name, share))
        val range = memoryDatabase.workSheet + Ranges.NO_RANGE

        viewModelScope.launch {
            voteRepository.appendToSpreadSheet(memoryDatabase.spreadsheetId!!,range, values)
        }
    }

    fun writeResides(name:String, share:Double){
        val values = mutableListOf(mutableListOf<Any>(name, share))
        val range = memoryDatabase.workSheet + Ranges.RESIDE_RANGE

        viewModelScope.launch {
            voteRepository.appendToSpreadSheet(memoryDatabase.spreadsheetId!!,range, values)
        }
    }

}