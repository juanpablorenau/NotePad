package com.example.data.repository.dto

import com.example.data.model.db.NoteItemDb
import com.example.model.entities.NoteItem
import com.example.model.entities.NoteItemType
import javax.inject.Inject

class NoteItemDto @Inject constructor() {

    fun toDomain(noteItemDb: NoteItemDb) =
        NoteItem(
            id = noteItemDb.id,
            noteId = noteItemDb.noteId,
            text = noteItemDb.text,
            isChecked = noteItemDb.isChecked,
            type = when (noteItemDb.type) {
                NoteItemType.TEXT.name -> NoteItemType.TEXT
                else -> NoteItemType.CHECK_BOX
            }
        )

    fun toDb(noteItem: NoteItem) =
        NoteItemDb(
            id = noteItem.id,
            noteId = noteItem.noteId,
            text = noteItem.text,
            isChecked = noteItem.isChecked,
            type = noteItem.type.name
        )
}