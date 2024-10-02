package com.example.data.source.datastore

import kotlinx.coroutines.flow.Flow

interface DataStoreSource {
    fun getBoolean(key: String, defaultValue: Boolean = false): Flow<Boolean>
    suspend fun putBoolean(key: String, value: Boolean)
    fun getInteger(key: String, defaultValue: Int = 0): Flow<Int>
    suspend fun putInteger(key: String, value: Int)
    fun getString(key: String, defaultValue: String = ""): Flow<String>
    suspend fun putString(key: String, value: String)
}