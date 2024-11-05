package com.example.model.entities

import com.example.model.enums.ParagraphType
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
    val paragraphType: ParagraphType = ParagraphType.LEFT,
) {
    constructor(typeText: TypeText) : this(
        id = getUUID(),
        typeText = typeText,
        isBold = typeText.isBold
    )

    fun duplicate(newFormatTextId: String): FormatText = copy(
        id = getUUID(),
        itemId = newFormatTextId
    )
}