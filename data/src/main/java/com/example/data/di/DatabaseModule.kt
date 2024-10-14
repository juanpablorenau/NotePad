package com.example.data.di

import android.content.Context
import androidx.room.Room
import com.example.data.source.local.dao.*
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
            "NotePad.db"
        ).fallbackToDestructiveMigration().build()
    }

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
