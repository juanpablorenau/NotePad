package com.example.data.repository.dto

import com.example.data.model.db.TableDb
import com.example.data.model.db.TableEmbeddedDb
import com.example.model.entities.Table
import javax.inject.Inject

class TableDto @Inject constructor(
    private val cellDto: CellDto,
) {
    fun toDomain(tableDb: TableDb?) =
       tableDb?.let { tableNotNull ->
           with(tableNotNull) {
               Table(
                   id = table.id,
                   noteItemId = table.noteItemId,
                   cells = tableDb.cells.map { cellDto.toDomain(it) }
               )
           }
       }

    fun toDb(table: Table?) =
        if (table == null) null
        else
            with(table) {
                TableDb(
                    table = TableEmbeddedDb(id = id, noteItemId = noteItemId),
                    cells = cells.map { cellDto.toDb(it) }
                )
            }
}