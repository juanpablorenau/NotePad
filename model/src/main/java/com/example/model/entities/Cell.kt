package com.example.model.entities

import com.example.model.utils.getUUID
import com.example.model.utils.normalize

data class Cell(
    val id: String = "",
    val tableId: String = "",
    val text: String = "",
    val isFocused: Boolean = false,
    val isStartCell: Boolean = false,
    val formatText: FormatText,
) {
    constructor(id: String, tableId: String, isStartCell: Boolean) : this(
        id = id,
        tableId = tableId,
        isStartCell = isStartCell,
        isFocused = isStartCell,
        formatText = FormatText(id = getUUID(), formatTextId = id)
    )

    fun containsInCell(query: String) =
        text.normalize().contains(query.normalize(), ignoreCase = true)

    fun duplicate(newTableId: String): Cell {
        val newCellId = getUUID()
        return this.copy(
            id = newCellId,
            tableId = newTableId,
            formatText = formatText.duplicate(newCellId)
        )
    }
}