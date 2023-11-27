package com.example.grapefruit.data.repository

import com.example.grapefruit.data.handleWithFlow
import hu.blueberry.cloud.google.DriveManager

import hu.blueberry.cloud.ResourceState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class DriveRepository @Inject constructor(
    private var driveManager: DriveManager
) {
    suspend fun createFolder(name: String): Flow<ResourceState<String>> {
        return handleWithFlow { driveManager.createFolder(name) }
    }

    suspend fun createSpreadSheetInFolder(
        folderId: String,
        sheetName: String
    ): Flow<ResourceState<String>> {
        return handleWithFlow { driveManager.createSpreadSheetInFolder(folderId, sheetName) }
    }

    suspend fun createFile(
        fileName: String,
        parents: List<String>,
        mimeType: String
    ): Flow<ResourceState<String>> {
        return handleWithFlow { driveManager.createFile(fileName, parents, mimeType) }
    }

    suspend fun searchFile(name: String): Flow<ResourceState<String?>> {
        return handleWithFlow { driveManager.searchFolder(name) }
    }


}





