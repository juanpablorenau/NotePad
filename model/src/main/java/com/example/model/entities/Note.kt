package com.example.model.entities

import com.example.model.enums.NoteColor
import com.example.model.utils.getUUID
import com.example.model.utils.normalize

data class Note(
    val id: String = "",
    val title: String = "",
    val lightNoteColor: String = NoteColor.PALE_YELLOW.lightColor,
    val darkNoteColor: String = NoteColor.PALE_YELLOW.darkColor,
    val isPinned: Boolean = false,
    val isChecked: Boolean = false,
    val index: Int = 0,
    val items: List<NoteItem> = emptyList(),
) {
    constructor(id: String, index: Int) : this(
        id = id,
        index = index,
        items = listOf(NoteItem(id = getUUID(), noteId = id))
    )

    fun contains(query: String) = containsInTitle(query) || containsInItems(query)

    private fun containsInTitle(query: String) =
        title.normalize().contains(query.normalize(), ignoreCase = true)

    private fun containsInItems(query: String) =
        items.any { noteItem -> noteItem.containsInItem(query) }

    fun addTextField() = this.copy(items = items.toMutableList().apply {
        if (isEmpty()) add(NoteItem(id = getUUID(), noteId = id))
        else {
            val focusedIndex = indexOfFirst { it.isFocused }

            if (getOrNull(focusedIndex)?.isText() != true) {
                val updatedItems = map { it.copy(isFocused = false) }
                clear()
                addAll(updatedItems)
                add(focusedIndex + 1, NoteItem(id = getUUID(), noteId = id))
            }
        }
    })

    fun addCheckbox(noteItemId: String?) = this.copy(items = this.items.toMutableList().apply {
        if (isEmpty()) add(NoteItem(id = getUUID(), noteId = id, isChecked = false))
        else {
            val focusedIndex = indexOfFirst { it.isFocused }
            val updatedItems = map { it.copy(isFocused = false) }

            clear()
            addAll(updatedItems)

            val index = if (noteItemId != null) indexOfFirst { item -> item.id == noteItemId } + 1
            else focusedIndex + 1

            add(index, NoteItem(id = getUUID(), noteId = id, isChecked = false))
        }
    })

    fun addTable() = this.copy(items = this.items.toMutableList().apply {
        if (isEmpty()) add(NoteItem(getUUID(), id, Table(id = getUUID())))
        else {
            val focusedIndex = indexOfFirst { it.isFocused }
            val updatedItems = map { it.copy(isFocused = false) }

            clear()
            addAll(updatedItems)

            add(focusedIndex + 1, NoteItem(getUUID(), id, Table(id = getUUID())))
        }
    })

    fun updateNoteItem(noteItem: NoteItem) = copy(items = items.map { current ->
        if (current.id == noteItem.id) noteItem
        else current.copy(isFocused = false)
    })

    fun deleteTextField(noteItemId: String) =
        copy(items = items.filter { item -> item.id != noteItemId })

    fun deleteCheckbox(noteItemId: String) = copy(items = items.toMutableList().apply {
        val index = indexOfFirst { noteItem -> noteItem.id == noteItemId }
        when (index) {
            -1 -> return@apply
            size - 1 -> removeLast()
            else -> {
                val prev = getOrNull(index - 1)
                val next = getOrNull(index + 1)
                removeAt(index)
                if (prev?.isText() == true && next?.isText() == true) {
                    removeAt(index)
                    removeAt(index - 1)
                    add(index - 1, prev.copy(text = "${prev.text}\n${next.text}"))
                }
            }
        }
    })

    fun applyFormat(formatText: FormatText) = copy(items = items.map { currentNoteItem ->
        if (currentNoteItem.isFocused) currentNoteItem.copy(formatText = formatText)
        else currentNoteItem
    })

    fun duplicate() : Note {
        val newNoteId = getUUID()
        return Note(
            id = newNoteId,
            title = title,
            lightNoteColor = lightNoteColor,
            darkNoteColor = darkNoteColor,
            isPinned = isPinned,
            isChecked = isChecked,
            index = index,
            items = items.map { noteItem -> noteItem.duplicate(newNoteId) }
        )
    }
}
