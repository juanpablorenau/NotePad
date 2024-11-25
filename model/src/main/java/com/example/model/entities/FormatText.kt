package com.example.model.entities

import com.example.model.enums.TextColor
import com.example.model.enums.TypeText
import com.example.model.utils.getUUID

data class FormatText(
    val id: String = "",
    val itemId: String = "",
    val startIndex: Int = 0,
    val endIndex: Int = 0,
    val typeText: TypeText = TypeText.BODY,
    val isBold: Boolean = false,
    val isItalic: Boolean = false,
    val isUnderline: Boolean = false,
    val isLineThrough: Boolean = false,
    val color: TextColor = TextColor.BASIC,
) {
    constructor(typeText: TypeText) : this(
        id = getUUID(),
        typeText = typeText,
        isBold = typeText.isBold
    )

    fun getColor(isDarkTheme: Boolean) = if (isDarkTheme) color.darkColor else color.lightColor

    fun duplicate(newFormatTextId: String): FormatText = copy(
        id = getUUID(),
        itemId = newFormatTextId
    )

    fun isBefore(index: Int) = (endIndex - 1) < index

    fun isBetween(index: Int) = index in startIndex..<endIndex

    fun isRightAfter(index: Int) = endIndex == index

    fun isAfter(index: Int) = startIndex > index

    fun shouldDelete(index: Int) = startIndex == index && startIndex == (endIndex - 1)
}