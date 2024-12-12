package com.example.data.source.local

import com.example.data.model.db.NoteDb
import com.example.model.entities.Note
import kotlinx.coroutines.flow.Flow

interface NoteDataSource {

    fun getNotes(): Flow<List<NoteDb>>
    fun searchNotes(query: String): Flow<List<NoteDb>>
    suspend fun getNoteById(id: String): NoteDb?
    suspend fun insertNote(note: NoteDb)
    suspend fun updateNote(note: NoteDb)
    suspend fun updateEmbeddedNote(note: NoteDb)
    suspend fun deleteNote(note: Note)
}