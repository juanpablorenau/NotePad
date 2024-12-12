package com.example.data.repository.impl

import com.example.data.repository.NoteRepository
import com.example.data.repository.dto.NoteDto
import com.example.data.source.local.NoteDataSource
import com.example.model.entities.Note
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val noteDataSource: NoteDataSource,
    private val dispatcher: CoroutineDispatcher,
    private val noteDto: NoteDto,
) : NoteRepository {

    override fun getNotes(query: String): Flow<List<Note>> {
        val notesFlow = if (query.isEmpty()) noteDataSource.getNotes()
        else noteDataSource.searchNotes(query)

        return notesFlow
            .transform { notes -> emit(notes.map { noteDto.toDomain(it) }) }
            .flowOn(dispatcher)
    }

    override fun getNoteById(id: String): Flow<Note> = flow {
        noteDataSource.getNoteById(id)?.also { emit(noteDto.toDomain(it)) }
            ?: throw Exception("Note $id not found")
    }.flowOn(dispatcher)

    override suspend fun insertNote(note: Note) {
        withContext(dispatcher) { noteDataSource.insertNote(noteDto.toDb(note)) }
    }

    override suspend fun updateNote(note: Note) {
        withContext(dispatcher) { noteDataSource.updateNote(noteDto.toDb(note)) }
    }

    override suspend fun updateEmbeddedNote(note: Note) {
        withContext(dispatcher) { noteDataSource.updateEmbeddedNote(noteDto.toDb(note)) }
    }

    override suspend fun deleteNote(note: Note) {
        withContext(dispatcher) { noteDataSource.deleteNote(note) }
    }
}