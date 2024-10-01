package com.example.data.source.datastore.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.example.data.source.datastore.DataStoreSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DataStoreSourceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val dispatcher: CoroutineDispatcher,
) : DataStoreSource {
    override fun getBoolean(key: String, defaultValue: Boolean) =
        dataStore.data.map { preferences ->
            preferences[booleanPreferencesKey(key)] ?: defaultValue
        }.flowOn(dispatcher)

    override suspend fun putBoolean(key: String, value: Boolean) {
        withContext(dispatcher) {
            dataStore.edit { preferences -> preferences[booleanPreferencesKey(key)] = value }
        }
    }

    override fun getInteger(key: String, defaultValue: Int) =
        dataStore.data.map { preferences -> preferences[intPreferencesKey(key)] ?: defaultValue }
            .flowOn(dispatcher)

    override suspend fun putInteger(key: String, value: Int) {
        withContext(dispatcher) {
            dataStore.edit { preferences -> preferences[intPreferencesKey(key)] = value }
        }
    }
}
