package com.example.data.repository.dto

import com.example.data.model.db.CellDb
import com.example.data.model.db.CellEmbeddedDb
import com.example.model.entities.Cell
import javax.inject.Inject

class CellDto @Inject constructor(
    private val formatTextDto: FormatTextDto,
) {
    fun toDomain(cellDb: CellDb) =
        with(cellDb) {
            Cell(
                id = cell.id,
                tableId = cell.tableId,
                text = cell.text,
                isFocused = cell.isFocused,
                index = cell.index,
                formatTexts = formatTexts.map { formatTextDto.toDomain(it) }
            )
        }

    fun toDb(cell: Cell) =
        with(cell) {
            CellDb(
                cell = CellEmbeddedDb(
                    id = id,
                    tableId = tableId,
                    text = text,
                    isFocused = isFocused,
                    index = index
                ),
                formatTexts = formatTexts.map { formatTextDto.toDb(it) }
            )
        }
}