package com.example.model.enums

enum class TypeText(
    val fontSize: Int,
    val isBold: Boolean
) {
    TITLE(24, true),
    HEADER(20, false),
    SUBTITLE(16, true),
    BODY(16, false)
}