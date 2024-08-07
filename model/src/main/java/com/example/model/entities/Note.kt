package com.example.model.entities

import java.util.Date
import java.util.UUID

data class Note(
    val id : String = UUID.randomUUID().toString(),
    val title: String = "",
    val content: String = "",
    val author: Author = Author(),
    val createdDate: Date = Date(),
    val lastModifiedDate: Date = Date(),
    val category: String = "",
    val color: String = "#fff138",
    val isPinned: Boolean = false,
    val isFavorite: Boolean = false,
    val isArchived: Boolean = false,
    val isLocked: Boolean = false,
    val offsetX : Float = 0f,
    val offsetY : Float = 0f,
    val isChecked: Boolean = false
)