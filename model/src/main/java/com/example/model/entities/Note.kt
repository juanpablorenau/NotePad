package com.example.model.entities

import com.example.model.utils.normalize
import java.util.UUID

data class Note(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "New Title",
    val lightColor: String = "",
    val darkColor: String = "",
    val isPinned: Boolean = false,
    val isChecked: Boolean = false,
    val index: Int = 0,
    val items: List<NoteItem> = emptyList(),
) {
    fun contains(query: String) = containsInTitle(query) || containsInItems(query)

    private fun containsInTitle(query: String) =
        title.normalize().contains(query.normalize(), ignoreCase = true)

    private fun containsInItems(query: String) =
        items.any { it.text.normalize().contains(query.normalize(), ignoreCase = true) }
}