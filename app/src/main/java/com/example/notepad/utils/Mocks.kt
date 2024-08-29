package com.example.notepad.utils

import com.example.model.entities.Note
import com.example.model.entities.NoteItem
import com.example.model.entities.Color as AppColor

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
        lightColor = AppColor.PALE_YELLOW.lightColor,
        darkColor = AppColor.PALE_YELLOW.darkColor,
        isPinned = false,
    )

val mockNoteItems = listOf(NoteItem(), NoteItem(), NoteItem())

