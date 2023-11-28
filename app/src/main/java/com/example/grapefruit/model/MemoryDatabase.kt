package com.example.grapefruit.model

import java.io.File

class MemoryDatabase {
    var folderId : String? = null
    var spreadsheetId : String? = null
    var workSheet : String? = null
    var userList: MutableList<User> = mutableListOf()
}