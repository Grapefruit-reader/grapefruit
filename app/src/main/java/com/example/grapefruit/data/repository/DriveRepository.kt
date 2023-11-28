package com.example.grapefruit.data.repository

import com.example.grapefruit.data.handleWithFlow
import com.example.grapefruit.model.FileInfo
import com.example.grapefruit.model.StringValues
import com.example.grapefruit.model.User
import com.example.grapefruit.utils.generatePdf
import com.google.api.services.drive.model.FileList
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

    suspend fun generateFile(
        userList: List<User>,
        title: String,
        fileName: String
    ): Flow<ResourceState<File?>> {
        return handleWithFlow { generatePdf(userList, title, fileName) }
    }

    suspend fun createFileFlow(
        fileName: String,
        parents: List<String>,
        mimeType: String,
        file: File
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
        return driveManager.createSpreadSheetInFolder(folderId, sheetName)
    }

    suspend fun createFileFlowBlocking(
        fileName: String,
        parents: List<String>,
        mimeType: String,
        file: File
    ): String {
        return driveManager.createFile(fileName, parents, mimeType, file)
    }


    private fun _getAllSpreadSheetsInFolder(
        parentsList: List<String>,
        mimeType: String = DriveManager.MimeType.SPREADSHEET
    ): List<FileInfo> {

        val fileList = driveManager.searchFilesInFolder(parentsList, mimeType)

        return fileList.map { FileInfo(name = it.name, id = it.id) }
    }

    suspend fun getAllSpreadSheetsInFolder(
        parentsList: List<String>,
        mimeType: String = DriveManager.MimeType.SPREADSHEET
    ): Flow<ResourceState<List<FileInfo>>> {
        return handleWithFlow {
            _getAllSpreadSheetsInFolder(parentsList, mimeType)
        }
    }


    suspend fun upsertFolder(): Flow<ResourceState<String>> {
        return handleWithFlow {  searchFolderBlocking(StringValues.BASE_FOLDER_NAME) ?: createFolderBlocking(
            StringValues.BASE_FOLDER_NAME
        )
        }
    }

}





