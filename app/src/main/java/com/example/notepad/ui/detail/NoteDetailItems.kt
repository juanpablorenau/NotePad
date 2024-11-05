package com.example.notepad.ui.detail

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.model.entities.Cell
import com.example.model.entities.NoteItem
import com.example.notepad.utils.bottomBorder
import com.example.notepad.utils.endBorder
import com.example.notepad.utils.getAnnotatedString
import com.example.notepad.utils.mockCell
import com.example.notepad.utils.mockCheckBoxItem
import com.example.notepad.utils.mockTableItem
import com.example.notepad.utils.mockTextItem
import com.example.notepad.utils.startBorder
import com.example.notepad.utils.topBorder

@Preview(showBackground = true)
@Composable
fun TextFieldItem(
    noteItem: NoteItem = mockTextItem,
    isDarkTheme: Boolean = false,
    updateNoteItem: (NoteItem) -> Unit = {},
    changeFocusIn: (NoteItem) -> Unit = {},
    deleteTextField: (NoteItem) -> Unit = {},
) {
    val focusRequester = remember(noteItem.id) { FocusRequester() }
    val annotatedString = getAnnotatedString(noteItem.text, noteItem.formatTexts, isDarkTheme)
    var textField by remember(noteItem.id, annotatedString) {
        mutableStateOf(TextFieldValue(annotatedString, TextRange(annotatedString.length)))
    }

    BasicTextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onFocusChanged { changeFocusIn(noteItem.copy(isFocused = it.isFocused)) }
            .onKeyEvent {
                if (it.key == Key.Backspace && textField.text.isEmpty()) {
                    deleteTextField(noteItem)
                    true
                } else false
            },
        cursorBrush = SolidColor(MaterialTheme.colorScheme.secondary),
        value = textField,
        onValueChange = { newTextFieldValue ->
            if (newTextFieldValue.selection != textField.selection) {
                updateNoteItem(
                    noteItem.copy(
                        text = newTextFieldValue.text,
                        cursorStartIndex = newTextFieldValue.selection.start,
                        cursorEndIndex = newTextFieldValue.selection.end
                    )
                )
                textField = newTextFieldValue
            }
        },
    )

    LaunchedEffect(noteItem.id, noteItem.isFocused) {
        if (noteItem.isFocused) focusRequester.requestFocus()
        else focusRequester.freeFocus()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun CheckBoxItem(
    noteItem: NoteItem = mockCheckBoxItem,
    isDarkTheme: Boolean = false,
    addCheckBox: (String) -> Unit = {},
    updateNoteItem: (NoteItem) -> Unit = {},
    changeFocusIn: (NoteItem) -> Unit = {},
    deleteNoteItemField: (NoteItem) -> Unit = {},
) {
    val focusRequester = remember(noteItem.id) { FocusRequester() }
    val annotatedString = getAnnotatedString(noteItem.text, noteItem.formatTexts, isDarkTheme)
    var isChecked by remember(noteItem.id) { mutableStateOf(noteItem.isChecked) }
    var textField by remember(noteItem.id, annotatedString) {
        mutableStateOf(TextFieldValue(annotatedString, TextRange(annotatedString.length)))
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Absolute.Left
    ) {
        CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { newChecked ->
                    updateNoteItem(noteItem.copy(isChecked = newChecked))
                    isChecked = newChecked
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    checkmarkColor = Color.White
                )
            )
        }

        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 4.dp)
                .focusRequester(focusRequester)
                .onFocusChanged { changeFocusIn(noteItem.copy(isFocused = it.isFocused)) }
                .onKeyEvent {
                    if (it.key == Key.Backspace && textField.text.isEmpty()) {
                        deleteNoteItemField(noteItem)
                        true
                    } else false
                },
            cursorBrush = SolidColor(MaterialTheme.colorScheme.secondary),
            value = textField,
            onValueChange = { newTextFieldValue ->
                if (newTextFieldValue.selection != textField.selection) {
                    updateNoteItem(
                        noteItem.copy(
                            text = newTextFieldValue.text,
                            cursorStartIndex = newTextFieldValue.selection.start,
                            cursorEndIndex = newTextFieldValue.selection.end
                        )
                    )
                    textField = newTextFieldValue
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (textField.text.isNotEmpty()) addCheckBox(noteItem.id)
                    else deleteNoteItemField(noteItem)
                },
            ),
        )
    }

    LaunchedEffect(noteItem.id, noteItem.isFocused) {
        if (noteItem.isFocused) focusRequester.requestFocus()
        else focusRequester.freeFocus()
    }
}

