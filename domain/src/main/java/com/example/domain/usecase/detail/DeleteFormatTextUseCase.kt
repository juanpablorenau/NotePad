package com.example.domain.usecase.detail

import com.example.data.repository.FormatTextRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteFormatTextUseCase @Inject constructor(
    private val repository: FormatTextRepository,
    private val dispatcher: CoroutineDispatcher,
) {

    suspend operator fun invoke(formatTextId: String) =
        withContext(dispatcher) { repository.deleteFormatText(formatTextId) }
}