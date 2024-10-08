package com.example.data.source.local.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.model.db.CellEmbeddedDb
import com.example.data.model.db.FormatTextDb
import com.example.data.model.db.NoteEmbeddedDb
import com.example.data.model.db.NoteItemEmbeddedDb
import com.example.data.model.db.TableEmbeddedDb

@Database(
    entities = [
        NoteEmbeddedDb::class,
        NoteItemEmbeddedDb::class,
        TableEmbeddedDb::class,
        CellEmbeddedDb::class,
        FormatTextDb::class,
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notePadDao(): NoteDao
}