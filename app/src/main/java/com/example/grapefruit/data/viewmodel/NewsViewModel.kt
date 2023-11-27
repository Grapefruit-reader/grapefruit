package com.example.grapefruit.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grapefruit.data.repository.DriveRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.blueberry.cloud.ResourceState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DriveViewModel @Inject constructor(
    var driveRepository: DriveRepository
) : ViewModel(){

    private var _filename: MutableStateFlow<ResourceState<String>> = MutableStateFlow(ResourceState.Loading())
    val filename : StateFlow<ResourceState<String>> = _filename
    fun createFolder(name: String){
        viewModelScope.launch {
            driveRepository.createFolder(name)
                .collectLatest {
                    fileName ->
                    _filename.value = fileName
                }
        }

    }
}