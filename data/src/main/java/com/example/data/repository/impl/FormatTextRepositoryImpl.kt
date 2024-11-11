package com.example.data.repository.impl

import com.example.data.repository.FormatTextRepository
import com.example.data.source.local.FormatTextDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FormatTextRepositoryImpl @Inject constructor(
    private val formatTextDataSource: FormatTextDataSource,
    private val dispatcher: CoroutineDispatcher,
) : FormatTextRepository {

    override suspend fun deleteFormatText(formatTextId: String) {
        withContext(dispatcher) { formatTextDataSource.deleteFormatText(formatTextId) }
    }
}