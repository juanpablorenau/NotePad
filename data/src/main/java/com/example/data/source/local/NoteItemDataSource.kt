package com.example.data.source.local;

import com.example.data.model.db.NoteItemDb
import com.example.model.entities.NoteItem

interface NoteItemDataSource {
    suspend fun insertNoteItem(noteItem: NoteItemDb)
    suspend fun deleteNoteItem(noteItem: NoteItem)
}
