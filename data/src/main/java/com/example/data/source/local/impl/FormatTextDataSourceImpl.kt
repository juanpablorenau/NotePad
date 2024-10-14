package com.example.data.source.local.impl

import com.example.data.model.db.FormatTextDb
import com.example.data.source.local.FormatTextDataSource
import com.example.data.source.local.dao.FormatTextDao
import javax.inject.Inject

class FormatTextDataSourceImpl @Inject constructor(
    private val formatTextDao: FormatTextDao,
) : FormatTextDataSource {

    override suspend fun insertFormatText(formatText: FormatTextDb) {
        formatTextDao.insertFormatText(formatText)
    }

    override suspend fun deleteFormatText(id: String) {
    }
}