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
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun noteItemDao(): NoteItemDao
    abstract fun formatTextDao(): FormatTextDao
    abstract fun tableDao(): TableDao
    abstract fun cellDao(): CellDao
}