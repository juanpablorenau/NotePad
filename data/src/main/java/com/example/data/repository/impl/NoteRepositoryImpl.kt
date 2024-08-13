package com.example.data.repository.impl

import com.example.data.repository.NoteRepository
import com.example.data.repository.dto.NoteDto
import com.example.data.source.local.LocalDataSource
import com.example.model.entities.Note
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val dispatcher: CoroutineDispatcher,
    private val noteDto: NoteDto,
) : NoteRepository {

    override fun getNotes(): Flow<List<Note>> = flow {
        localDataSource.getNotes().map { noteDto.toDomain(it) }.also { emit(it) }
    }.flowOn(dispatcher)

    override fun getNoteById(id: String): Flow<Note> = flow {
        localDataSource.getNoteById(id)?.also { emit(noteDto.toDomain(it)) }
            ?: throw Exception("Note $id not found")
    }.flowOn(dispatcher)

    override suspend fun insertNote(note: Note) {
        withContext(dispatcher) { localDataSource.insertNote(noteDto.toDb(note)) }
    }

    override suspend fun updateNote(note: Note) {
        withContext(dispatcher) { localDataSource.updateNote(noteDto.toDb(note)) }
    }

    override suspend fun deleteNote(id: String) {
        withContext(dispatcher) { localDataSource.deleteNote(id) }
    }
}