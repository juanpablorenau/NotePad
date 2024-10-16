package com.example.domain.usecase.detail

import com.example.data.repository.NoteRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetNoteDetailUseCase @Inject constructor(
    private val repository: NoteRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    operator fun invoke(id: String) = repository.getNoteById(id).map { note ->
        note.copy(items = note.items.sortedBy { item -> item.index })
    }.flowOn(dispatcher)
}