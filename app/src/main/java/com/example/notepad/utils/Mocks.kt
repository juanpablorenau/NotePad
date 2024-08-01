package com.example.notepad.utils

import com.example.model.entities.Author
import com.example.model.entities.Note
import java.util.Date

val mockNoteList by lazy {
    listOf(
        mockNote,
        mockNote.copy(heightFactor = 2f),
        mockNote,
        mockNote.copy(heightFactor = 1f),
        mockNote,
        mockNote.copy(heightFactor = 1.75f),
    )
}

val mockNote by lazy {
    Note(
        title = "Meeting Notes",
        content = "Here are the notes from the meeting.",
        author = Author(),
        createdDate = Date(),
        lastModifiedDate = Date(),
        tags = listOf("meeting", "work", "notes"),
        color = "#fff138",
        isPinned = true,
        isFavorite = true,
        isArchived = true,
        isLocked = true,
        heightFactor = 1f
    )
}