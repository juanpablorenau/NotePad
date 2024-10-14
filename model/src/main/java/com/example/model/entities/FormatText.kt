package com.example.model.entities

data class FormatText(
    val id: String,
    val formatTextId: String = "",
    val typeText: TypeText = TypeText.BODY,
    val fontSize: Int = 16,
    val isBold: Boolean = false,
    val isItalic: Boolean = false,
    val isUnderline: Boolean = false,
    val isLineThrough: Boolean = false,
    val textLightColor: String = TextColor.BASIC.lightColor,
    val textDarkColor: String = TextColor.BASIC.darkColor,
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
    PURPLE("#6A1B9A", "#9C27B0"),
    LAVENDER("#AB47BC", "#E1BEE7"),
    PINK("#D5006D", "#FF4081"),
    RED("#F44336", "#FF5252"),
    ORANGE("#FF5722", "#FF8A00"),
    YELLOW("#FFEB3B", "#FFD600"),
    GREEN("#4CAF50", "#8BC34A"),
    PALE_GREEN("#66BB6A", "#A5D6A7"),
    MINT_GREEN("#1DE9B6", "#00BFAE"),
    SKY_BLUE("#40C4FF", "#00B0FF"),
    BLUE("#2196F3", "#448AFF"),
    GRAY_BLUE("#90A4AE", "#B0BEC5"),
    GRAY("#9E9E9E", "#CFD8DC"),
    GRAY_GREEN("#8D8B73", "#B3C9A4"),
    BROWN("#795548", "#A1887F"),
}