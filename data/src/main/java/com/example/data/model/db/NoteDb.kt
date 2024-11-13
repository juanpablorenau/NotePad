package com.example.data.model.db

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

data class NoteDb(
    @Embedded
    val note: NoteEmbeddedDb,

    @Relation(
        entity = NoteItemEmbeddedDb::class,
        parentColumn = "id",
        entityColumn = "noteId"
    )
    val items : List<NoteItemDb>
 )

@Entity(tableName = "Notes")
data class NoteEmbeddedDb(
    @PrimaryKey val id: String,

    @ColumnInfo(name = "title") val title: String = "New Title",
    @ColumnInfo(name = "color") val color: String = "",
    @ColumnInfo(name = "isPinned") val isPinned: Boolean = false,
    @ColumnInfo(name = "index") val index: Int = 0,
)
