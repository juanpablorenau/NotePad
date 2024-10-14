package com.example.data.source.local

import com.example.data.model.db.FormatTextDb

interface FormatTextDataSource {
    suspend fun insertFormatText(formatText: FormatTextDb)
    suspend fun deleteFormatText(id: String)
}