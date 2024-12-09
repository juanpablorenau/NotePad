package com.example.model.entities

import com.example.model.enums.FormatType
import com.example.model.enums.NoteColor
import com.example.model.enums.ParagraphType
import com.example.model.utils.getUUID
import com.example.model.utils.normalize

data class Note(
    val id: String = "",
    val title: String = "",
    val color: NoteColor = NoteColor.PALE_YELLOW,
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

    fun getColor(isDarkTheme: Boolean) = if (isDarkTheme) color.darkColor else color.lightColor

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

    fun updateNoteItem(noteItem: NoteItem, deleteFormat: (String) -> Unit) =
        copy(items = items.map { current ->
            if (current.id == noteItem.id) {
                when {
                    current.text.length > noteItem.text.length -> {
                        noteItem.updateFormatsAfterDeletingCharacter(
                            getIndexOfCharacter(current.text, noteItem.text)
                        ) { id -> deleteFormat(id) }
                    }

                    current.text.length < noteItem.text.length -> {
                        noteItem.updateFormatsAfterAddingCharacter(
                            getIndexOfCharacter(current.text, noteItem.text)
                        )
                    }

                    else -> noteItem
                }
            } else current
        })

    private fun getIndexOfCharacter(oldText: String, newText: String): Int {
        val minLength = minOf(oldText.length, newText.length)

        for (i in 0 until minLength) {
            if (oldText[i] != newText[i]) return i
        }

        return minLength
    }

    fun deleteTextField(noteItemId: String) =
        copy(items = items.mapIndexedNotNull { index, noteItem ->
            val targetIndex = items.indexOfFirst { it.id == noteItemId }

            when {
                index == targetIndex -> null
                index == targetIndex - 1 -> noteItem.restoreFocus().copy(index = index)
                index > targetIndex -> noteItem.copy(index = index - 1)
                else -> noteItem.copy(index = index)
            }
        })

    fun deleteNoteItemField(noteItemId: String) = copy(items = items.toMutableList().apply {
        val index = items.indexOfFirst { it.id == noteItemId }
        if (index == -1) return@apply

        //Delete the text field
        val previous = getOrNull(index - 1)
        val next = getOrNull(index + 1)
        removeAt(index)

        //Move the focus to the previous text field
        previous?.let { prev ->
            removeAt(index - 1)

            //Merge the text fields if they are next to each other
            if (prev.isText() && next?.isText() == true) {
                removeAt(index - 1)
                val mergeText = "${prev.text}\n${next.text}"
                add(index - 1, prev.copy(text = mergeText).restoreFocus())
            } else add(index - 1, prev.restoreFocus())
        }

        //Update the indexes
        val updatedItems = mapIndexed { newIndex, noteItem -> noteItem.copy(index = newIndex) }
        clear()
        addAll(updatedItems)
    })

    fun applyParagraph(
        paragraphType: ParagraphType
    ) = copy(items = items.map { current ->
        if (current.isFocused) current.copy(paragraphType = paragraphType)
        else current
    })

    fun applyFormat(
        formatType: FormatType,
        formatText: FormatText,
        deleteFormat: (String) -> Unit
    ) =
        copy(items = items.map { current ->
            if (current.isFocused) {
                current.checkIfExistsFormatWithSameIndexes(formatType, formatText, deleteFormat)
                    ?: current.checkIfMatchingFormat(formatType, formatText)
                    ?: current.addFormatTextInCursor(formatText.copy(id = getUUID()))
            } else current
        })

    fun duplicate(): Note {
        val newNoteId = getUUID()
        return this.copy(
            id = newNoteId,
            items = items.map { noteItem -> noteItem.duplicate(newNoteId) })
    }

    fun getFocusedItem() = items.firstOrNull { it.isFocused }

    fun removeFocus() = copy(items = items.map { it.removeFocus() })

    fun setCursorOnLastPosition() =
        copy(items = items.map { noteItem -> noteItem.setCursorOnLastPosition() })

    fun changeFocusIn(noteItem: NoteItem) =
        copy(items = items.map { current ->
            current.copy(isFocused = current.id == noteItem.id)
        })

    fun getItemsText(): String {
        var totalText = "*$title*\n\n"
        items.forEach { currentItem ->
            totalText += when {
                currentItem.isText() -> currentItem.text
                currentItem.isTable() -> currentItem.table?.getItemsText().orEmpty()
                else -> "-${currentItem.text}"
            }.plus("\n")
        }
        return totalText
    }
}
