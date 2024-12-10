package com.example.data.di

import android.content.Context
import androidx.room.Room
import com.example.data.source.local.dao.AppDatabase
import com.example.data.source.local.dao.CellDao
import com.example.data.source.local.dao.FormatTextDao
import com.example.data.source.local.dao.NoteDao
import com.example.data.source.local.dao.NoteItemDao
import com.example.data.source.local.dao.TableDao
import com.example.data.utils.Constants.DATABASE_NAME
import com.example.data.utils.TransactionProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDataBase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideTransactionProvider(appDatabase: AppDatabase): TransactionProvider =
        TransactionProvider(appDatabase)

    @Singleton
    @Provides
    fun provideNoteDao(db: AppDatabase): NoteDao = db.noteDao()

    @Singleton
    @Provides
    fun provideNoteItemDao(db: AppDatabase): NoteItemDao = db.noteItemDao()

    @Singleton
    @Provides
    fun provideFormatTextDao(db: AppDatabase): FormatTextDao = db.formatTextDao()

    @Singleton
    @Provides
    fun provideTableDao(db: AppDatabase): TableDao = db.tableDao()

    @Singleton
    @Provides
    fun provideCellDao(db: AppDatabase): CellDao = db.cellDao()
}
