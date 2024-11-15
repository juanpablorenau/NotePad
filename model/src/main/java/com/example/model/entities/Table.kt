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

    fun initFocus() = copy(cells = cells.mapIndexed { index, cell ->
        if (index == 0) cell.initFocus()
        else cell.removeFocus()
    })

    fun removeFocus() = copy(cells = cells.map { it.removeFocus() })

    fun restoreFocus() = copy(cells = cells.mapIndexed { index, cell ->
        if (index == cells.lastIndex) cell.initFocus()
        else cell.removeFocus()
    })

    fun changeFocus(cellId: String) =
        copy(cells = cells.map { current ->
            current.copy(isFocused = current.id == cellId)
        })

    fun isEmpty() = cells.all { it.text.isEmpty() }
}