package com.example.data.source.local.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.model.db.NoteDbModel


@Database(
    entities = [
        NoteDbModel::class,
    ],
    version = 1,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notePadDao(): NotePadDao
}