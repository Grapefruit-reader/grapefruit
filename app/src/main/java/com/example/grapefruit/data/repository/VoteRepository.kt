package com.example.grapefruit.data.repository

import com.example.grapefruit.data.handleWithFlow
import com.google.api.services.sheets.v4.model.AppendValuesResponse
import hu.blueberry.cloud.ResourceState
import hu.blueberry.cloud.google.GoogleSheetsManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VoteRepository @Inject constructor(
    private var googleSheetsManager: GoogleSheetsManager,
) {

    suspend fun appendToSpreadSheet(spreasheetId:String, range:String, values: MutableList<MutableList<Any>>): Flow<ResourceState<AppendValuesResponse?>> {
       return handleWithFlow {
            googleSheetsManager.appendToSpreadSheet(spreasheetId, range, values)
        }
    }
}