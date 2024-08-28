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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.model.entities.NoteCheckBox
import com.example.model.entities.NoteTextField


@Preview(showBackground = true)
@Composable
fun CheckBoxItem(
    checkBox: NoteCheckBox = NoteCheckBox(text = "Sample Text"),
    addNewCheckBox: () -> Unit = {},
    updateCheckBox: (NoteCheckBox) -> Unit = {},
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

            BasicTextField(
                modifier = Modifier.focusRequester(focusRequester),
                value = checkBox.text,
                singleLine = true,
                onValueChange = { newText ->
                    updateCheckBox(checkBox.copy(text = newText, isChecked = checkBox.isChecked))
                },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    if (checkBox.text.isNotEmpty()) addNewCheckBox()
                    else deleteCheckBox(checkBox.id)
                }),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TextFieldItem(
    noteItem: NoteTextField = NoteTextField(text = "Sample Text"),
    updateTextField: (NoteTextField) -> Unit = {},
) {
    val focusRequester = remember(noteItem.id) { FocusRequester() }

    LaunchedEffect(noteItem.id) {
        focusRequester.requestFocus()
    }

    Box(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .focusRequester(focusRequester)
    ) {
        BasicTextField(
            modifier = Modifier
                .padding(horizontal = 24.dp),
            value = noteItem.text,
            onValueChange = { newText ->
                updateTextField(noteItem.copy(text = newText))
            },
        )
    }
}
