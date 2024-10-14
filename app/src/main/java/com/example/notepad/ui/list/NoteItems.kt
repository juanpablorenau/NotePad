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
import com.example.model.entities.NoteItem
import com.example.model.entities.ParagraphType
import com.example.notepad.utils.getColor
import com.example.notepad.utils.mockCheckBox
import com.example.notepad.utils.mockNoteItem

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun CheckBoxItem(
    noteItem: NoteItem = mockCheckBox,
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

            with(noteItem.formatText) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    text = noteItem.text,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
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
                    textAlign = when (noteItem.formatText.paragraphType) {
                        ParagraphType.LEFT -> TextAlign.Start
                        ParagraphType.CENTER -> TextAlign.Center
                        ParagraphType.RIGHT -> TextAlign.End
                        else -> TextAlign.Justify
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TextFieldItem(
    noteItem: NoteItem = mockNoteItem,
    isDarkTheme: Boolean = false,
) {
    val color = getColor(
        if (isDarkTheme) noteItem.formatText.textDarkColor
        else noteItem.formatText.textLightColor
    )

    with(noteItem.formatText) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            maxLines = 8,
            overflow = TextOverflow.Ellipsis,
            text = noteItem.text,
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
            textAlign = when (noteItem.formatText.paragraphType) {
                ParagraphType.LEFT -> TextAlign.Start
                ParagraphType.CENTER -> TextAlign.Center
                ParagraphType.RIGHT -> TextAlign.End
                else -> TextAlign.Justify
            }
        )
    }
}
