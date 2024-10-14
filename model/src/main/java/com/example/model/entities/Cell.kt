package com.example.model.entities

import com.example.model.utils.getUUID

data class Cell(
    val id: String = "",
    val tableId: String = "",
    val text: String = "",
    val isFocused: Boolean = false,
    val isStartCell: Boolean = false,
    val formatText: FormatText = FormatText(id = getUUID(), formatTextId = id),
)