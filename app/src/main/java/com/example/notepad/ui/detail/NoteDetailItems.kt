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
import androidx.compose.runtime.remember
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.model.entities.NoteItem
import com.example.model.entities.NoteItemType


@Preview(showBackground = true)
@Composable
fun CheckBoxItem(
    checkBox: NoteItem = NoteItem(type = NoteItemType.CHECK_BOX),
    addNewCheckBox: () -> Unit = {},
    updateCheckBox: (NoteItem) -> Unit = {},
    deleteCheckBox: (String) -> Unit = {},
) {
    val focusRequester = remember(checkBox.id) { FocusRequester() }

    LaunchedEffect(checkBox.id) { focusRequester.requestFocus() }

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
                checked = checkBox.isChecked,
                onCheckedChange = { newChecked ->
                    updateCheckBox(checkBox.copy(text = checkBox.text, isChecked = newChecked))
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    checkmarkColor = Color.White
                )
            )

            SelectionContainer {
                BasicTextField(
                    modifier = Modifier.focusRequester(focusRequester),
                    value = checkBox.text,
                    singleLine = true,
                    onValueChange = { newText ->
                        updateCheckBox(
                            checkBox.copy(
                                text = newText,
                                isChecked = checkBox.isChecked
                            )
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (checkBox.text.isNotEmpty()) addNewCheckBox()
                            else deleteCheckBox(checkBox.id)
                        },

                        ),
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.secondary)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TextFieldItem(
    noteItem: NoteItem = NoteItem(text = "Sample Text", type = NoteItemType.TEXT),
    updateTextField: (NoteItem) -> Unit = {},
    deleteTextField: (String) -> Unit = {},
) {
    val focusRequester = remember(noteItem.id) { FocusRequester() }

    LaunchedEffect(noteItem.id) {
        focusRequester.requestFocus()
    }

    Box(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .focusRequester(focusRequester)
            .onKeyEvent { event ->
                if (event.key == Key.Delete && noteItem.text.isEmpty()) {
                    deleteTextField(noteItem.id)
                    true
                } else false
            }
    ) {
        SelectionContainer{
            BasicTextField(
                modifier = Modifier
                    .padding(horizontal = 24.dp),
                value = noteItem.text,
                onValueChange = { newText ->
                    updateTextField(noteItem.copy(text = newText))
                },
                textStyle = TextStyle(color = MaterialTheme.colorScheme.secondary)
            )
        }
    }
}
