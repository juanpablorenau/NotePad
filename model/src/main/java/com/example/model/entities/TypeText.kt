package com.example.model.entities

data class TypeText(
    val text: String = "",
    val fontSize : Int = 24,
    val isBold : Boolean = false,
    val isItalic : Boolean = false,
    val isUnderline : Boolean = false
)