package com.example.data.di

import android.content.Context
import androidx.room.Room
import com.example.data.source.local.dao.AppDatabase
import com.example.data.source.local.dao.NotePadDao
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
        ).build()
    }

    @Singleton
    @Provides
    fun provideNotePadDao(db: AppDatabase): NotePadDao = db.notePadDao()
}