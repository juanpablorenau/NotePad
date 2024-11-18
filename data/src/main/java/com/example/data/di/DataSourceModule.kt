package com.example.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.data.source.datastore.DataStoreSource
import com.example.data.source.datastore.impl.DataStoreSourceImpl
import com.example.data.source.local.CellDataSource
import com.example.data.source.local.FormatTextDataSource
import com.example.data.source.local.NoteDataSource
import com.example.data.source.local.NoteItemDataSource
import com.example.data.source.local.TableDataSource
import com.example.data.source.local.dao.CellDao
import com.example.data.source.local.dao.FormatTextDao
import com.example.data.source.local.dao.NoteDao
import com.example.data.source.local.dao.NoteItemDao
import com.example.data.source.local.dao.TableDao
import com.example.data.source.local.impl.CellDataSourceImpl
import com.example.data.source.local.impl.FormatTextDataSourceImpl
import com.example.data.source.local.impl.NoteDataSourceImpl
import com.example.data.source.local.impl.NoteItemDataSourceImpl
import com.example.data.source.local.impl.TableDataSourceImpl
import com.example.data.utils.TransactionProvider
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
    fun providesNoteDataSource(
        noteDao: NoteDao,
        noteItemDataSource: NoteItemDataSource,
        transactionProvider: TransactionProvider,
    ): NoteDataSource = NoteDataSourceImpl(noteDao, noteItemDataSource, transactionProvider)

    @Provides
    fun providesNoteItemDataSource(
        noteItemDao: NoteItemDao,
        formatTextDataSource: FormatTextDataSource,
        tableDataSource: TableDataSource,
    ): NoteItemDataSource =
        NoteItemDataSourceImpl(noteItemDao, formatTextDataSource, tableDataSource)

    @Provides
    fun providesTableDataSource(
        tableDao: TableDao,
        cellDataSource: CellDataSource,
    ): TableDataSource = TableDataSourceImpl(tableDao, cellDataSource)

    @Provides
    fun providesCellDataSource(
        cellDao: CellDao,
        formatTextDataSource: FormatTextDataSource
    ): CellDataSource = CellDataSourceImpl(cellDao, formatTextDataSource)

    @Provides
    fun provideFormatTextDataSource(
        formatTextDao: FormatTextDao,
    ): FormatTextDataSource = FormatTextDataSourceImpl(formatTextDao)


    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "appDataStore")

    @Provides
    fun providesDataStoreSource(
        @ApplicationContext context: Context,
        dispatcher: CoroutineDispatcher,
    ): DataStoreSource = DataStoreSourceImpl(context.dataStore, dispatcher)
}