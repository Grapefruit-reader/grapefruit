package com.example.grapefruit.data.repository

import com.example.grapefruit.data.handleWithFlow
import com.example.grapefruit.model.MemoryDatabase
import com.example.grapefruit.model.User
import com.example.grapefruit.utils.generatePdf
import hu.blueberry.cloud.google.DriveManager

import hu.blueberry.cloud.ResourceState
import kotlinx.coroutines.flow.Flow
import java.io.File
import javax.inject.Inject


class DriveRepository @Inject constructor(
    private var driveManager: DriveManager
) {
    suspend fun createFolder(name: String): Flow<ResourceState<String>> {
        return handleWithFlow { driveManager.createFolder(name) }
    }

    suspend fun createFolderFlow(name: String): Flow<ResourceState<String>> {
        return handleWithFlow { driveManager.createFolder(name) }
    }

    suspend fun createSpreadSheetInFolderFlow(
        folderId: String,
        sheetName: String
    ): Flow<ResourceState<String>> {
        return handleWithFlow { driveManager.createSpreadSheetInFolder(folderId, sheetName) }
    }
    suspend fun generateFile(userList: List<User>, title:String, fileName:String): Flow<ResourceState<File?>> {
        return handleWithFlow { generatePdf(userList,title, fileName) }
    }

    suspend fun createFileFlow(
        fileName: String,
        parents: List<String>,
        mimeType: String,
        file:File
    ): Flow<ResourceState<String>> {
        return handleWithFlow { driveManager.createFile(fileName, parents, mimeType, file) }
    }

    suspend fun searchFolderFlow(name: String): Flow<ResourceState<String?>> {
        return handleWithFlow { driveManager.searchFolder(name) }
    }

    suspend fun createFolderBlocking(name: String): String {
        return driveManager.createFolder(name)
    }

    suspend fun searchFolderBlocking(name: String): String? {
        return driveManager.searchFolder(name)
    }

    suspend fun createSpreadSheetInFolderBlocking(
        folderId: String,
        sheetName: String
    ): String {
        return  driveManager.createSpreadSheetInFolder(folderId, sheetName)
    }

    suspend fun createFileFlowBlocking(
        fileName: String,
        parents: List<String>,
        mimeType: String,
        file:File
    ): String {
        return driveManager.createFile(fileName, parents, mimeType,file)
    }

}





