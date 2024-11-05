package com.example.model.entities

import com.example.model.enums.FormatType
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

    fun applyFormat(formatType: FormatType, formatText: FormatText) =
        copy(items = items.map { current ->
            if (current.isFocused) {
                current.getFormatTextWithSameIndexes()?.let { sameIndexesFormat ->
                    current
                        .removeFormatText(sameIndexesFormat)
                        .addFormatText(mergedFormat(formatType, formatText, sameIndexesFormat))
                } ?: current.addFormatText(formatText)
            } else current
        })

    private fun mergedFormat(
        formatType: FormatType, newFormat: FormatText, oldFormat: FormatText
    ) = with(newFormat) {
        when (formatType) {
            FormatType.BOLD -> oldFormat.copy(isBold = isBold)
            FormatType.ITALIC -> oldFormat.copy(isItalic = isItalic)
            FormatType.UNDERLINE -> oldFormat.copy(isUnderline = isUnderline)
            FormatType.LINE_THROUGH -> oldFormat.copy(isLineThrough = isLineThrough)
            FormatType.PARAGRAPH_TYPE -> oldFormat.copy(paragraphType = paragraphType)
            FormatType.TEXT_COLOR -> oldFormat.copy(color = color)
            FormatType.TYPE_TEXT -> oldFormat.copy(typeText = typeText, isBold = typeText.isBold)
        }
    }

    fun duplicate(): Note {
        val newNoteId = getUUID()
        return this.copy(
            id = newNoteId,
            items = items.map { noteItem -> noteItem.duplicate(newNoteId) })
    }

    fun getFocusedItem() = items.firstOrNull { it.isFocused }

    fun setFocusOnLastItem() = copy(items = items
        .mapIndexed { index, noteItem ->
            if (index == items.lastIndex) noteItem.initFocus()
            else noteItem.removeFocus()
        }
    )

    fun changeFocusIn(noteItem: NoteItem) =
        copy(items = items.map { current ->
            if (current.id == noteItem.id) noteItem
            else current
        })
}
