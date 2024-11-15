package com.example.notepad.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.entities.Cell
import com.example.model.entities.FormatText
import com.example.model.entities.NoteItem
import com.example.notepad.ui.detail.CellItem
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
fun TextItem(
    text: String = "",
    formatTexts: List<FormatText> = emptyList(),
    isDarkTheme: Boolean = false,
    maxLines: Int = 1,
) {
    var annotatedString by remember { mutableStateOf(AnnotatedString(text)) }

    LaunchedEffect(text, formatTexts, isDarkTheme) {
        annotatedString = getAnnotatedString(text, formatTexts, isDarkTheme)
    }

    Text(
        modifier = Modifier.fillMaxWidth(),
        text = annotatedString,
        overflow = TextOverflow.Ellipsis,
        maxLines = maxLines,
        fontSize = 12.sp
    )
}

@Preview(showBackground = true)
@Composable
fun TextFieldItem(
    noteItem: NoteItem = mockTextItem,
    isDarkTheme: Boolean = false,
) {
    TextItem(
        text = noteItem.text,
        formatTexts = noteItem.formatTexts,
        isDarkTheme = isDarkTheme,
        maxLines = 4,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun CheckBoxItem(
    noteItem: NoteItem = mockCheckBoxItem,
    isDarkTheme: Boolean = false,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Absolute.Left,
    ) {
        CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
            Checkbox(
                modifier = Modifier
                    .size(18.dp)
                    .scale(0.75f)
                    .padding(end = 4.dp),
                enabled = false,
                checked = noteItem.isChecked,
                onCheckedChange = { _ -> },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    checkmarkColor = Color.White
                )
            )
        }

        HorizontalDivider(
            modifier = Modifier.width(4.dp),
            color = Color.Transparent
        )

        TextItem(
            text = noteItem.text,
            formatTexts = noteItem.formatTexts,
            maxLines = 1,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TableItem(
    noteItem: NoteItem = mockTableItem,
    isDarkTheme: Boolean = false,
    isPreviousItemTable: Boolean = false,
) {
    noteItem.table?.let { table ->
        val color = MaterialTheme.colorScheme.onBackground

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .topBorder(if (!isPreviousItemTable) 0.5.dp else 0.dp, color = color)
                .endBorder(color = color)
                .bottomBorder(color = color)
        ) {
            table.cells.forEach { cell ->
                CellItem(
                    modifier = Modifier
                        .startBorder(color = color)
                        .padding(horizontal = 4.dp)
                        .weight(1f),
                    cell = cell,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CellItem(
    modifier: Modifier = Modifier,
    cell: Cell = mockCell,
) {
    Box(modifier = modifier) {
        TextItem(
            text = cell.text,
            formatTexts = cell.formatTexts,
            maxLines = 1,
        )
    }
}
