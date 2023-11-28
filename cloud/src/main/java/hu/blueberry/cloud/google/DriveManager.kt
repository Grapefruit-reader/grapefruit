package hu.blueberry.cloud.google

import android.content.Context
import android.os.Environment
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn

import com.google.api.client.googleapis.extensions.android.gms.auth.*
import com.google.api.client.http.FileContent
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File
import com.google.api.services.drive.model.FileList
import com.google.api.services.sheets.v4.Sheets
import dagger.hilt.android.qualifiers.ApplicationContext
import hu.blueberry.cloud.google.base.CloudBase
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DriveManager @Inject constructor(
    private var cloudBase: CloudBase
) {

    var scopes = listOf(DriveScopes.DRIVE_FILE, DriveScopes.DRIVE_METADATA)

    object MimeType {
        const val FOLDER = "application/vnd.google-apps.folder"
        const val SPREADSHEET = "application/vnd.google-apps.spreadsheet"
        const val PDF = "application/pdf"
    }

    companion object{
        const val TAG = "DriveManager"
    }

    val drive: Drive
        get() = getDriveService()!!


    private fun getDriveService(): Drive? {
        return Drive.Builder(
            NetHttpTransport(), GsonFactory.getDefaultInstance(),
            cloudBase.getCredentials(scopes)
        ).setApplicationName("Grapefruit").build()
    }


    fun createFolder(name: String): String {

        val folder: File

        val folderData = File().apply {
            this.name = name
            this.mimeType = MimeType.FOLDER
        }

        folder = drive.files().create(folderData).execute()

        return folder.id


    }


    fun searchFolder(name: String): String? {

        val folderId: String?

        val files = drive.files().list()
        files.q = "mimeType='${MimeType.FOLDER}'"

        val result = files.execute()

        result.files.forEach {
            Log.d(TAG, it.id ?: "no id")
        }

        val folder = result.files.filter {
            it.name == name
        }.firstOrNull()

        folderId = folder?.id

        return folderId

    }


    fun searchFilesInFolder(parentIdList: List<String>, mimeType: String): MutableList<File> {

        val files = drive.files().list()

        val stringBuilder = StringBuilder()

        for(parent in parentIdList){
            stringBuilder.append(" and '${parent}' in parents")
        }

        files.q = "mimeType='${mimeType}'" + stringBuilder.toString()

        val result = files.execute()

        result.files.forEach {
            Log.d(TAG, "File(name = ${it.name}, id = ${it.id})")
        }

        return result.files ?: mutableListOf()

    }

    fun createSpreadSheetInFolder(folderId: String, sheetName: String): String {
        val folderData = File().apply {
            this.name = sheetName
            this.mimeType = MimeType.SPREADSHEET
            this.parents = listOf(folderId)
        }

        val file = drive.files().create(folderData).execute()
        return file.id
    }


    fun createFile(
        name: String,
        parents: List<String>,
        mimeType: String,
        file: java.io.File
    ): String {

        var f = File().apply {
            this.name = name
            this.parents = parents
            this.mimeType = mimeType
        }


        val mediaContent = FileContent(MimeType.PDF, file)

        f = drive.files().create(f, mediaContent).execute()
        return f.id
    }

}