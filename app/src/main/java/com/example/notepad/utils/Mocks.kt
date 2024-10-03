package com.example.notepad.utils

import com.example.model.entities.Cell
import com.example.model.entities.Note
import com.example.model.entities.NoteItem
import com.example.model.entities.NoteColor as AppColor

val mockNoteList by lazy {
    listOf(
        mockNote.copy(
            id = "1",
            title = "Lista de Tareas",
        )
    )
}

val mockNote =
    Note(
        title = "Meeting Notes",
        lightNoteColor = AppColor.PALE_YELLOW.lightColor,
        darkNoteColor = AppColor.PALE_YELLOW.darkColor,
        isPinned = true,
    )

val mockNoteItems =
    listOf(NoteItem(text = "Item 1"), NoteItem(text = "Item 2"), NoteItem(text = "Item 3"))

val mockNoteItem = NoteItem(text = "Item 1")

val mockCell = Cell()