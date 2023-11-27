package com.example.grapefruit.data.repository

import com.example.grapefruit.data.handleWithFlow
import com.google.api.services.sheets.v4.model.ValueRange
import hu.blueberry.cloud.ResourceState
import hu.blueberry.cloud.google.GoogleSheetsManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SpreadSheetRepository @Inject constructor(
    private var googleSheetsManager: GoogleSheetsManager
) {

    suspend fun readSpreadSheet(spreadSheetId:String, range:String): Flow<ResourceState<ValueRange?>> {
        return handleWithFlow { googleSheetsManager.readSpreadSheet(spreadSheetId,range) }
    }
}