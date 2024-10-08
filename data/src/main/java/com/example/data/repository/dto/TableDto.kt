package com.example.data.repository.dto

import com.example.data.model.db.TableDb
import com.example.data.model.db.TableEmbeddedDb
import com.example.model.entities.Table
import javax.inject.Inject

class TableDto @Inject constructor(
    private val cellDto: CellDto,
) {
    fun toDomain(tableDb: TableDb) =
        with(tableDb) {
            Table(
                id = table.id,
                noteItemId = table.noteItemId,
                startCell = cellDto.toDomain(cells[0]),
                endCell = cellDto.toDomain(cells[1]),
            )
        }

    fun toDb(table: Table) =
        with(table) {
            TableDb(
                table = TableEmbeddedDb(id = id, noteItemId = noteItemId),
                cells = listOf(cellDto.toDb(startCell), cellDto.toDb(endCell))
            )
        }
}