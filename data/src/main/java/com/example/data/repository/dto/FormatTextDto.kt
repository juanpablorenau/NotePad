package com.example.data.repository.dto

import com.example.data.model.db.FormatTextDb
import com.example.model.entities.FormatText
import com.example.model.entities.ParagraphType
import com.example.model.entities.TypeText
import javax.inject.Inject

class FormatTextDto @Inject constructor(
    private val typeTextDto: TypeTextDto,
    private val paragraphTypeDto: ParagraphTypeDto,
) {

    fun toDomain(formatTextDb: FormatTextDb) =
        FormatText(
            formatTextId = formatTextDb.formatTextId,
            typeText = typeTextDto.toDomain(formatTextDb.typeText),
            fontSize = formatTextDb.fontSize,
            isBold = formatTextDb.isBold,
            isItalic = formatTextDb.isItalic,
            isUnderline = formatTextDb.isUnderline,
            isLineThrough = formatTextDb.isLineThrough,
            textLightColor = formatTextDb.textDarkColor,
            textDarkColor = formatTextDb.textDarkColor,
            paragraphType = paragraphTypeDto.toDomain(formatTextDb.paragraphType)
        )

    fun toDb(formatText: FormatText) =
        FormatTextDb(
            formatTextId = formatText.formatTextId,
            typeText = formatText.typeText.name,
            fontSize = formatText.fontSize,
            isBold = formatText.isBold,
            isItalic = formatText.isItalic,
            isUnderline = formatText.isUnderline,
            isLineThrough = formatText.isLineThrough,
            textLightColor = formatText.textLightColor,
            textDarkColor = formatText.textDarkColor,
            paragraphType = formatText.paragraphType.name
        )
}

class TypeTextDto @Inject constructor() {
    fun toDomain(type: String): TypeText =
        when (type) {
            TypeText.TITLE.name -> TypeText.TITLE
            TypeText.HEADER.name -> TypeText.HEADER
            TypeText.SUBTITLE.name -> TypeText.SUBTITLE
            else -> TypeText.BODY
        }
}

class ParagraphTypeDto @Inject constructor() {
    fun toDomain(type: String): ParagraphType =
        when (type) {
            ParagraphType.LEFT.name -> ParagraphType.LEFT
            ParagraphType.CENTER.name -> ParagraphType.CENTER
            ParagraphType.RIGHT.name -> ParagraphType.RIGHT
            else -> ParagraphType.JUSTIFY
        }
}