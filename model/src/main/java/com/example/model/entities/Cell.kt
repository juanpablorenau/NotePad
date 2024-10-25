package com.example.model.entities

import com.example.model.utils.getUUID
import com.example.model.utils.normalize

data class Cell(
    val id: String = "",
    val tableId: String = "",
    val text: String = "",
    val isFocused: Boolean = false,
    val index: Int = 0,
    val formatText: FormatText,
) {
    constructor(id: String, tableId: String, index: Int, isFocused: Boolean) : this(
        id = id,
        tableId = tableId,
        index = index,
        isFocused = isFocused,
        formatText = FormatText(id = getUUID(), formatTextId = id)
    )

    fun contains(query: String) =
        text.normalize().contains(query.normalize(), ignoreCase = true)

    fun duplicate(newTableId: String): Cell {
        val newCellId = getUUID()
        return this.copy(
            id = newCellId,
            tableId = newTableId,
            formatText = formatText.duplicate(newCellId)
        )
    }

    fun initFocus() = copy(isFocused = true)

    fun removeFocus() = copy(isFocused = false)
}