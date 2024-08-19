package com.example.domain.usecase.list

import com.example.data.repository.NoteRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteNotesUseCase @Inject constructor(
    private val repository: NoteRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(ids: List<String>) =
        withContext(dispatcher) { repository.deleteNotes(ids) }
}