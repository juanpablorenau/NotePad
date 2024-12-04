package com.example.domain.usecase.note

import com.example.model.entities.Note
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateNotesUseCase @Inject constructor(
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(notes: List<Note>) =
        withContext(dispatcher) {
            notes.map { async { updateNoteUseCase(it) } }.awaitAll()
        }
}