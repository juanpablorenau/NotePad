package com.example.domain.usecase.preferences

import com.example.data.repository.PreferencesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetColumnsCountUseCase @Inject constructor(
    private val repository: PreferencesRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(columnsCount: Int) =
        withContext(dispatcher) { repository.setColumnsCount(columnsCount) }
}