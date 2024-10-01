package com.example.data.repository

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    fun getIsDarkTheme(): Flow<Boolean>
    suspend fun setIsDarkTheme(darkTheme: Boolean)
}