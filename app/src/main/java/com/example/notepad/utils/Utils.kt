package com.example.notepad.utils

import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.entities.FormatText

fun getColor(hexColor: String): Color {
    return Color(android.graphics.Color.parseColor(hexColor))
}

fun getAnnotatedString(
    text: String,
    formatTexts: List<FormatText>,
    isDarkTheme: Boolean
): AnnotatedString {
    val styles = formatTexts.map { format -> format.toSpanStyle(isDarkTheme) }
    return AnnotatedString(text = text, spanStyles = styles)
}

fun FormatText.toSpanStyle(isDarkTheme: Boolean): AnnotatedString.Range<SpanStyle> =
    AnnotatedString.Range(
        start = startIndex,
        end = endIndex,
        item = SpanStyle(
            color = getColor(if (isDarkTheme) color.darkColor else color.lightColor),
            fontSize = typeText.fontSize.sp,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            fontStyle = if (isItalic) FontStyle.Italic else FontStyle.Normal,
            textDecoration = when {
                isUnderline && isLineThrough -> TextDecoration.combine(
                    listOf(
                        TextDecoration.Underline,
                        TextDecoration.LineThrough,
                    )
                )

                isUnderline -> TextDecoration.Underline
                isLineThrough -> TextDecoration.LineThrough
                else -> TextDecoration.None
            },
        )
    )

fun Modifier.bottomBorder(strokeWidth: Dp = 0.5.dp, color: Color) = composed(
    factory = {
        val density = LocalDensity.current
        val strokeWidthPx = density.run { strokeWidth.toPx() }

        Modifier.drawBehind {
            val width = size.width
            val height = size.height - strokeWidthPx / 2

            drawLine(
                color = color,
                start = Offset(x = 0f, y = height),
                end = Offset(x = width, y = height),
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