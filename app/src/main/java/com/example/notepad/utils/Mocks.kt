package com.example.notepad.utils

import androidx.compose.ui.focus.FocusRequester
import com.example.model.entities.*
import com.example.model.enums.NoteColor as AppColor

val mockNoteList by lazy {
    listOf(
        mockNote.copy(id = "1"),
        mockNote.copy(id = "2"),
        mockNote.copy(id = "3")
    )
}

val mockNote by lazy {
    Note(
        title = "Meeting Notes",
        lightNoteColor = AppColor.PALE_YELLOW.lightColor,
        darkNoteColor = AppColor.PALE_YELLOW.darkColor,
        isPinned = true,
        items = mockNoteItems
    )
}

val mockNoteItems by lazy {
    listOf(
        mockCheckBoxItem,
        mockTextItem,
        mockTableItem
    )
}

val mockTextItem by lazy { NoteItem(id = "1", noteId = "1", 0).copy(text = "Text Item") }

val mockCheckBoxItem by lazy {
    NoteItem(
        id = "1",
        noteId = "1",
        isChecked = true,
        index = 0
    ).copy(text = "Checkbox Item")
}

val mockTableItem by lazy { NoteItem(id = "1", noteId = "1", mockTable, 0) }

val mockTable by lazy {
    Table(
        id = "1",
        noteItemId = "1",
        cells =
        listOf(
            mockCell,
            mockCell,
            mockCell.copy(text = "Very large text in the cell")
        )
    )
}

val mockCell by lazy {
    Cell(
        id = "1",
        tableId = "1",
        index = 0,
        text = "Cell Text",
        formatText = FormatText()
    )
}

val mockFocusRequesters by lazy { mockTable.cells.map { FocusRequester() } }