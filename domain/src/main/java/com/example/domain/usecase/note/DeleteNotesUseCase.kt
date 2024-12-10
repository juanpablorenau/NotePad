package com.example.domain.usecase.note

import com.example.model.entities.Note
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteNotesUseCase @Inject constructor(
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(notes: List<Note>) =
        withContext(dispatcher) {
            notes.map { note ->
                launch {
                    if (note.isChecked) deleteNoteUseCase(note)
                    else updateNoteUseCase(note)
                }
            }.joinAll()
        }
}