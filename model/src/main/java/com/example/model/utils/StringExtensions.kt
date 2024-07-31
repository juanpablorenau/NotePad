package com.example.model.utils


fun String?.orEmptyString() = this ?: ""

fun String.addQuotationMarks() =  " \"$this\""

fun String.uppercaseFirstChar() = replaceFirstChar { firstChar -> firstChar.uppercase() }

fun String.visualFormat(delimiter: String) =
    split(delimiter).joinToString(" ") { string -> string.uppercaseFirstChar() }

fun String.abbreviate(n: Int) =
    split(" ").joinToString(". ") { it.take(n) }.plus(".")