@Preview(showBackground = true)
@Composable
fun TableItem(
    noteItem: NoteItem = mockTableItem,
    isDarkTheme: Boolean = false,
    isPreviousItemTable: Boolean = false,
    updateNoteItem: (NoteItem) -> Unit = {},
    changeFocusIn: (NoteItem) -> Unit = {},
    deleteNoteItemField: (NoteItem) -> Unit = {},
) {
    noteItem.table?.let { table ->
        val color = MaterialTheme.colorScheme.onBackground
        val focusRequesters = remember(noteItem.id) { List(table.cells.size) { FocusRequester() } }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(intrinsicSize = IntrinsicSize.Max)
                .topBorder(if (!isPreviousItemTable) 0.5.dp else 0.dp, color = color)
                .bottomBorder(color = color)
                .startBorder(color = color)
                .endBorder(color = color),
        ) {
            table.cells.forEachIndexed { index, cell ->
                CellItem(
                    modifier = Modifier
                        .fillMaxHeight()
                        .border(0.25.dp, color = color)
                        .padding(8.dp)
                        .weight(1f),
                    isDarkTheme = isDarkTheme,
                    noteItem = noteItem,
                    cell = cell,
                    isFirstCell = index == 0,
                    currentFocusRequester = focusRequesters[index],
                    previousFocusRequester = focusRequesters.getOrNull(index - 1),
                    updateNoteItem = updateNoteItem,
                    changeFocusIn = changeFocusIn,
                    deleteNoteItemField = deleteNoteItemField,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CellItem(
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean = false,
    noteItem: NoteItem = mockTableItem,
    cell: Cell = mockCell,
    isFirstCell: Boolean = false,
    currentFocusRequester: FocusRequester = FocusRequester(),
    previousFocusRequester: FocusRequester? = null,
    updateNoteItem: (NoteItem) -> Unit = {},
    changeFocusIn: (NoteItem) -> Unit = {},
    deleteNoteItemField: (NoteItem) -> Unit = {},
) {
    var textField by remember(cell.id) {
        mutableStateOf(TextFieldValue(cell.text, TextRange(cell.text.length)))
    }

    Box(modifier = modifier) {
        BasicTextField(
            modifier = Modifier
                .focusRequester(currentFocusRequester)
                .onFocusChanged {
                    changeFocusIn(noteItem.changeFocusInTable(cell.id, it.isFocused))
                }
                .onKeyEvent {
                    val isBackspace = it.key == Key.Backspace
                    when {
                        isFirstCell && isBackspace && noteItem.isTableEmpty() -> {
                            previousFocusRequester?.requestFocus()
                            deleteNoteItemField(noteItem)
                            true
                        }

                        !isFirstCell && isBackspace && textField.text.isEmpty() -> {
                            previousFocusRequester?.requestFocus()
                            true
                        }

                        else -> false
                    }
                },
            value = textField,
            onValueChange = { newTextFieldValue ->
                textField = newTextFieldValue
                updateNoteItem(noteItem.applyInTable(cell.copy(text = textField.text)))
            },
            cursorBrush = SolidColor(MaterialTheme.colorScheme.secondary),
        )
    }

    LaunchedEffect(cell.id, cell.isFocused) {
        if (cell.isFocused) currentFocusRequester.requestFocus()
        else currentFocusRequester.freeFocus()
    }
}