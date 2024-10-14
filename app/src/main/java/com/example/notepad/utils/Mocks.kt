package com.example.notepad.utils

import com.example.model.entities.Note
import com.example.model.entities.NoteItem
import com.example.model.enums.NoteColor as AppColor

val mockNoteList by lazy { listOf(mockNote.copy(id = "1", title = "Lista de Tareas")) }

val mockNote by lazy {
    Note(
        title = "Meeting Notes",
        lightNoteColor = AppColor.PALE_YELLOW.lightColor,
        darkNoteColor = AppColor.PALE_YELLOW.darkColor,
        isPinned = true,
    )
}

val mockNoteItems by lazy { listOf(mockNoteItem, mockNoteItem, mockNoteItem) }
val mockNoteItem by lazy { NoteItem(id = "1", noteId = "1") }
val mockCheckBox by lazy { NoteItem(id = "1", noteId = "1", isChecked = true) }