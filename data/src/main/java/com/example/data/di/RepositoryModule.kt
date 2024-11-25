package com.example.data.di

import com.example.data.repository.FormatTextRepository
import com.example.data.repository.NoteItemRepository
import com.example.data.repository.NoteRepository
import com.example.data.repository.PreferencesRepository
import com.example.data.repository.dto.LanguageDto
import com.example.data.repository.dto.NoteDto
import com.example.data.repository.impl.FormatTextRepositoryImpl
import com.example.data.repository.impl.NoteItemRepositoryImpl
import com.example.data.repository.impl.NoteRepositoryImpl
import com.example.data.repository.impl.PreferencesRepositoryImpl
import com.example.data.source.datastore.DataStoreSource
import com.example.data.source.local.FormatTextDataSource
import com.example.data.source.local.NoteDataSource
import com.example.data.source.local.NoteItemDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    fun providesPreferencesRepository(
        dataStoreSource: DataStoreSource,
        @IoDispatcher dispatcher: CoroutineDispatcher,
        languageDto: LanguageDto,
    ): PreferencesRepository = PreferencesRepositoryImpl(dataStoreSource, dispatcher, languageDto)

    @Provides
    fun providesNoteRepository(
        noteDataSource: NoteDataSource,
        @IoDispatcher dispatcher: CoroutineDispatcher,
        noteDto: NoteDto,
    ): NoteRepository = NoteRepositoryImpl(noteDataSource, dispatcher, noteDto)

    @Provides
    fun providesNoteItemRepository(
        noteItemDataSource: NoteItemDataSource,
        @IoDispatcher dispatcher: CoroutineDispatcher,
    ): NoteItemRepository = NoteItemRepositoryImpl(noteItemDataSource, dispatcher)

    @Provides
    fun providesFormatTextRepository(
        formatTextDataSource: FormatTextDataSource,
        @IoDispatcher dispatcher: CoroutineDispatcher,
    ): FormatTextRepository = FormatTextRepositoryImpl(formatTextDataSource, dispatcher)
}