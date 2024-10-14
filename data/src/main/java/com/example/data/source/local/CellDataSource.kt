package com.example.data.source.local

import com.example.data.model.db.CellDb

interface CellDataSource {
    suspend fun insertCell(cell: CellDb)
    suspend fun deleteCell(id: String)
}