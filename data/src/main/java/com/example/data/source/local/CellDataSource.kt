package com.example.data.source.local

import com.example.data.model.db.CellDb
import com.example.model.entities.Cell

interface CellDataSource {
    suspend fun insertCell(cell: CellDb)
    suspend fun deleteCell(cell: Cell)
}