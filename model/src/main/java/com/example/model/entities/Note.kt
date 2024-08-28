package com.example.model.entities

import java.util.UUID

data class Note(
    val id : String = UUID.randomUUID().toString(),
    val title: String = "New Title",
    val content: String = "",
    val lightColor: String = "",
    val darkColor: String = "",
    val isPinned: Boolean = false,
    val isChecked: Boolean = false,
    val index: Int = 0,
    val items: List<NoteItem> = emptyList()
)