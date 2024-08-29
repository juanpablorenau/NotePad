package com.example.data.di

import com.example.data.source.local.LocalDataSource
import com.example.data.source.local.dao.NoteDao
import com.example.data.source.local.impl.LocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {

    @Provides
    fun providesLocalDataSource(
        noteDao: NoteDao,
        dispatcher: CoroutineDispatcher,
    ): LocalDataSource = LocalDataSourceImpl(noteDao, dispatcher)
}