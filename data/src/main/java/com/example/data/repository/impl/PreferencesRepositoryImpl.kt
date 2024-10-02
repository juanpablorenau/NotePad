package com.example.data.repository.impl

import com.example.data.model.Preference
import com.example.data.repository.PreferencesRepository
import com.example.data.repository.dto.LanguageDto
import com.example.data.source.datastore.DataStoreSource
import com.example.model.entities.Language
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor(
    private val dataStoreSource: DataStoreSource,
    private val dispatcher: CoroutineDispatcher,
    private val languageDto: LanguageDto,
) :
    PreferencesRepository {
    override fun getIsDarkTheme(): Flow<Boolean> =
        dataStoreSource.getBoolean(Preference.DARK_THEME.key).flowOn(dispatcher)

    override suspend fun setIsDarkTheme(darkTheme: Boolean) {
        withContext(dispatcher) {
            dataStoreSource.putBoolean(Preference.DARK_THEME.key, darkTheme)
        }
    }

    override fun getLanguage(): Flow<Language> =
        dataStoreSource.getString(Preference.LANGUAGE.key).map { language ->
            languageDto.toDomain(language)
        }.flowOn(dispatcher)

    override suspend fun setLanguage(languageKey: String) {
        withContext(dispatcher) {
            dataStoreSource.putString(Preference.LANGUAGE.key, languageKey)
        }
    }
}