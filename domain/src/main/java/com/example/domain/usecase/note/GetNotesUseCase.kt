package com.example.domain.usecase.note

import com.example.data.repository.NoteRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetNotesUseCase @Inject constructor(
    private val repository: NoteRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    operator fun invoke() = repository.getNotes()
        .map { notes -> notes.map { note -> note.sortItemsByIndex() } }
        .flowOn(dispatcher)
}