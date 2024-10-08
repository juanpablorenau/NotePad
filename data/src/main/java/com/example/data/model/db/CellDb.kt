package com.example.data.model.db

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

data class CellDb(
    @Embedded
    val cell: CellEmbeddedDb,

    @Embedded
    val formatText: FormatTextDb,
)

@Entity(tableName = "Cells")
data class CellEmbeddedDb(
    @PrimaryKey val id: String = "",

    @ColumnInfo(name = "tableId") val tableId: String,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "isFocused") val isFocused: Boolean = false,
    @ColumnInfo(name = "isStartCell") val isStartCell: Boolean = false,
    @ColumnInfo(name = "isEndCell") val isEndCell: Boolean = false,
)
