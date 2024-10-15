package com.example.data.source.local

import com.example.data.model.db.NoteDb
import com.example.model.entities.Note

interface NoteDataSource {

    suspend fun getNotes(): List<NoteDb>
    suspend fun getNoteById(id: String): NoteDb?
    suspend fun insertNote(note: NoteDb)
    suspend fun updateNote(note: NoteDb)
    suspend fun deleteNote(note: Note)
}