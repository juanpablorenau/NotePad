package com.example.data.source.local.impl

import com.example.data.model.db.CellDb
import com.example.data.source.local.CellDataSource
import com.example.data.source.local.FormatTextDataSource
import com.example.data.source.local.dao.CellDao
import com.example.data.utils.tryRoom
import javax.inject.Inject

class CellDataSourceImpl @Inject constructor(
    private val cellDao: CellDao,
    private val formatTextDataSource: FormatTextDataSource,
) : CellDataSource {
    override suspend fun insertCell(cell: CellDb) {
       tryRoom(this.toString()) { cellDao.insertCell(cell.cell) }
        formatTextDataSource.insertFormatText(cell.formatText)
    }

    override suspend fun deleteCell(id: String) {
    }
}