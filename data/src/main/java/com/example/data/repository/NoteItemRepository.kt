package com.example.data.repository

import com.example.model.entities.NoteItem

interface NoteItemRepository {
    suspend fun deleteNoteItem(noteItem: NoteItem)
}