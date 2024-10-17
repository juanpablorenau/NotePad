package com.example.domain.usecase.detail

import com.example.data.repository.NoteRepository
import com.example.model.entities.Note
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private val repository: NoteRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(note: Note) = withContext(dispatcher) { repository.deleteNote(note) }
}