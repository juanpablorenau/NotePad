package com.example.model.utils

import java.text.Normalizer


fun String?.orEmptyString() = this ?: ""

fun String.normalize() =
    Normalizer.normalize(this, Normalizer.Form.NFD).replace("\\p{M}".toRegex(), "")

fun String.capitalizeFirstLetter() =
    if (this.isNotEmpty()) this[0].uppercaseChar() + this.substring(1).lowercase()
    else this

