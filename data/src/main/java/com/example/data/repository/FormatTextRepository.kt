package com.example.data.repository

interface FormatTextRepository {

    suspend fun deleteFormatText(formatTextId: String)
}