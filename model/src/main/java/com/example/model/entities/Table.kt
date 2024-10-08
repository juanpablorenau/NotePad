package com.example.model.entities

data class Table(
    val id: String = "",
    val noteItemId: String = "",
    val startCell: Cell = Cell(tableId = id, isStartCell = true),
    val endCell: Cell = Cell(tableId = id, isStartCell = false),
)