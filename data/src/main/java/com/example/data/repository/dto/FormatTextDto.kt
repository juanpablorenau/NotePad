package com.example.data.repository.dto

import com.example.data.model.db.FormatTextDb
import com.example.model.entities.FormatText
import com.example.model.enums.ParagraphType
import com.example.model.enums.TextColor
import com.example.model.enums.TypeText
import javax.inject.Inject

class FormatTextDto @Inject constructor() {

    fun toDomain(formatTextDb: FormatTextDb) =
        FormatText(
            id = formatTextDb.id,
            itemId = formatTextDb.itemId,
            startIndex = formatTextDb.startIndex,
            endIndex = formatTextDb.endIndex,
            typeText = TypeText.valueOf(formatTextDb.typeText),
            isBold = formatTextDb.isBold,
            isItalic = formatTextDb.isItalic,
            isUnderline = formatTextDb.isUnderline,
            isLineThrough = formatTextDb.isLineThrough,
            color = TextColor.valueOf(formatTextDb.color),
            paragraphType = ParagraphType.valueOf(formatTextDb.paragraphType)
        )

    fun toDb(formatText: FormatText) =
        FormatTextDb(
            id = formatText.id,
            itemId = formatText.itemId,
            startIndex = formatText.startIndex,
            endIndex = formatText.endIndex,
            typeText = formatText.typeText.name,
            isBold = formatText.isBold,
            isItalic = formatText.isItalic,
            isUnderline = formatText.isUnderline,
            isLineThrough = formatText.isLineThrough,
            color = formatText.color.name,
            paragraphType = formatText.paragraphType.name
        )
}