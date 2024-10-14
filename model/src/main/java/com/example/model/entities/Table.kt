package com.example.model.entities

import com.example.model.utils.getUUID

data class Table(
    val id: String = "",
    val noteItemId: String = "",
    val startCell: Cell = Cell(id = getUUID(), tableId = id, isStartCell = true),
    val endCell: Cell = Cell(id = getUUID(), tableId = id, isStartCell = false),
)