package com.example.model.utils

import java.text.Normalizer


fun String?.orEmptyString() = this ?: ""

fun String.normalize() =
    Normalizer.normalize(this, Normalizer.Form.NFD).replace("\\p{M}".toRegex(), "")

