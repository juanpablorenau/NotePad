package com.example.model.entities

import java.util.Date
import java.util.UUID

data class Note(
    val title: String = "",
    val content: String = "",
    val author: Author = Author(),
    val createdDate: Date = Date(),
    val lastModifiedDate: Date = Date(),
    val tags: List<String> = emptyList(),
    val color: String = "#fff138",
    val isPinned: Boolean = false,
    val isFavorite: Boolean = false,
    val isArchived: Boolean = false,
    val isLocked: Boolean = false,
    val heightFactor: Float = 0f,
    val offsetX : Float = 0f,
    val offsetY : Float = 0f
) {
    val id : String = UUID.randomUUID().toString()
}