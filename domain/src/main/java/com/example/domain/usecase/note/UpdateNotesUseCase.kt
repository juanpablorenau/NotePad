package com.example.domain.usecase.note

import com.example.data.repository.NoteRepository
import com.example.model.entities.Note
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateNotesUseCase @Inject constructor(
    private val repository: NoteRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(notes: List<Note>) =
        withContext(dispatcher) {
            notes.map { note -> async { repository.updateEmbeddedNote(note) } }.awaitAll()
        }
}