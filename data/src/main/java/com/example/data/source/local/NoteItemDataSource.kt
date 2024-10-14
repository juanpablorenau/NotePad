package com.example.data.source.local;

import com.example.data.model.db.NoteItemDb
import com.example.data.model.db.NoteItemEmbeddedDb

interface NoteItemDataSource {
    suspend fun insertNoteItem(noteItem: NoteItemDb)
    suspend fun updateNoteItem(noteItem: NoteItemDb)
    suspend fun deleteNoteItem(noteItem: NoteItemEmbeddedDb)
}
