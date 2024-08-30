package com.example.data.repository

import com.example.model.entities.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getNoteById(id: String): Flow<Note>
    fun getNotes(): Flow<List<Note>>
    suspend fun insertNote(note: Note)
    suspend fun updateNote(note: Note)
    suspend fun updateNotes(notes: List<Note>)
    suspend fun deleteNote(id: String)
    suspend fun deleteNotes(ids: List<String>)
    suspend fun deleteNoteItem(id: String)
}