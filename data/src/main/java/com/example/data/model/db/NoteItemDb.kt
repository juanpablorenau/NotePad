package com.example.data.model.db

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

data class NoteItemDb(
    @Embedded
    val noteItem: NoteItemEmbeddedDb,

    @Relation(
        entity = FormatTextDb::class,
        parentColumn = "id",
        entityColumn = "itemId"
    )
    val formatTexts: List<FormatTextDb>,

    @Relation(
        entity = TableEmbeddedDb::class,
        parentColumn = "id",
        entityColumn = "noteItemId"
    )
    val table: TableDb?,
)

@Entity(tableName = "NoteItems")
data class NoteItemEmbeddedDb(
    @PrimaryKey val id: String = "",

    @ColumnInfo(name = "noteId") val noteId: String,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "cursorStartIndex") val cursorStartIndex: Int,
    @ColumnInfo(name = "cursorEndIndex") val cursorEndIndex: Int,
    @ColumnInfo(name = "isChecked") val isChecked: Boolean = false,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "paragraphType") val paragraphType: String = "",
    @ColumnInfo(name = "isFocused") val isFocused: Boolean = false,
    @ColumnInfo(name = "index") val index: Int = 0,
)
