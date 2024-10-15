package com.example.model.entities

import com.example.model.enums.NoteItemType
import com.example.model.utils.getUUID
import com.example.model.utils.normalize

data class NoteItem(
    val id: String = "",
    val noteId: String = "",
    val text: String = "",
    val isChecked: Boolean = false,
    val type: NoteItemType = NoteItemType.TEXT,
    val isFocused: Boolean = true,
    val formatText: FormatText,
    val table: Table? = null,
) {
    constructor(id: String, noteId: String) : this(
        id = id,
        noteId = noteId,
        type = NoteItemType.TEXT,
        formatText = FormatText(id = getUUID(), formatTextId = id)
    )

    constructor(id: String, noteId: String, isChecked: Boolean) : this(
        id = id,
        noteId = noteId,
        type = NoteItemType.CHECK_BOX,
        formatText = FormatText(id = getUUID(), formatTextId = id),
        isChecked = isChecked,
    )

    constructor(id: String, noteId: String, table: Table) : this(
        id = id,
        noteId = noteId,
        type = NoteItemType.TABLE,
        formatText = FormatText(id = getUUID(), formatTextId = id),
        table = table.copy(noteItemId = id)
    )

    fun isText() = type == NoteItemType.TEXT
    fun isTable() = type == NoteItemType.TABLE

    fun containsInItem(query: String) =
        if (isTable()) containsInTable(query)
        else text.normalize().contains(query.normalize(), ignoreCase = true)

    private fun containsInTable(query: String) =
        table?.let { tab ->
            tab.startCell.containsInCell(query) || tab.endCell.containsInCell(query)
        } ?: false

    fun duplicate(newNoteId: String): NoteItem {
        val newNoteItemId = getUUID()
        return NoteItem(
            id = newNoteItemId,
            noteId = newNoteId,
            text = text,
            isChecked = isChecked,
            type = type,
            isFocused = isFocused,
            formatText = formatText.duplicate(newNoteItemId),
            table = table?.duplicate(newNoteItemId)
        )
    }
}
