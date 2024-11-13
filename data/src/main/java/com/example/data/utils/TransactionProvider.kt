package com.example.data.utils

import androidx.room.withTransaction
import com.example.data.source.local.dao.AppDatabase

class TransactionProvider(private val db: AppDatabase) {

    suspend fun <R> runAsTransaction(block: suspend () -> R): R = db.withTransaction(block)
}