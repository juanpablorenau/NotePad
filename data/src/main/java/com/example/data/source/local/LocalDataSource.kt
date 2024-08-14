package com.example.data.source.local

import com.example.data.model.db.NoteDbModel

interface LocalDataSource {

    suspend fun getNotes(): List<NoteDbModel>
    suspend fun getNoteById(id: String): NoteDbModel?
    suspend fun insertNote(note: NoteDbModel)
    suspend fun updateNote(note: NoteDbModel)
    suspend fun deleteNote(id: String)
}