package com.example.data.repository

import com.example.model.enums.Language
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    fun getIsDarkTheme(): Flow<Boolean>
    suspend fun setIsDarkTheme(darkTheme: Boolean)
    fun getLanguage(): Flow<Language>
    suspend fun setLanguage(languageKey: String)
    fun getColumnsCount(): Flow<Int>
    suspend fun setColumnsCount(count: Int)
}