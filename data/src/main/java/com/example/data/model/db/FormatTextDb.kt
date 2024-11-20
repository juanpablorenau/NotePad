package com.example.data.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FormatTexts")
data class FormatTextDb(
    @PrimaryKey val id: String = "",

    @ColumnInfo(name = "itemId") val itemId: String = "",
    @ColumnInfo(name = "startIndex") val startIndex: Int = 0,
    @ColumnInfo(name = "endIndex") val endIndex: Int = 0,
    @ColumnInfo(name = "typeText") val typeText: String = "",
    @ColumnInfo(name = "fontSize") val fontSize: Int = 16,
    @ColumnInfo(name = "isBold") val isBold: Boolean = false,
    @ColumnInfo(name = "isItalic") val isItalic: Boolean = false,
    @ColumnInfo(name = "isUnderline") val isUnderline: Boolean = false,
    @ColumnInfo(name = "isLineThrough") val isLineThrough: Boolean = false,
    @ColumnInfo(name = "color") val color: String = "",
)
