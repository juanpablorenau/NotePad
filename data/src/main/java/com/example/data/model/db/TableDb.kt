package com.example.data.model.db

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation


data class TableDb(
    @Embedded
    val table: TableEmbeddedDb,

    @Relation(
        entity = CellEmbeddedDb::class,
        parentColumn = "id",
        entityColumn = "tableId"
    )
    val cells: List<CellDb>,
)

@Entity(tableName = "Tables")
data class TableEmbeddedDb(
    @PrimaryKey val id: String = "",
    @ColumnInfo(name = "noteItemId") val noteItemId: String,
)