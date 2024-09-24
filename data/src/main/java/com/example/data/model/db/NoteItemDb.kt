package com.example.data.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "NoteItems")
data class NoteItemDb(
    @PrimaryKey val id: String = "",
    @ColumnInfo(name = "noteId") val noteId: String,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "isChecked") val isChecked: Boolean = false,
    @ColumnInfo(name = "type") val type: String ,
    @ColumnInfo(name = "isFocused") val isFocused: Boolean = false,
)
