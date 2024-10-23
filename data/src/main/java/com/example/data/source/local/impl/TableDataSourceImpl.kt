package com.example.data.source.local.impl

import com.example.data.model.db.TableDb
import com.example.data.source.local.CellDataSource
import com.example.data.source.local.TableDataSource
import com.example.data.source.local.dao.TableDao
import com.example.model.entities.Table
import javax.inject.Inject

class TableDataSourceImpl @Inject constructor(
    private val tableDao: TableDao,
    private val cellDataSource: CellDataSource,
) : TableDataSource {

    override suspend fun insertTable(table: TableDb) {
        tableDao.insertTable(table.table)
        table.cells.forEach { currentCell -> cellDataSource.insertCell(currentCell) }
    }

    override suspend fun deleteTable(table: Table) {
        tableDao.deleteTable(table.id)
        table.cells.forEach { currentCell -> cellDataSource.deleteCell(currentCell) }
    }
}