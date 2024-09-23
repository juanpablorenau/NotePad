package com.example.data.source.local.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.model.db.NoteEmbeddedDb
import com.example.data.model.db.NoteItemDb

@Database(
    entities = [
        NoteEmbeddedDb::class,
        NoteItemDb::class
    ],
    version = 2,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notePadDao(): NoteDao
}