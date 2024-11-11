package com.example.model.enums

enum class TypeText(
    val fontSize: Int,
    val isBold: Boolean
) {
    TITLE(24, true),
    HEADER(20, false),
    SUBTITLE(14, true),
    BODY(14, false)
}