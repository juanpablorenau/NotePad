package com.example.notepad.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
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
import com.example.model.entities.Cell
import com.example.model.entities.NoteItem
import com.example.notepad.utils.*

@Preview(showBackground = true)
@Composable
fun CheckBoxItem(
    noteItem: NoteItem = mockCheckBoxItem,
    isDarkTheme: Boolean = false,
    currentFocusRequester: FocusRequester = FocusRequester(),
    previousFocusRequester: FocusRequester? = null,
    addCheckBox: (String) -> Unit = {},
    updateNoteItem: (NoteItem) -> Unit = {},
    deleteCheckBox: (NoteItem) -> Unit = {},
) {
    var isChecked by remember { mutableStateOf(noteItem.isChecked) }
    var textFieldValue by remember {
        mutableStateOf(TextFieldValue(noteItem.text, TextRange(noteItem.text.length)))
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.Left
        ) {
            Checkbox(
                checked = isChecked, onCheckedChange = { newChecked ->
                    isChecked = newChecked
                    updateNoteItem(
                        noteItem.copy(
                            text = textFieldValue.text, isChecked = isChecked, isFocused = true
                        )
                    )
                }, colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary, checkmarkColor = Color.White
                )
            )

            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(currentFocusRequester)
                    .onFocusChanged { if (it.isFocused) updateNoteItem(noteItem.copy(isFocused = true)) }
                    .onKeyEvent {
                        if (it.key == Key.Backspace && textFieldValue.text.isEmpty()) {
                            previousFocusRequester?.requestFocus()
                            deleteCheckBox(noteItem)
                            true
                        } else false
                    },
                value = textFieldValue,
                singleLine = true,
                onValueChange = { newTextFieldValue ->
                    textFieldValue = newTextFieldValue.copy(text = newTextFieldValue.text)
                    updateNoteItem(
                        noteItem.copy(
                            text = textFieldValue.text, isChecked = isChecked, isFocused = true
                        )
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (textFieldValue.text.isNotEmpty()) addCheckBox(noteItem.id)
                        else {
                            previousFocusRequester?.requestFocus()
                            deleteCheckBox(noteItem)
                        }
                    },
                ),
                textStyle = noteItem.formatText.toTextStyle(isDarkTheme),
            )
        }
    }

    LaunchedEffect(noteItem.id, noteItem.isFocused) {
        if (noteItem.isFocused) currentFocusRequester.requestFocus()
        else currentFocusRequester.freeFocus()
        textFieldValue = textFieldValue.copy(selection = TextRange(textFieldValue.text.length))
    }
}

@Preview(showBackground = true)
@Composable
fun TextFieldItem(
    noteItem: NoteItem = mockTextItem,
    isDarkTheme: Boolean = false,
    currentFocusRequester: FocusRequester = FocusRequester(),
    previousFocusRequester: FocusRequester? = null,
    updateNoteItem: (NoteItem) -> Unit = {},
    deleteTextField: (NoteItem) -> Unit = {},
) {
    var textFieldValue by remember {
        mutableStateOf(TextFieldValue(noteItem.text, TextRange(noteItem.text.length)))
    }

    BasicTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .focusRequester(currentFocusRequester)
            .onFocusChanged { if (it.isFocused) updateNoteItem(noteItem.copy(isFocused = true)) }
            .onKeyEvent {
                if (it.key == Key.Backspace && textFieldValue.text.isEmpty()) {
                    previousFocusRequester?.requestFocus()
                    deleteTextField(noteItem)
                    true
                } else false
            },
        textStyle = noteItem.formatText.toTextStyle(isDarkTheme),
        value = textFieldValue,
        onValueChange = { newTextFieldValue ->
            textFieldValue = newTextFieldValue.copy(text = newTextFieldValue.text)
            updateNoteItem(noteItem.copy(text = textFieldValue.text, isFocused = true))
        },
    )

    LaunchedEffect(noteItem.id, noteItem.isFocused) {
        if (noteItem.isFocused) currentFocusRequester.requestFocus()
        else currentFocusRequester.freeFocus()
        textFieldValue = textFieldValue.copy(selection = TextRange(textFieldValue.text.length))
    }
}

@Preview(showBackground = true)
@Composable
fun TableItem(
    noteItem: NoteItem = mockTableItem,
    isDarkTheme: Boolean = false,
    isPreviousItemTable: Boolean = false,
    updateNoteItem: (NoteItem) -> Unit = {},
) {
    noteItem.table?.let { table ->
        with(table) {
            val color = MaterialTheme.colorScheme.onBackground
            val isStartCellTextLonger = startCell.text.length >= endCell.text.length

            Row(modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { if (it.isFocused) updateNoteItem(noteItem.copy(isFocused = true)) }
                .padding(horizontal = 24.dp)
                .topBorder(if (!isPreviousItemTable) 0.5.dp else 0.dp, color = color)
                .bottomBorder(color = color)
                .startBorder(color = color)
                .endBorder(color = color)) {
                val endBorderColor = if (isStartCellTextLonger) color else Color.Transparent
                CellItem(
                    modifier = Modifier
                        .endBorder(color = endBorderColor)
                        .padding(8.dp)
                        .weight(1f),
                    isDarkTheme = isDarkTheme,
                    noteItem = noteItem,
                    cell = startCell,
                    updateNoteItem = updateNoteItem,
                )

                val startBorderColor = if (!isStartCellTextLonger) color else Color.Transparent

                CellItem(
                    modifier = Modifier
                        .startBorder(color = startBorderColor)
                        .padding(8.dp)
                        .weight(1f),
                    isDarkTheme = isDarkTheme,
                    noteItem = noteItem,
                    cell = endCell,
                    updateNoteItem = updateNoteItem,
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
    updateNoteItem: (NoteItem) -> Unit = {},
) {
    var cellTextField by remember {
        mutableStateOf(TextFieldValue(cell.text, TextRange(cell.text.length)))
    }

    Box(modifier = modifier) {
        BasicTextField(
            textStyle = cell.formatText.toTextStyle(isDarkTheme),
            value = TextFieldValue(cell.text, TextRange(cell.text.length)),
            onValueChange = { newTextFieldValue ->
                cellTextField = newTextFieldValue.copy(text = newTextFieldValue.text)
                updateNoteItem(
                    noteItem.copy(table = noteItem.table?.let { table ->
                        table.copy(
                            startCell = if (cell.isStartCell) cell.copy(text = cellTextField.text)
                            else table.startCell,
                            endCell = if (!cell.isStartCell) cell.copy(text = cellTextField.text)
                            else table.endCell
                        )
                    })
                )
            },
        )
    }
}