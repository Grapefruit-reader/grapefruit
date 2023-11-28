package com.example.grapefruit.data.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grapefruit.data.handleResponse
import com.example.grapefruit.data.repository.VoteRepository
import com.example.grapefruit.model.MemoryDatabase
import com.example.grapefruit.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.blueberry.cloud.ResourceState
import hu.blueberry.cloud.google.GoogleSheetsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WriteVoteViewModel @Inject constructor(
    private var voteRepository: VoteRepository,
    private var memoryDatabase: MemoryDatabase
): ViewModel(){

    init {
        memoryDatabase.workSheet = "Teszt"
    }
    var user : MutableStateFlow<User?> = MutableStateFlow(null)

    fun setData(user:User){
        buttonState.value = ButtonStates.ACTIVE
        this.user.value = user
    }

    var buttonState = MutableStateFlow(ButtonStates.DISABLED)

    object ButtonStates {
         val ACTIVE = "active"
        val DISABLED = "disaled"
    }
    object Ranges{
        const val YES_RANGE = "A:B"
        const val NO_RANGE = "D:E"
        const val RESIDE_RANGE = "G:H"
    }

    private var _voteWriting : MutableStateFlow<ResourceState<String>> = MutableStateFlow(ResourceState.Initial())
    var voteWriting : StateFlow<ResourceState<String>> = _voteWriting

    fun writeFromData(func : (user:User) -> Unit){
        user.value?.let {
           func(it)
        }
    }

    fun writeYesFromData() = writeFromData { user -> writeYes(user.name,user.share ) }
    fun writeNoFromData() = writeFromData { user -> writeNo(user.name,user.share ) }
    fun writeResidesFromData() = writeFromData { user -> writeResides(user.name,user.share ) }


    fun writeYes(name:String, share:Double){
        buttonState.value =  ButtonStates.DISABLED

        val values = mutableListOf(mutableListOf<Any>(name, share))
        val range = memoryDatabase.workSheet + "!" + Ranges.YES_RANGE

        viewModelScope.launch {
            voteRepository.appendToSpreadSheet(memoryDatabase.spreadsheetId!!,range, values).collectLatest { it ->
                handleResponse(it,
                    onSuccess = {_voteWriting.value = ResourceState.Success(data = "Success")},
                    onLoading = { _voteWriting.value = ResourceState.Loading()},
                    onError = {error -> _voteWriting.value = ResourceState.Error(error)}
                )
            }
        }
    }

    fun writeNo(name:String, share:Double){
        buttonState.value =  ButtonStates.DISABLED

        val values = mutableListOf(mutableListOf<Any>(name, share))
        val range = memoryDatabase.workSheet + "!" + Ranges.NO_RANGE

        viewModelScope.launch {
            voteRepository.appendToSpreadSheet(memoryDatabase.spreadsheetId!!,range, values).collectLatest {
                handleResponse(it,
                    onSuccess = {_voteWriting.value = ResourceState.Success(data = "Success")},
                    onLoading = { _voteWriting.value = ResourceState.Loading()},
                    onError = {error -> _voteWriting.value = ResourceState.Error(error)}
                )
            }
        }
    }

    fun writeResides(name:String, share:Double){
        buttonState.value =  ButtonStates.DISABLED

        val values = mutableListOf(mutableListOf<Any>(name, share))
        val range = memoryDatabase.workSheet + "!" + Ranges.RESIDE_RANGE

        viewModelScope.launch {
            voteRepository.appendToSpreadSheet(memoryDatabase.spreadsheetId!!,range, values).collectLatest {
                handleResponse(it,
                    onSuccess = {_voteWriting.value = ResourceState.Success(data = "Success")},
                    onLoading = { _voteWriting.value = ResourceState.Loading()},
                    onError = {error -> _voteWriting.value = ResourceState.Error(error)}
                )
            }
        }
    }

}