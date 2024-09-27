package com.example.data.repository.dto

import com.example.data.model.db.NoteItemDb
import com.example.model.entities.NoteItem
import com.example.model.entities.NoteItemType
import javax.inject.Inject

class NoteItemDto @Inject constructor(
    private val formatTextDto: FormatTextDto,
    private val noteItemTypeDto: NoteItemTypeDto,
) {

    fun typeToDomain(noteItemDb: NoteItemDb) =
        NoteItem(
            id = noteItemDb.id,
            noteId = noteItemDb.noteId,
            text = noteItemDb.text,
            isChecked = noteItemDb.isChecked,
            isFocused = noteItemDb.isFocused,
            type = noteItemTypeDto.toDomain(noteItemDb.type),
            formatText = formatTextDto.toDomain(noteItemDb.formatText)
        )

    fun toDb(noteItem: NoteItem) =
        NoteItemDb(
            id = noteItem.id,
            noteId = noteItem.noteId,
            text = noteItem.text,
            isChecked = noteItem.isChecked,
            isFocused = noteItem.isFocused,
            type = noteItem.type.name,
            formatText = formatTextDto.toDb(noteItem.formatText)
        )
}

class NoteItemTypeDto @Inject constructor() {
    fun toDomain(type: String): NoteItemType =
        when (type) {
            NoteItemType.TEXT.name -> NoteItemType.TEXT
            else -> NoteItemType.CHECK_BOX
        }
}

