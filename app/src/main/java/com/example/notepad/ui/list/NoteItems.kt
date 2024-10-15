package com.example.notepad.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.entities.Cell
import com.example.model.entities.FormatText
import com.example.model.entities.NoteItem
import com.example.model.entities.ParagraphType
import com.example.notepad.utils.*

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun CheckBoxItem(
    noteItem: NoteItem = mockCheckBoxItem,
    isDarkTheme: Boolean = false,
) {
    val color = getColor(
        if (isDarkTheme) noteItem.formatText.textDarkColor
        else noteItem.formatText.textLightColor
    )

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
                    modifier = Modifier.scale(0.75f),
                    enabled = false,
                    checked = noteItem.isChecked,
                    onCheckedChange = { _ -> },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary,
                        checkmarkColor = Color.White
                    )
                )
            }

            TextItem(
                text = noteItem.text,
                color = color,
                maxLines = 1,
                formatText = noteItem.formatText
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TextFieldItem(
    noteItem: NoteItem = mockTextItem,
    isDarkTheme: Boolean = false,
) {
    val color = getColor(
        if (isDarkTheme) noteItem.formatText.textDarkColor
        else noteItem.formatText.textLightColor
    )

    TextItem(
        text = noteItem.text,
        color = color,
        maxLines = 8,
        formatText = noteItem.formatText
    )
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
        val isStartCellTextLonger = table.startCell.text.length >= table.endCell.text.length

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .topBorder(if (!isPreviousItemTable) 0.5.dp else 0.dp, color = color)
                .bottomBorder(color = color)
                .startBorder(color = color)
                .endBorder(color = color)
        ) {
            CellItem(
                modifier = Modifier
                    .endBorder(color = if (isStartCellTextLonger) color else Color.Transparent)
                    .padding(horizontal = 4.dp)
                    .weight(1f),
                cell = table.startCell,
            )
            CellItem(
                modifier = Modifier
                    .startBorder(color = if (!isStartCellTextLonger) color else Color.Transparent)
                    .padding(horizontal = 4.dp)
                    .weight(1f),
                cell = table.endCell,
            )
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
            maxLines = 1,
            formatText = cell.formatText,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TextItem(
    text: String = mockTextItem.text,
    maxLines: Int = 1,
    color: Color = MaterialTheme.colorScheme.onBackground,
    formatText: FormatText = mockTextItem.formatText,
) {
    with(formatText) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = text,
            overflow = TextOverflow.Ellipsis,
            maxLines = maxLines,
            color = color,
            fontSize = 12.sp,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            fontStyle = if (isItalic) FontStyle.Italic else FontStyle.Normal,
            textDecoration = when {
                isUnderline && isLineThrough -> TextDecoration.combine(
                    listOf(TextDecoration.Underline, TextDecoration.LineThrough)
                )

                isUnderline -> TextDecoration.Underline
                isLineThrough -> TextDecoration.LineThrough
                else -> null
            },
            textAlign = when (paragraphType) {
                ParagraphType.LEFT -> TextAlign.Start
                ParagraphType.CENTER -> TextAlign.Center
                ParagraphType.RIGHT -> TextAlign.End
                else -> TextAlign.Justify
            }
        )
    }
}
