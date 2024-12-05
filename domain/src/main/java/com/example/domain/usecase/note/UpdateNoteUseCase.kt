package com.example.domain.usecase.note

import com.example.data.repository.NoteRepository
import com.example.model.entities.Note
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateNoteUseCase @Inject constructor(
    private val repository: NoteRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(note: Note) =
        withContext(dispatcher) {
            repository.updateNote(
                note
                    .removeFocus()
                    .setCursorOnLastPosition()
            )
        }
}