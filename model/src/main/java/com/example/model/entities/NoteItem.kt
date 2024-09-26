package com.example.model.entities

data class NoteItem(
    val id: String = "",
    val noteId: String = "",
    val text: String = "",
    val isChecked: Boolean = false,
    val type: NoteItemType = NoteItemType.TEXT,
    val isFocused: Boolean = false,
    val formatText: FormatText = FormatText()
){
    constructor(id: String, noteId: String): this(
        id = id,
        noteId = noteId,
        type = NoteItemType.TEXT
    )

    fun isText() = type == NoteItemType.TEXT
}

enum class NoteItemType {
    TEXT,
    CHECK_BOX,
}