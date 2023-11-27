package com.example.grapefruit.data.repository

import hu.blueberry.cloud.google.DriveManager

import hu.blueberry.cloud.ResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class DriveRepository @Inject constructor(
    private var driveManager: DriveManager
) {


    suspend fun createFolder(name:String) : Flow<ResourceState<String?>> {
        return flow{
            emit(ResourceState.Loading())

            val response = driveManager.createFolder(name)

            emit(ResourceState.Success(response))

        }.catch{
            e -> emit(ResourceState.Error(e))
        }.flowOn(Dispatchers.IO)
    }





}

