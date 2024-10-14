package com.example.data.source.local

import com.example.data.model.db.TableDb

interface TableDataSource {
    suspend fun insertTable(table: TableDb)
    suspend fun updateTable(table: TableDb)
    suspend fun deleteTable(id: String)
}