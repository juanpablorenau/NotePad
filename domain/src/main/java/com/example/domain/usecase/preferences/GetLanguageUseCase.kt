package com.example.domain.usecase.preferences

import com.example.data.repository.PreferencesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetLanguageUseCase @Inject constructor(
    private val repository: PreferencesRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    operator fun invoke() = repository.getLanguage().flowOn(dispatcher)
}