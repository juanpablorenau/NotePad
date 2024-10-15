package com.example.data.source.local

import com.example.data.model.db.FormatTextDb
import com.example.model.entities.FormatText

interface FormatTextDataSource {
    suspend fun insertFormatText(formatText: FormatTextDb)
    suspend fun deleteFormatText(formatText: FormatText)
}