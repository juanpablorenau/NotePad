package com.example.data.repository.dto

import com.example.data.model.db.NoteItemDb
import com.example.data.model.db.NoteItemEmbeddedDb
import com.example.model.entities.NoteItem
import com.example.model.enums.NoteItemType
import javax.inject.Inject

class NoteItemDto @Inject constructor(
    private val formatTextDto: FormatTextDto,
    private val tableDto: TableDto,
) {
    fun toDomain(noteItemDb: NoteItemDb) =
        with(noteItemDb) {
            NoteItem(
                id = noteItem.id,
                noteId = noteItem.noteId,
                text = noteItem.text,
                isChecked = noteItem.isChecked,
                isFocused = noteItem.isFocused,
                type = NoteItemType.valueOf(noteItem.type),
                formatText = formatTextDto.toDomain(formatText),
                table = tableDto.toDomain(table)
            )
        }

    fun toDb(noteItem: NoteItem) =
        with(noteItem) {
            NoteItemDb(
                noteItem = NoteItemEmbeddedDb(
                    id = id,
                    noteId = noteId,
                    text = text,
                    isChecked = isChecked,
                    isFocused = isFocused,
                    type = type.name,
                ),
                formatText = formatTextDto.toDb(formatText),
                table = tableDto.toDb(table)
            )
        }
}
