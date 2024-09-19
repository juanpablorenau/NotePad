package com.example.notepad.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.model.entities.NoteItem
import com.example.model.entities.NoteItemType


@Composable
fun CheckBoxItem(
    noteItem: NoteItem = NoteItem(type = NoteItemType.CHECK_BOX),
    currentFocusRequester: FocusRequester,
    previousFocusRequester: FocusRequester?,
    addCheckBox: (String) -> Unit = {},
    updateCheckBox: (NoteItem) -> Unit = {},
    deleteCheckBox: (String) -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 24.dp)
            .onKeyEvent {
                if (it.key == Key.Backspace && noteItem.text.isEmpty()) {
                    previousFocusRequester?.requestFocus()
                    deleteCheckBox(noteItem.id)
                    true
                } else false
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.Left
        ) {
            Checkbox(
                checked = noteItem.isChecked,
                onCheckedChange = { newChecked ->
                    updateCheckBox(noteItem.copy(text = noteItem.text, isChecked = newChecked))
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    checkmarkColor = Color.White
                )
            )

            SelectionContainer {
                BasicTextField(
                    modifier = Modifier.focusRequester(currentFocusRequester),
                    value = noteItem.text,
                    singleLine = true,
                    onValueChange = { newText ->
                        updateCheckBox(
                            noteItem.copy(text = newText, isChecked = noteItem.isChecked)
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (noteItem.text.isNotEmpty()) addCheckBox(noteItem.id)
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
    }

    LaunchedEffect(noteItem.id) {
        currentFocusRequester.requestFocus()
    }
}

@Composable
fun TextFieldItem(
    noteItem: NoteItem = NoteItem(text = "Sample Text", type = NoteItemType.TEXT),
    currentFocusRequester: FocusRequester,
    previousFocusRequester: FocusRequester?,
    updateTextField: (NoteItem) -> Unit = {},
    deleteTextField: (String) -> Unit = {},
) {

    Box(
        modifier = Modifier
            .onKeyEvent {
                if (it.key == Key.Backspace && noteItem.text.isEmpty()) {
                    previousFocusRequester?.requestFocus()
                    deleteTextField(noteItem.id)
                    true
                } else false
            }
    ) {
        BasicTextField(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .focusRequester(currentFocusRequester),
            value = noteItem.text,
            onValueChange = { newText ->
                updateTextField(noteItem.copy(text = newText))
            },
            textStyle = TextStyle(color = MaterialTheme.colorScheme.secondary)
        )
    }

    LaunchedEffect(noteItem.id) {
        currentFocusRequester.requestFocus()
    }
}
