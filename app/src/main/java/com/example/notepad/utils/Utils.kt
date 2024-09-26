package com.example.notepad.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.example.model.entities.FormatText
import com.example.model.entities.ParagraphType

fun getColor(hexColor: String): Color {
    return Color(android.graphics.Color.parseColor(hexColor))
}

fun FormatText.toTextStyle(isDarkTheme: Boolean): TextStyle {
    val color = getColor(if (isDarkTheme) textColor.lightColor else textColor.darkColor)
    var textStyle = TextStyle(color = color, fontSize = fontSize.sp)

    if (isBold) textStyle = textStyle.copy(fontWeight = FontWeight.Bold)
    if (isItalic) textStyle = textStyle.copy(fontStyle = FontStyle.Italic)

    textStyle = when {
        isUnderline && isLineThrough -> textStyle.copy(
            textDecoration = TextDecoration.combine(
                listOf(TextDecoration.Underline, TextDecoration.LineThrough)
            )
        )

        isUnderline -> textStyle.copy(textDecoration = TextDecoration.Underline)
        isLineThrough -> textStyle.copy(textDecoration = TextDecoration.LineThrough)
        else -> textStyle
    }

    textStyle = when (paragraphType) {
        ParagraphType.LEFT -> textStyle.copy(textAlign = TextAlign.Left)
        ParagraphType.RIGHT -> textStyle.copy(textAlign = TextAlign.Right)
        ParagraphType.CENTER -> textStyle.copy(textAlign = TextAlign.Center)
        ParagraphType.JUSTIFY -> textStyle.copy(textAlign = TextAlign.Justify)
    }

    return textStyle
}