package com.example.data.repository.dto

import com.example.data.model.db.NoteDbModel
import com.example.model.entities.Note
import javax.inject.Inject

class NoteDto @Inject constructor() {

    fun toDb(note: Note) =
        with(note) {
            NoteDbModel(
                id = id,
                title = title,
                content = content,
                lightColor = lightColor,
                darkColor = darkColor,
                isPinned = isPinned,
                index = index
            )
        }

    fun toDomain(noteDbModel: NoteDbModel) =
        with(noteDbModel) {
            Note(
                id = id,
                title = title,
                content = content,
                lightColor = lightColor,
                darkColor = darkColor,
                isPinned = isPinned,
                index = index
            )
        }
}