package com.example.domain.usecase.note

import com.example.data.repository.NoteRepository
import com.example.model.entities.Note
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetNotesUseCase @Inject constructor(
    private val repository: NoteRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    operator fun invoke(query: String) =
        repository.getNotes(query)
            .map { notes -> getSortedNotes(notes) }
            .flowOn(dispatcher)


    private fun getSortedNotes(notes: List<Note>): List<Note> {
        val (pinNotes, unPinNotes) = notes.partition { it.isPinned }
        return pinNotes.sortedBy { it.index } + unPinNotes.sortedBy { it.index }
    }
}