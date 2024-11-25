package com.example.data.repository.dto

import com.example.data.model.db.NoteItemDb
import com.example.data.model.db.NoteItemEmbeddedDb
import com.example.model.entities.NoteItem
import com.example.model.enums.NoteItemType
import com.example.model.enums.ParagraphType
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
                cursorStartIndex = noteItem.cursorStartIndex,
                cursorEndIndex = noteItem.cursorEndIndex,
                isChecked = noteItem.isChecked,
                isFocused = noteItem.isFocused,
                type = NoteItemType.valueOf(noteItem.type),
                formatTexts = formatTexts.map { formatTextDto.toDomain(it) },
                paragraphType = ParagraphType.valueOf(noteItem.paragraphType),
                table = tableDto.toDomain(table),
                index = noteItem.index
            )
        }

    fun toDb(noteItem: NoteItem) =
        with(noteItem) {
            NoteItemDb(
                noteItem = NoteItemEmbeddedDb(
                    id = id,
                    noteId = noteId,
                    text = text,
                    cursorStartIndex = cursorStartIndex,
                    cursorEndIndex = cursorEndIndex,
                    isChecked = isChecked,
                    isFocused = isFocused,
                    type = type.name,
                    index = index,
                    paragraphType = paragraphType.name
                ),
                formatTexts = formatTexts.map { formatTextDto.toDb(it) },
                table = tableDto.toDb(table),
            )
        }
}
