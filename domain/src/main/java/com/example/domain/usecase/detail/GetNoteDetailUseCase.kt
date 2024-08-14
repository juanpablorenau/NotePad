package com.example.domain.usecase.detail

import com.example.data.repository.NoteRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetNoteDetailUseCase @Inject constructor(
    private val repository: NoteRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    operator fun invoke(id: String) = repository.getNoteById(id).flowOn(dispatcher)
}