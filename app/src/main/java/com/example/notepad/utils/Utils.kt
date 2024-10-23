package com.example.notepad.utils

import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.entities.FormatText
import com.example.model.entities.ParagraphType

fun getColor(hexColor: String): Color {
    return Color(android.graphics.Color.parseColor(hexColor))
}

fun FormatText.toTextStyle(isDarkTheme: Boolean): TextStyle {
    val color = getColor(if (isDarkTheme) textDarkColor else textLightColor)
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


fun Modifier.bottomBorder(strokeWidth: Dp = 0.5.dp, color: Color) = composed(
    factory = {
        val density = LocalDensity.current
        val strokeWidthPx = density.run { strokeWidth.toPx() }

        Modifier.drawBehind {
            val width = size.width
            val height = size.height - strokeWidthPx/2

            drawLine(
                color = color,
                start = Offset(x = 0f, y = height),
                end = Offset(x = width , y = height),
                strokeWidth = strokeWidthPx
            )
        }
    }
)

fun Modifier.topBorder(strokeWidth: Dp = 0.5.dp, color: Color) = composed(
    factory = {
        val density = LocalDensity.current
        val strokeWidthPx = density.run { strokeWidth.toPx() }

        Modifier.drawBehind {
            val width = size.width
            val strokeWidthHalf = strokeWidthPx / 2

            drawLine(
                color = color,
                start = Offset(x = 0f, y = strokeWidthHalf),
                end = Offset(x = width, y = strokeWidthHalf),
                strokeWidth = strokeWidthPx
            )
        }
    }
)

fun Modifier.startBorder(strokeWidth: Dp = 0.5.dp, color: Color) = composed(
    factory = {
        val density = LocalDensity.current
        val strokeWidthPx = density.run { strokeWidth.toPx() }

        Modifier.drawBehind {
            val height = size.height
            val strokeWidthHalf = strokeWidthPx / 2

            drawLine(
                color = color,
                start = Offset(x = strokeWidthHalf, y = 0f),
                end = Offset(x = strokeWidthHalf, y = height),
                strokeWidth = strokeWidthPx
            )
        }
    }
)

fun Modifier.endBorder(strokeWidth: Dp = 0.5.dp, color: Color) = composed(
    factory = {
        val density = LocalDensity.current
        val strokeWidthPx = density.run { strokeWidth.toPx() }

        Modifier.drawBehind {
            val width = size.width - strokeWidthPx / 2
            val height = size.height

            drawLine(
                color = color,
                start = Offset(x = width, y = 0f),
                end = Offset(x = width, y = height),
                strokeWidth = strokeWidthPx
            )
        }
    }
)