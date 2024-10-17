package com.example.data.repository.impl

import com.example.data.model.db.NoteItemDb
import com.example.data.repository.NoteItemRepository
import com.example.data.repository.dto.NoteItemDto
import com.example.data.source.local.NoteItemDataSource
import com.example.model.entities.NoteItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NoteItemRepositoryImpl @Inject constructor(
    private val noteItemDataSource: NoteItemDataSource,
    private val dispatcher: CoroutineDispatcher,
) : NoteItemRepository {
    override suspend fun deleteNoteItem(noteItem: NoteItem) {
       withContext(dispatcher){
           noteItemDataSource.deleteNoteItem(noteItem)
       }
    }
}