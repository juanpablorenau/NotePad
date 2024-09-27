package com.example.data.source.local.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.model.db.FormatTextDb
import com.example.data.model.db.NoteEmbeddedDb
import com.example.data.model.db.NoteItemDb

@Database(
    entities = [
        NoteEmbeddedDb::class,
        NoteItemDb::class,
        FormatTextDb::class
    ],
    version = 3,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notePadDao(): NoteDao
}