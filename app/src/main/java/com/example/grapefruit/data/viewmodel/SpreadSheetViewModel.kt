package com.example.grapefruit.data.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grapefruit.data.handleResponse
import com.example.grapefruit.data.handleWithFlow
import com.example.grapefruit.data.repository.DriveRepository
import com.example.grapefruit.data.repository.SpreadSheetRepository
import com.example.grapefruit.model.MemoryDatabase
import com.example.grapefruit.model.User
import com.example.grapefruit.ui.permisions.PermissionRequestManager
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.blueberry.cloud.ResourceState
import hu.blueberry.cloud.google.DriveManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SpreadSheetViewModel @Inject constructor(
    private var memoryDatabase: MemoryDatabase,
    private var spreadSheetRepository: SpreadSheetRepository,
    private var driveRepository: DriveRepository,
) : ViewModel() {

    companion object {
        const val TAG = "SpreadSheetViewModel"
    }

    private var _sheet: MutableStateFlow<ResourceState<String>> =
        MutableStateFlow(ResourceState.Initial())
    val sheet: StateFlow<ResourceState<String>> = _sheet
    private var file: File? = null

    val permissionManager = PermissionRequestManager()

    fun resetSheetValue(){
        _sheet.value = ResourceState.Initial()
    }

    fun readSpreadSheet(range: String) {
        viewModelScope.launch {
            spreadSheetRepository.readSpreadSheet(memoryDatabase.spreadsheetId ?: "", range)
                .collectLatest { it ->
                    handleResponse(it,
                        onSuccess = { data ->
                            val rawData = data?.values.toString()

                            val innerLists = rawData
                                .substringAfter("[[")
                                .substringBeforeLast("]]]")
                                .split("], [")

                            val userList = innerLists.map { row ->
                                val (name, share) = row.split(",")
                                User(name, share.toDoubleOrNull() ?: 0.0)
                            }
                            memoryDatabase.userList.clear()
                            memoryDatabase.userList.addAll(userList)
                            _sheet.value = ResourceState.Success(data = "Success")


                        },

                        onLoading = {
                            Log.i("Sheet", "Loading")
                        },
                        onError = { error ->
                            _sheet.value = ResourceState.Error(error)
                            if(error is UserRecoverableAuthIOException){
                                permissionManager.requestPermissionAndRepeatRequest(error.intent){
                                    readSpreadSheet(range)
                                }
                            }

                        })

                }

        }
    }

    fun uploadPdf() {
        viewModelScope.launch {
            driveRepository.generateFile(memoryDatabase.userList, "Lakógyűlés", "QR")
                .collectLatest {
                    handleResponse(it,
                        onSuccess = { createFile(it) },
                        onError = {error ->
                            Log.d(TAG, error.toString())
                        })
                }

        }
    }


    fun createFile(f: File?) {
        file = f
        viewModelScope.launch {
            driveRepository.createFileFlow(
                file?.name!!,
                listOf(memoryDatabase.folderId!!),
                DriveManager.MimeType.PDF,
                file!!
            ).collectLatest {
                handleResponse(it,
                    onSuccess = {},
                    onError = {error ->
                        if(error is UserRecoverableAuthIOException){
                            permissionManager.requestPermissionAndRepeatRequest(error.intent){
                                createFile(f)
                            }
                        }
                    })
            }
        }
    }


    fun readSpreadSheetFlow(range: String) {
        viewModelScope.launch {
            handleWithFlow {
                val res = readSpreadSheet(range)

            }.collectLatest {
                when (it) {
                    is ResourceState.Success -> {
                        it.data
                    }

                    else -> {}
                }
            }
        }
    }


}