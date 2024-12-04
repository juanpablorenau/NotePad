package com.example.model.utils

import java.text.Normalizer

fun String.normalize() =
    Normalizer.normalize(this, Normalizer.Form.NFD).replace("\\p{M}".toRegex(), "")

