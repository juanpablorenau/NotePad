package com.example.notepad.utils

import com.example.model.entities.Cell
import com.example.model.entities.FormatText
import com.example.model.entities.Note
import com.example.model.entities.NoteItem
import com.example.model.entities.Table
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

val mockNoteItems by lazy { listOf(mockTextItem, mockCheckBoxItem, mockTableItem) }

val mockTextItem by lazy { NoteItem(id = "1", noteId = "1") }

val mockCheckBoxItem by lazy { NoteItem(id = "1", noteId = "1", isChecked = true) }

val mockTableItem by lazy { NoteItem(id = "1", noteId = "1", mockTable) }

val mockTable by lazy {
    Table(
        id = "1",
        noteItemId = "1",
        startCell = mockCell,
        endCell = mockCell
    )
}

val mockCell by lazy {
    Cell(
        id = "1",
        tableId = "1",
        isStartCell = true,
        text = "Cell Text",
        formatText = FormatText()
    )
}