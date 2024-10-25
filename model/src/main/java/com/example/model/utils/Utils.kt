package com.example.model.utils

fun getUUID() = java.util.UUID.randomUUID().toString()

fun Boolean?.orFalse() = this ?: false