package com.example.notepad.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.example.model.entities.FormatText
import com.example.model.entities.NoteItem
import com.example.model.enums.ParagraphType

fun getColorFromHex(hexColor: String) = Color(android.graphics.Color.parseColor(hexColor))

fun getAnnotatedString(
    noteItem: NoteItem,
    isDarkTheme: Boolean,
    isDynamicFontSize: Boolean = true
): AnnotatedString {
    val spanStyles = noteItem.formatTexts.mapNotNull { format ->
        format.toSpanStyle(isDarkTheme, isDynamicFontSize)
    }

    return AnnotatedString(
        text = noteItem.text,
        spanStyles = spanStyles,
        paragraphStyles = listOf(noteItem.toParagraphStyle())
    )
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
        Log.e("SpanStyle", "startIndex: $startIndex, endIndex: $endIndex")
        null
    }

fun NoteItem.toParagraphStyle(): AnnotatedString.Range<ParagraphStyle> =
    AnnotatedString.Range(
        start = 0,
        end = text.length,
        item = ParagraphStyle(
            textAlign = when (paragraphType) {
                ParagraphType.LEFT -> TextAlign.Left
                ParagraphType.JUSTIFY -> TextAlign.Justify
                ParagraphType.CENTER -> TextAlign.Center
                ParagraphType.RIGHT -> TextAlign.Right
            }
        )
    )


fun setClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Copied Text", text)
    clipboard.setPrimaryClip(clip)
}

fun showToast(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, message, duration).show()
}

fun getAppVersion(context: Context): String {
    return try {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        packageInfo.versionName ?: "1.0"
    } catch (e: Exception) {
        "1.0"
    }
}