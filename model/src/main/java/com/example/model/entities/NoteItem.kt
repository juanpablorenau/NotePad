package com.example.model.entities

import com.example.model.enums.NoteItemType
import com.example.model.utils.getUUID

data class NoteItem(
    val id: String = "",
    val noteId: String = "",
    val text: String = "",
    val isChecked: Boolean = false,
    val type: NoteItemType = NoteItemType.TEXT,
    val isFocused: Boolean = false,
    val formatText: FormatText = FormatText(formatTextId = getUUID()),
    val table: Table = Table(),
) {
    constructor(id: String, noteId: String) : this(
        id = id,
        noteId = noteId,
        type = NoteItemType.TEXT,
    )

    constructor(id: String, noteId: String, table: Table) : this(
        id = id,
        noteId = noteId,
        type = NoteItemType.TABLE,
        table = table.copy(noteItemId = id)
    )

    fun isText() = type == NoteItemType.TEXT
    fun isTable() = type == NoteItemType.TABLE
}
