package com.example.data.source.local.impl

import com.example.data.model.db.CellDb
import com.example.data.source.local.CellDataSource
import com.example.data.source.local.FormatTextDataSource
import com.example.data.source.local.dao.CellDao
import com.example.model.entities.Cell
import javax.inject.Inject

class CellDataSourceImpl @Inject constructor(
    private val cellDao: CellDao,
    private val formatTextDataSource: FormatTextDataSource,
) : CellDataSource {

    override suspend fun insertCell(cell: CellDb) {
        cellDao.insertCell(cell.cell)
        formatTextDataSource.insertFormatText(cell.formatText)
    }

    override suspend fun deleteCell(cell: Cell) {
        cellDao.deleteCell(cell.id)
        formatTextDataSource.deleteFormatText(cell.formatText)
    }
}