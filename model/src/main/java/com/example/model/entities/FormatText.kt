package com.example.model.entities

data class FormatText(
    val typeText: TypeText = TypeText.BODY,
    val fontSize: Int = 24,
    val isBold: Boolean = false,
    val isItalic: Boolean = false,
    val isUnderline: Boolean = false,
    val isLineThrough: Boolean = false,
    val color: String = "#000000",
    val paragraphType: ParagraphType = ParagraphType.LEFT,
)

enum class TypeText {
    TITLE, HEADER, SUBTITLE, BODY
}

enum class ParagraphType {
    LEFT, RIGHT, CENTER, JUSTIFY

}