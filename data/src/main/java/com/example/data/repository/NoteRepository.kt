package com.example.data.repository

import com.example.model.entities.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getNotes(query: String): Flow<List<Note>>
    fun getNoteById(id: String): Flow<Note>
    suspend fun insertNote(note: Note)
    suspend fun updateNote(note: Note)
    suspend fun updateEmbeddedNote(note: Note)
    suspend fun deleteNote(note: Note)
}