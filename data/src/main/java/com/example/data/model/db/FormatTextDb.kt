package com.example.data.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FormatTexts")
data class FormatTextDb(
    @PrimaryKey val formatTextId: String = "",

    @ColumnInfo(name = "typeText") val typeText: String = "",
    @ColumnInfo(name = "fontSize") val fontSize: Int = 16,
    @ColumnInfo(name = "isBold") val isBold: Boolean = false,
    @ColumnInfo(name = "isItalic") val isItalic: Boolean = false,
    @ColumnInfo(name = "isUnderline") val isUnderline: Boolean = false,
    @ColumnInfo(name = "isLineThrough") val isLineThrough: Boolean = false,
    @ColumnInfo(name = "textLightColor") val textLightColor: String = "",
    @ColumnInfo(name = "textDarkColor") val textDarkColor: String = "",
    @ColumnInfo(name = "paragraphType") val paragraphType: String = "",
)
