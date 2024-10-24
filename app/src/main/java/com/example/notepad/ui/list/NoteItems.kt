package com.example.notepad.ui.list

import androidx.compose.foundation.layout.*
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
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
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
