package com.example.model.entities

import com.example.model.utils.getUUID
import com.example.model.utils.normalize

data class Note(
    val id: String = "",
    val title: String = "New Title",
    val lightColor: String = Color.PALE_YELLOW.lightColor,
    val darkColor: String = Color.PALE_YELLOW.darkColor,
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
        items.any { it.text.normalize().contains(query.normalize(), ignoreCase = true) }

    fun addTextField() = this.copy(
        items = items.toMutableList().apply {
            if (isEmpty()) add(NoteItem(id = getUUID(), noteId = id))
            else {
                val index = indexOf(items.maxBy { it.lastFocused })
                if (!get(index).isText()) add(index + 1, NoteItem(id = getUUID(), noteId = id))
            }
        }
    )

    fun addCheckbox(noteItemId: String?) =
        this.copy(items = this.items.toMutableList().apply {
            if (isEmpty()) add(NoteItem(id = getUUID(), noteId = id, type = NoteItemType.CHECK_BOX))
            else {
                val index =
                    if (noteItemId != null) indexOfFirst { item -> item.id == noteItemId } + 1
                    else indexOf(items.maxBy { it.lastFocused }) + 1
                add(index, NoteItem(id = getUUID(), noteId = id, type = NoteItemType.CHECK_BOX))
            }
        })

    fun updateTextField(textField: NoteItem) = copy(
        items = items.map { current ->
            if (current.id == textField.id) {
                current.copy(text = textField.text, lastFocused = textField.lastFocused)
            } else current
        })

    fun updateCheckbox(checkBox: NoteItem) = copy(
        items = items.map { current ->
            if (current.id == checkBox.id) {
                current.copy(
                    text = checkBox.text,
                    isChecked = checkBox.isChecked,
                    lastFocused = checkBox.lastFocused
                )
            } else current
        })

    fun deleteTextField(noteItemId: String) = copy(
        items = items.filter { item -> item.id != noteItemId }
    )

    fun deleteCheckbox(noteItemId: String) = copy(
        items = items.toMutableList().apply {
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

    fun copy(newNoteId: String) =
        Note(
            id = newNoteId,
            title = title,
            lightColor = lightColor,
            darkColor = darkColor,
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
            }
        )
}
