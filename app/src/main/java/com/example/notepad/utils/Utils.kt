package com.example.notepad.utils

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.ui.graphics.Color
import androidx.palette.graphics.Palette
import coil.request.SuccessResult

private fun getPaletteFrom(result: SuccessResult): Palette {
    val bitmap = (result.drawable as BitmapDrawable).bitmap
        .copy(Bitmap.Config.ARGB_8888, false)
    return Palette.from(bitmap).generate()
}

fun getDominantColor(result: SuccessResult): Int =
    getPaletteFrom(result).getDominantColor(Color.Black.value.toInt())

fun getSecondDominantColor(result: SuccessResult): Int =
    getPaletteFrom(result).swatches.getOrNull(1)?.rgb ?: getDominantColor(result)

