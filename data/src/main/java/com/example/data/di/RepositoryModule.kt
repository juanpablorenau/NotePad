package com.example.data.di

import com.example.data.repository.NoteRepository
import com.example.data.repository.PreferencesRepository
import com.example.data.repository.dto.NoteDto
import com.example.data.repository.impl.NoteRepositoryImpl
import com.example.data.repository.impl.PreferencesRepositoryImpl
import com.example.data.source.datastore.DataStoreSource
import com.example.data.source.local.LocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    fun providesNoteRepository(
        localDataSource: LocalDataSource,
        dispatcher: CoroutineDispatcher,
        noteDto: NoteDto,
    ): NoteRepository = NoteRepositoryImpl(localDataSource, dispatcher, noteDto)

    @Provides
    fun providesPreferencesRepository(
        dataStoreSource: DataStoreSource,
        dispatcher: CoroutineDispatcher,
    ): PreferencesRepository = PreferencesRepositoryImpl(dataStoreSource, dispatcher)

}