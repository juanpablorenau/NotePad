package com.example.domain.usecase.note

import com.example.data.repository.NoteRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetNoteUseCase @Inject constructor(
    private val repository: NoteRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    operator fun invoke(id: String) = repository.getNoteById(id).map { note ->
        note.copy(items = note.items.sortedBy { it.index })
    }.flowOn(dispatcher)
}