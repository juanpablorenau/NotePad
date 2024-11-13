package com.example.data.repository.dto

import com.example.data.model.db.NoteDb
import com.example.data.model.db.NoteEmbeddedDb
import com.example.model.entities.Note
import com.example.model.enums.NoteColor
import javax.inject.Inject

class NoteDto @Inject constructor(
    private val noteItemDto: NoteItemDto,
) {
    fun toDomain(noteDb: NoteDb) =
        with(noteDb) {
            Note(
                id = note.id,
                title = note.title,
                color = NoteColor.valueOf(note.color),
                isPinned = note.isPinned,
                index = note.index,
                items = items.map { noteItemDto.toDomain(it) }
            )
        }

    fun toDb(note: Note) =
        with(note) {
            NoteDb(
                note = NoteEmbeddedDb(
                    id = id,
                    title = title,
                    color = color.name,
                    isPinned = isPinned,
                    index = index
                ),
                items = items.map { noteItemDto.toDb(it) }
            )
        }
}