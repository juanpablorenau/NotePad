package com.example.model.entities

import com.example.model.utils.getUUID
import com.example.model.utils.normalize

data class Note(
    val id: String = "",
    val title: String = "New Title",
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
        items = listOf(NoteItem(id = getUUID(), noteId = id, isFocused = true))

    )

    fun contains(query: String) = containsInTitle(query) || containsInItems(query)

    private fun containsInTitle(query: String) =
        title.normalize().contains(query.normalize(), ignoreCase = true)

    private fun containsInItems(query: String) =
        items.any { it.text.normalize().contains(query.normalize(), ignoreCase = true) }

    fun addTextField() = this.copy(items = items.toMutableList().apply {
        if (isEmpty()) add(NoteItem(id = getUUID(), noteId = id, isFocused = true))
        else {
            val focusedIndex = indexOfFirst { it.isFocused }

            if (!get(focusedIndex).isText()) {
                val updatedItems = map { it.copy(isFocused = false) }
                clear()
                addAll(updatedItems)
                add(focusedIndex + 1, NoteItem(id = getUUID(), noteId = id, isFocused = true))
            }
        }
    })

    fun addCheckbox(noteItemId: String?) = this.copy(items = this.items.toMutableList().apply {
        if (isEmpty()) {
            add(
                NoteItem(
                    id = getUUID(), noteId = id, type = NoteItemType.CHECK_BOX, isFocused = true
                )
            )
        } else {
            val focusedIndex = indexOfFirst { it.isFocused }
            val updatedItems = map { it.copy(isFocused = false) }

            clear()
            addAll(updatedItems)

            val index = if (noteItemId != null) indexOfFirst { item -> item.id == noteItemId } + 1
            else focusedIndex + 1

            add(
                index, NoteItem(
                    id = getUUID(), noteId = id, type = NoteItemType.CHECK_BOX, isFocused = true
                )
            )
        }
    })


    fun updateTextField(textField: NoteItem) = copy(items = items.map { current ->
        if (current.id == textField.id) textField
        else current.copy(isFocused = false)
    })

    fun updateCheckbox(checkBox: NoteItem) = copy(items = items.map { current ->
        if (current.id == checkBox.id) checkBox
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

    fun copy(newNoteId: String) = Note(id = newNoteId,
        title = title,
        lightNoteColor = lightNoteColor,
        darkNoteColor = darkNoteColor,
        isPinned = isPinned,
        isChecked = isChecked,
        index = index,
        items = items.map {
            NoteItem(
                id = getUUID(),
                noteId = newNoteId,
                text = it.text,
                type = it.type,
                isChecked = it.isChecked,
            )
        })
}
