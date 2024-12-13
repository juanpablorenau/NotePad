package com.example.model.entities

import com.example.model.enums.FormatType
import com.example.model.enums.NoteItemType
import com.example.model.enums.ParagraphType
import com.example.model.utils.getUUID
import com.example.model.utils.normalize
import com.example.model.utils.orFalse

data class NoteItem(
    val id: String = "",
    val noteId: String = "",
    val text: String = "",
    val cursorStartIndex: Int = 0,
    val cursorEndIndex: Int = 0,
    val isChecked: Boolean = false,
    val type: NoteItemType = NoteItemType.TEXT,
    val isFocused: Boolean = true,
    val formatTexts: List<FormatText> = emptyList(),
    val paragraphType: ParagraphType = ParagraphType.LEFT,
    val table: Table? = null,
    val index: Int = 0,
) {
    constructor(id: String, noteId: String, index: Int) : this(
        id = id,
        noteId = noteId,
        type = NoteItemType.TEXT,
        index = index,
        isFocused = true,
    )

    constructor(id: String, noteId: String, isChecked: Boolean, index: Int) : this(
        id = id,
        noteId = noteId,
        type = NoteItemType.CHECK_BOX,
        isChecked = isChecked,
        index = index,
        isFocused = true,
    )

    constructor(id: String, noteId: String, table: Table, index: Int) : this(
        id = id,
        noteId = noteId,
        type = NoteItemType.TABLE,
        table = table.copy(noteItemId = id),
        index = index,
        isFocused = true,
    )

    fun isText() = type == NoteItemType.TEXT
    fun isTable() = type == NoteItemType.TABLE

    fun containsInItem(query: String) =
        if (isTable()) containsInTable(query)
        else text.normalize().contains(query.normalize(), ignoreCase = true)

    private fun containsInTable(query: String) = table?.contains(query) ?: false

    fun duplicate(newNoteId: String): NoteItem {
        val newNoteItemId = getUUID()
        return this.copy(
            id = newNoteItemId,
            noteId = newNoteId,
            formatTexts = formatTexts.map { it.duplicate(newNoteItemId) },
            table = table?.duplicate(newNoteItemId)
        )
    }

    fun applyInTable(cell: Cell) = copy(table = table?.applyInTable(cell))

    fun changeFocusInTable(cellId: String) =
        copy(table = table?.changeFocus(cellId))

    fun initFocus() = copy(isFocused = true, table = table?.initFocus())

    fun removeFocus() = copy(isFocused = false, table = table?.removeFocus())

    fun restoreFocus() = copy(isFocused = true, table = table?.restoreFocus())

    fun setCursorOnLastPosition() =
        copy(cursorStartIndex = text.length, cursorEndIndex = text.length)

    fun isTableEmpty() = table?.isEmpty().orFalse()

    fun updateFormatsAfterDeletingCharacter(deletedIndex: Int, deleteFormat: (String) -> Unit) =
        copy(
            formatTexts = formatTexts.mapNotNull { current ->
                when {
                    current.shouldDelete(deletedIndex) -> deleteFormat(current.id).let { null }
                    current.isBefore(deletedIndex) -> current
                    current.isBetween(deletedIndex) -> current.copy(endIndex = current.endIndex - 1)
                    current.isAfter(deletedIndex) -> {
                        current.copy(
                            startIndex = current.startIndex - 1, endIndex = current.endIndex - 1
                        )
                    }

                    else -> current
                }
            }
        )

    fun updateFormatsAfterAddingCharacter(addedIndex: Int) =
        copy(formatTexts = formatTexts.map { current ->
            when {
                current.isRightAfter(addedIndex) -> current.copy(endIndex = current.endIndex + 1)
                current.isBefore(addedIndex) -> current
                current.isBetween(addedIndex) -> current.copy(endIndex = current.endIndex + 1)
                current.isAfter(addedIndex) -> {
                    current.copy(
                        startIndex = current.startIndex + 1, endIndex = current.endIndex + 1
                    )
                }

                else -> current
            }
        })

    fun checkIfExistsFormatWithSameIndexes(
        formatType: FormatType,
        formatText: FormatText,
        deleteFormat: (String) -> Unit
    ): NoteItem? =
        findFormatWithSameIndexes()?.let { sameIndexesFormat ->
            this
                .removeFormatText(sameIndexesFormat.id)
                .also { deleteFormat(sameIndexesFormat.id) }
                .addFormatText(mergedFormat(formatType, formatText, sameIndexesFormat))
        }

    fun checkIfMatchingFormat(
        formatType: FormatType,
        formatText: FormatText,
    ): NoteItem? =
        findMatchingFormat()?.let { matchingFormat ->
            val newFormatText = matchingFormat.copy(
                id = getUUID(),
                startIndex = cursorStartIndex,
                endIndex = cursorEndIndex
            )

            this.addFormatText(mergedFormat(formatType, formatText, newFormatText))
        }

    private fun findFormatWithSameIndexes() =
        formatTexts.find { it.startIndex == cursorStartIndex && it.endIndex == cursorEndIndex }

    fun findMatchingFormat() =
        formatTexts.find { format ->
            format.startIndex <= cursorStartIndex && format.endIndex >= cursorEndIndex
        }

    private fun mergedFormat(
        formatType: FormatType, newFormat: FormatText, oldFormat: FormatText
    ) = with(newFormat) {
        when (formatType) {
            FormatType.BOLD -> oldFormat.copy(isBold = true)
            FormatType.ITALIC -> oldFormat.copy(isItalic = true)
            FormatType.UNDERLINE -> oldFormat.copy(isUnderline = true)
            FormatType.LINE_THROUGH -> oldFormat.copy(isLineThrough = true)
            FormatType.TEXT_COLOR -> oldFormat.copy(color = color)
            FormatType.TYPE_TEXT -> oldFormat.copy(typeText = typeText, isBold = typeText.isBold)
        }
    }

    private fun addFormatText(formatText: FormatText) = copy(
        formatTexts = formatTexts.toMutableList().apply {
            add(formatText.copy(itemId = id))
        }
    )

    fun addFormatTextInCursor(formatText: FormatText) = copy(
        formatTexts = formatTexts.toMutableList().apply {
            add(
                formatText.copy(
                    itemId = id,
                    startIndex = cursorStartIndex,
                    endIndex = cursorEndIndex
                )
            )
        }
    )

    private fun removeFormatText(formatTextId: String) = copy(
        formatTexts = formatTexts.toMutableList().apply {
            removeIf { formatTextId == it.id }
        })
}
