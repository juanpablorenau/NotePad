package com.example.domain.usecase.detail

import com.example.data.repository.NoteItemRepository
import com.example.model.entities.NoteItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteNoteItemUseCase @Inject constructor(
    private val repository: NoteItemRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(noteItem: NoteItem) =
        withContext(dispatcher) { repository.deleteNoteItem(noteItem) }
}