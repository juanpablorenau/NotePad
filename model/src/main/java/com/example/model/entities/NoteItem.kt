package com.example.model.entities

import com.example.model.utils.getUUID

data class NoteItem(
    val id: String = "",
    val noteId: String = "",
    val text: String = "",
    val isChecked: Boolean = false,
    val type: NoteItemType = NoteItemType.TEXT,
    val isFocused: Boolean = false,
    val formatText: FormatText = FormatText(formatTextId = getUUID()),
    val table: Pair<Cell, Cell> = Pair(Cell(), Cell())
) {
    constructor(id: String, noteId: String) : this(
        id = id,
        noteId = noteId,
        type = NoteItemType.TEXT,
    )

    constructor(id: String, noteId: String, table: Pair<Cell, Cell>) : this(
        id = id,
        noteId = noteId,
        type = NoteItemType.TABLE,
        table = table,
    )

    fun isText() = type == NoteItemType.TEXT
    fun isTable() = type == NoteItemType.TABLE
}

data class Cell(
    val id: String = "",
    val text: String = "",
    val isFocused: Boolean = false,
    val formatText: FormatText = FormatText(formatTextId = getUUID()),
) {
    constructor() : this(
        text = ""
    )
}

enum class NoteItemType {
    TEXT,
    CHECK_BOX,
    TABLE,
}
