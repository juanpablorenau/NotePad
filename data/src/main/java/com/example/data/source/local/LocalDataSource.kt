package com.example.data.source.local

import com.example.data.model.db.NoteDb

interface LocalDataSource {

    suspend fun getNotes(): List<NoteDb>
    suspend fun getNoteById(id: String): NoteDb?
    suspend fun insertNote(note: NoteDb)
    suspend fun updateNote(note: NoteDb)
    suspend fun deleteNote(id: String)
    suspend fun deleteNoteItem(id: String)
}