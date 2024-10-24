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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.example.model.entities.Cell
import com.example.model.entities.NoteItem
import com.example.notepad.utils.*

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun CheckBoxItem(
    noteItem: NoteItem = mockCheckBoxItem,
    isDarkTheme: Boolean = false,
    currentFocus: FocusRequester = FocusRequester(),
    previousFocus: FocusRequester? = null,
    addCheckBox: (String) -> Unit = {},
    updateNoteItem: (NoteItem) -> Unit = {},
    changeFocusIn: (NoteItem) -> Unit = {},
    deleteNoteItemField: (NoteItem) -> Unit = {},
) {
    var isChecked by remember(noteItem.id) { mutableStateOf(noteItem.isChecked) }
    var textField by remember(noteItem.id) {
        mutableStateOf(TextFieldValue(noteItem.text, TextRange(noteItem.text.length)))
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.Left
        ) {
            CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { newChecked -> isChecked = newChecked },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary,
                        checkmarkColor = Color.White
                    )
                )
            }

            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp)
                    .focusRequester(currentFocus)
                    .onFocusChanged { if (it.isFocused) changeFocusIn(noteItem) }
                    .onKeyEvent {
                        if (it.key == Key.Backspace && textField.text.isEmpty()) {
                            previousFocus?.requestFocus()
                            deleteNoteItemField(noteItem)
                            true
                        } else false
                    },
                value = textField,
                singleLine = true,
                onValueChange = { newTextFieldValue -> textField = newTextFieldValue },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (textField.text.isNotEmpty()) addCheckBox(noteItem.id)
                        else {
                            previousFocus?.requestFocus()
                            deleteNoteItemField(noteItem)
                        }
                    },
                ),
                textStyle = noteItem.formatText.toTextStyle(isDarkTheme),
            )
        }
    }

    LaunchedEffect(noteItem.id) {
        if (noteItem.isFocused) currentFocus.requestFocus()
        else currentFocus.freeFocus()
    }

    LifecycleResumeEffect(noteItem.id, textField.text, isChecked) {
        onPauseOrDispose {
            updateNoteItem(noteItem.copy(text = textField.text, isChecked = isChecked))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TextFieldItem(
    noteItem: NoteItem = mockTextItem,
    isDarkTheme: Boolean = false,
    currentFocus: FocusRequester = FocusRequester(),
    previousFocus: FocusRequester? = null,
    updateNoteItem: (NoteItem) -> Unit = {},
    changeFocusIn: (NoteItem) -> Unit = {},
    deleteTextField: (NoteItem) -> Unit = {},
) {
    var textField by remember(noteItem.id) {
        mutableStateOf(TextFieldValue(noteItem.text, TextRange(noteItem.text.length)))
    }

    BasicTextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(currentFocus)
            .onFocusChanged { if (it.isFocused) changeFocusIn(noteItem) }
            .onKeyEvent {
                if (it.key == Key.Backspace && textField.text.isEmpty()) {
                    previousFocus?.requestFocus()
                    deleteTextField(noteItem)
                    true
                } else false
            },
        textStyle = noteItem.formatText.toTextStyle(isDarkTheme),
        value = textField,
        onValueChange = { newTextFieldValue -> textField = newTextFieldValue },
    )

    LaunchedEffect(noteItem.id) {
        if (noteItem.isFocused) currentFocus.requestFocus()
        else currentFocus.freeFocus()
    }

    LifecycleResumeEffect(noteItem.id, textField.text) {
        onPauseOrDispose { updateNoteItem(noteItem.copy(text = textField.text)) }
    }
}

@Preview(showBackground = true)
@Composable
fun TableItem(
    noteItem: NoteItem = mockTableItem,
    isDarkTheme: Boolean = false,
    isPreviousItemTable: Boolean = false,
    cellFocusList: List<FocusRequester> = mockFocusRequesters,
    previousFocus: FocusRequester? = null,
    updateNoteItem: (NoteItem) -> Unit = {},
    changeFocusIn: (NoteItem) -> Unit = {},
    deleteNoteItemField: (NoteItem) -> Unit = {},
) {
    noteItem.table?.let { table ->
        val color = MaterialTheme.colorScheme.onBackground

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
                val previousCellFocus = cellFocusList.getOrNull(index - 1) ?: previousFocus

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
                    currentFocusRequester = cellFocusList[index],
                    previousFocusRequester = previousCellFocus,
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
                    if (it.isFocused) changeFocusIn(noteItem.changeFocusInTable(cell.id))
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
            textStyle = cell.formatText.toTextStyle(isDarkTheme),
            value = textField,
            onValueChange = { newTextFieldValue ->
                textField = newTextFieldValue
                updateNoteItem(noteItem.applyInTable(cell.copy(text = textField.text)))
            },
        )
    }

    LaunchedEffect(cell.id) {
        if (cell.isFocused) currentFocusRequester.requestFocus()
        else currentFocusRequester.freeFocus()
    }
}
