package com.example.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.data.source.datastore.DataStoreSource
import com.example.data.source.datastore.impl.DataStoreSourceImpl
import com.example.data.source.local.LocalDataSource
import com.example.data.source.local.dao.NoteDao
import com.example.data.source.local.impl.LocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "appDataStore")

    @Provides
    fun providesDataStoreSource(
        @ApplicationContext context: Context,
        dispatcher: CoroutineDispatcher,
    ): DataStoreSource = DataStoreSourceImpl(context.dataStore, dispatcher)
}