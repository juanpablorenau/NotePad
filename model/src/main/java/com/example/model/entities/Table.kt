package com.example.model.entities

import com.example.model.utils.getUUID

data class Table(
    val id: String = "",
    val noteItemId: String = "",
    val cells: List<Cell> = emptyList(),
) {
    constructor(id: String, noteItemId: String) : this(
        id = id,
        noteItemId = noteItemId,
        cells = listOf(
            Cell(id = getUUID(), tableId = id, index = 0, isFocused = true),
            Cell(id = getUUID(), tableId = id, index = 1, isFocused = false),
        )
    )

    fun duplicate(newNoteItemId: String): Table {
        val newTableId = getUUID()
        return this.copy(
            id = newTableId,
            noteItemId = newNoteItemId,
            cells = cells.map { it.duplicate(newTableId) }
        )
    }

    fun contains(query: String) = cells.any { it.contains(query) }

    fun applyInTable(cell: Cell) =
        copy(cells = cells.map { currentCell ->
            if (currentCell.id == cell.id) cell
            else currentCell
        })

    fun initFocus() = copy(cells = cells.map { it.initFocus() })

    fun removeFocus() = copy(cells = cells.map { it.removeFocus() })

    fun changeFocus(cellId: String) = copy(cells = cells.map { current ->
        if (current.id == cellId) current.initFocus()
        else current.removeFocus()
    })

    fun cellsCount() = cells.size

    fun isEmpty() = cells.all { it.text.isEmpty() }
}