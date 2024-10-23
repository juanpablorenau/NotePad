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
        items = listOf(NoteItem(id = getUUID(), noteId = id, index = 0))
    )

    fun contains(query: String) = containsInTitle(query) || containsInItems(query)

    private fun containsInTitle(query: String) =
        title.normalize().contains(query.normalize(), ignoreCase = true)

    private fun containsInItems(query: String) =
        items.any { noteItem -> noteItem.containsInItem(query) }

    fun addTextField() = copy(items = items.toMutableList().apply {
        if (isEmpty()) add(NoteItem(id = getUUID(), noteId = id, index = 0))
        else {
            val focusedIndex = indexOfFirst { it.isFocused }

            if (getOrNull(focusedIndex)?.isText() != true) {
                val updatedItems = map { noteItem ->
                    val newIndex =
                        if (noteItem.index > focusedIndex) noteItem.index + 1
                        else noteItem.index
                    noteItem.copy(isFocused = false, index = newIndex)
                }

                clear()
                addAll(updatedItems)

                val newItemIndex = focusedIndex + 1
                add(newItemIndex, NoteItem(id = getUUID(), noteId = id, newItemIndex))
            }
        }
    })

    fun addCheckbox(noteItemId: String?) = copy(items = this.items.toMutableList().apply {
        if (isEmpty()) add(NoteItem(id = getUUID(), noteId = id, isChecked = false, index = 0))
        else {
            val focusedIndex = indexOfFirst { it.isFocused }
            val updatedItems = map { noteItem ->
                val newIndex =
                    if (noteItem.index > focusedIndex) noteItem.index + 1
                    else noteItem.index
                noteItem.copy(isFocused = false, index = newIndex)
            }

            clear()
            addAll(updatedItems)

            val newItemIndex =
                if (noteItemId != null) indexOfFirst { item -> item.id == noteItemId } + 1
                else focusedIndex + 1

            add(
                newItemIndex,
                NoteItem(id = getUUID(), noteId = id, isChecked = false, newItemIndex)
            )
        }
    })

    fun addTable() = copy(items = items.toMutableList().apply {
        if (isEmpty()) add(NoteItem(getUUID(), id, Table(id = getUUID()), index = 0))
        else {
            val focusedIndex = indexOfFirst { it.isFocused }
            val updatedItems = map { noteItem ->
                val newIndex =
                    if (noteItem.index > focusedIndex) noteItem.index + 1
                    else noteItem.index
                noteItem.copy(isFocused = false, index = newIndex)
            }

            clear()
            addAll(updatedItems)

            val newItemIndex = focusedIndex + 1
            val noteItemId = getUUID()
            add(
                newItemIndex,
                NoteItem(noteItemId, id, Table(id = getUUID(), noteItemId), newItemIndex)
            )
        }
    })

    fun updateNoteItem(noteItem: NoteItem) = copy(items = items.map { current ->
        if (current.id == noteItem.id) noteItem
        else current
    })

    fun deleteTextField(noteItemId: String) = copy(items = items
        .filter { item -> item.id != noteItemId }
        .mapIndexed { index, noteItem -> noteItem.copy(index = index) }
    )

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

                val updatedItems = mapIndexed { newIndex, noteItem ->
                    noteItem.copy(index = newIndex)
                }

                clear()
                addAll(updatedItems)
            }
        }
    })

    fun applyFormat(formatText: FormatText) = copy(items = items.map { currentNoteItem ->
        if (currentNoteItem.isFocused) currentNoteItem.copy(formatText = formatText)
        else currentNoteItem
    })

    fun duplicate(): Note {
        val newNoteId = getUUID()
        return this.copy(
            id = newNoteId,
            items = items.map { noteItem -> noteItem.duplicate(newNoteId) }
        )
    }

    fun setFocusOnLastItem() = copy(items = items
        .mapIndexed { index, noteItem ->
            if (index == items.lastIndex) noteItem.initFocus()
            else noteItem.removeFocus()
        }
    )

    fun changeFocusIn(noteItem: NoteItem) =
        copy(items = items.map { current ->
            if (current.id == noteItem.id) noteItem.copy(isFocused = true)
            else current.removeFocus()
        })
}
