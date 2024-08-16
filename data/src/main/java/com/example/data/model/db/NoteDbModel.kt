package com.example.data.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Notes")
data class NoteDbModel(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "title") val title: String = "New Title",
    @ColumnInfo(name = "content") val content: String = "",
    @ColumnInfo(name = "color") val color: String = "#FDFD96",
    @ColumnInfo(name = "isPinned") val isPinned: Boolean = false,
    @ColumnInfo(name = "index") val index: Int = 0
 )
