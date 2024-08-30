package com.example.notepad.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.model.entities.NoteItem
import com.example.model.entities.NoteItemType

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun CheckBoxItem(
    noteItem: NoteItem = NoteItem(type = NoteItemType.CHECK_BOX),
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.Left,
        ) {
            CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                Checkbox(
                    enabled = false,
                    checked = noteItem.isChecked,
                    onCheckedChange = { _ -> },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary,
                        checkmarkColor = Color.White
                    )
                )
            }

            Text(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                text = noteItem.text,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TextFieldItem(
    noteItem: NoteItem = NoteItem(text = "Sample Text", type = NoteItemType.TEXT),
) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        maxLines = 8,
        overflow = TextOverflow.Ellipsis,
        text = noteItem.text,
        color = MaterialTheme.colorScheme.secondary
    )
}
