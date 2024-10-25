package com.example.data.model.db

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

data class CellDb(
    @Embedded
    val cell: CellEmbeddedDb,

    @Relation(
        entity = FormatTextDb::class,
        parentColumn = "id",
        entityColumn = "formatTextId"
    )
    val formatText: FormatTextDb,
)

@Entity(tableName = "Cells")
data class CellEmbeddedDb(
    @PrimaryKey val id: String = "",

    @ColumnInfo(name = "tableId") val tableId: String,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "isFocused") val isFocused: Boolean = false,
    @ColumnInfo(name = "index") val index: Int
)
