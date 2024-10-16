package com.example.model.entities

import com.example.model.utils.getUUID

data class Table(
    val id: String = "",
    val noteItemId: String = "",
    val startCell: Cell = Cell(id = getUUID(), tableId = id, isStartCell = true),
    val endCell: Cell = Cell(id = getUUID(), tableId = id, isStartCell = false),
) {

    fun duplicate(newNoteItemId: String): Table {
        val newTableId = getUUID()
        return this.copy(
            id = newTableId,
            noteItemId = newNoteItemId,
            startCell = startCell.duplicate(newTableId),
            endCell = endCell.duplicate(newTableId)
        )
    }

    fun applyInTable(cell: Cell) =
        if (cell.isStartCell) {
            copy(
                startCell = cell.copy(isFocused = true),
                endCell = endCell.copy(isFocused = false)
            )
        } else {
            copy(
                startCell = startCell.copy(isFocused = false),
                endCell = cell.copy(isFocused = true)
            )
        }
}