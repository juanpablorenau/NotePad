package com.example.data.utils

import android.util.Log

suspend fun tryRoom(message: String, block: suspend () -> Unit) {
    try {
        block()
    } catch (e: Exception) {
        Log.e("ROOM ERROR", "In $message error: $e")
    }
}