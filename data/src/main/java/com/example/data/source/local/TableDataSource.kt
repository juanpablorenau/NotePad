package com.example.data.source.local

import com.example.data.model.db.TableDb
import com.example.model.entities.Table

interface TableDataSource {
    suspend fun insertTable(table: TableDb)
    suspend fun deleteTable(table: Table)
}