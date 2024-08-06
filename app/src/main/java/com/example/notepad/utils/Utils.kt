package com.example.notepad.utils

import androidx.compose.ui.graphics.Color

fun getColor(hexColor: String): Color {
    return Color(android.graphics.Color.parseColor(hexColor))
}