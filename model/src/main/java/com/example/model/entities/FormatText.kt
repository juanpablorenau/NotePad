package com.example.model.entities

data class FormatText(
    val typeText: TypeText = TypeText.BODY,
    val fontSize: Int = 16,
    val isBold: Boolean = false,
    val isItalic: Boolean = false,
    val isUnderline: Boolean = false,
    val isLineThrough: Boolean = false,
    val textColor: TextColor = TextColor.BASIC,
    val paragraphType: ParagraphType = ParagraphType.LEFT,
)

enum class TypeText {
    TITLE, HEADER, SUBTITLE, BODY
}

enum class ParagraphType {
    LEFT, RIGHT, CENTER, JUSTIFY
}

enum class TextColor(
    val lightColor: String,
    val darkColor: String
) {
    BASIC("#000000", "#FFFFFF"),
}