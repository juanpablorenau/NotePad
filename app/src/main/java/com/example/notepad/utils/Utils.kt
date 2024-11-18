package com.example.notepad.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.example.model.entities.FormatText

fun getColorFromHex(hexColor: String) = Color(android.graphics.Color.parseColor(hexColor))

fun getAnnotatedString(
    text: String,
    formatTexts: List<FormatText>,
    isDarkTheme: Boolean,
    isDynamicFontSize: Boolean = true
): AnnotatedString {
    val styles =
        formatTexts.mapNotNull { format -> format.toSpanStyle(isDarkTheme, isDynamicFontSize) }
    return AnnotatedString(text = text, spanStyles = styles)
}

fun FormatText.toSpanStyle(
    isDarkTheme: Boolean,
    isDynamicFontSize: Boolean
): AnnotatedString.Range<SpanStyle>? =
    try {
        AnnotatedString.Range(
            start = startIndex,
            end = endIndex,
            item = SpanStyle(
                color = getColorFromHex(getColor(isDarkTheme)),
                fontSize = if (isDynamicFontSize) typeText.fontSize.sp else 12.sp,
                fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
                fontStyle = if (isItalic) FontStyle.Italic else FontStyle.Normal,
                textDecoration = when {
                    isUnderline && isLineThrough -> TextDecoration.combine(
                        listOf(TextDecoration.Underline, TextDecoration.LineThrough)
                    )

                    isUnderline -> TextDecoration.Underline
                    isLineThrough -> TextDecoration.LineThrough
                    else -> TextDecoration.None
                },
            )
        )
    } catch (e: Exception) {
        Log.e("SPAN", "startIndex: $startIndex, endIndex: $endIndex")
        null
    }

fun setClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Copied Text", text)
    clipboard.setPrimaryClip(clip)
}

fun showToast(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, message, duration).show()
}