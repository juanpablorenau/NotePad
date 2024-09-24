package com.example.notepad.ui.detail

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.model.entities.NoteItem
import com.example.model.entities.NoteItemType

@Preview(showBackground = true)
@Composable
fun CheckBoxItem(
    noteItem: NoteItem = NoteItem(type = NoteItemType.CHECK_BOX),
    isCurrentFocused: Boolean = false,
    currentFocusRequester: FocusRequester = FocusRequester(),
    previousFocusRequester: FocusRequester? = null,
    addCheckBox: (String) -> Unit = {},
    updateCheckBox: (NoteItem) -> Unit = {},
    deleteCheckBox: (String) -> Unit = {},
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
                checked = isChecked,
                onCheckedChange = { newChecked ->
                    isChecked = newChecked
                    updateCheckBox(noteItem.copy(text = textFieldValue.text, isChecked = isChecked))
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    checkmarkColor = Color.White
                )
            )

            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(currentFocusRequester)
                    .onKeyEvent {
                        if (it.key == Key.Backspace && textFieldValue.text.isEmpty()) {
                            previousFocusRequester?.requestFocus()
                            deleteCheckBox(noteItem.id)
                            true
                        } else false
                    }
                    .onFocusChanged {
                        updateCheckBox(noteItem.copy(lastFocused = System.currentTimeMillis()))
                    },
                value = textFieldValue,
                singleLine = true,
                onValueChange = { newTextFieldValue ->
                    textFieldValue = newTextFieldValue.copy(text = newTextFieldValue.text)
                    updateCheckBox(noteItem.copy(text = textFieldValue.text, isChecked = isChecked))
                },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (textFieldValue.text.isNotEmpty()) addCheckBox(noteItem.id)
                        else {
                            previousFocusRequester?.requestFocus()
                            deleteCheckBox(noteItem.id)
                        }
                    },
                ),
                textStyle = TextStyle(color = MaterialTheme.colorScheme.secondary)
            )
        }
    }

    LaunchedEffect(noteItem.id) {
        if (isCurrentFocused) currentFocusRequester.requestFocus()
        else currentFocusRequester.freeFocus()
        textFieldValue = textFieldValue.copy(selection = TextRange(textFieldValue.text.length))
    }
}

@Preview(showBackground = true)
@Composable
fun TextFieldItem(
    noteItem: NoteItem = NoteItem(text = "Sample Text", type = NoteItemType.TEXT),
    isCurrentFocused: Boolean = false,
    currentFocusRequester: FocusRequester = FocusRequester(),
    previousFocusRequester: FocusRequester? = null,
    updateTextField: (NoteItem) -> Unit = {},
    deleteTextField: (String) -> Unit = {},
) {
    var textFieldValue by remember {
        mutableStateOf(TextFieldValue(noteItem.text, TextRange(noteItem.text.length)))
    }

    BasicTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .focusRequester(currentFocusRequester)
            .onKeyEvent {
                if (it.key == Key.Backspace && textFieldValue.text.isEmpty()) {
                    previousFocusRequester?.requestFocus()
                    deleteTextField(noteItem.id)
                    true
                } else false
            }
            .onFocusChanged {
                updateTextField(noteItem.copy(lastFocused = System.currentTimeMillis()))
            },
        value = textFieldValue,
        onValueChange = { newTextFieldValue ->
            textFieldValue = newTextFieldValue.copy(text = newTextFieldValue.text)
            updateTextField(noteItem.copy(text = textFieldValue.text))
        },
        textStyle = TextStyle(color = MaterialTheme.colorScheme.secondary)
    )

    LaunchedEffect(noteItem.id) {
        if (isCurrentFocused) currentFocusRequester.requestFocus()
        else currentFocusRequester.freeFocus()
        textFieldValue = textFieldValue.copy(selection = TextRange(textFieldValue.text.length))
    }
}