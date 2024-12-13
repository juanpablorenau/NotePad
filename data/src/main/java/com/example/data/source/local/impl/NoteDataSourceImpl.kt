package com.example.data.source.local.impl

import com.example.data.model.db.NoteDb
import com.example.data.source.local.NoteDataSource
import com.example.data.source.local.NoteItemDataSource
import com.example.data.source.local.dao.NoteDao
import com.example.data.utils.TransactionProvider
import com.example.model.entities.Note
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NoteDataSourceImpl @Inject constructor(
    private val noteDao: NoteDao,
    private val noteItemDataSource: NoteItemDataSource,
    private val transactionProvider: TransactionProvider,
) : NoteDataSource {

    override fun getNotes(): Flow<List<NoteDb>> = noteDao.getNotes()

    override fun searchNotes(query: String): Flow<List<NoteDb>> = noteDao.searchNotes(query)

    override suspend fun getNoteById(id: String): NoteDb? = noteDao.getNoteById(id)

    override suspend fun insertNote(note: NoteDb) {
        transactionProvider.runAsTransaction {
            noteDao.insertNote(note.note)
            note.items.forEach { currentItem ->
                noteItemDataSource.insertNoteItem(currentItem)
            }
        }
    }

    override suspend fun updateNote(note: NoteDb) {
        transactionProvider.runAsTransaction {
            noteDao.updateNote(note.note)
            note.items.forEach { currentItem ->
                noteItemDataSource.insertNoteItem(currentItem)
            }
        }
    }

    override suspend fun updateEmbeddedNote(note: NoteDb) {
        noteDao.updateNote(note.note)
    }

    override suspend fun deleteNote(note: Note) {
        transactionProvider.runAsTransaction {
            noteDao.deleteNote(note.id)
            note.items.forEach { currentItem ->
                noteItemDataSource.deleteNoteItem(currentItem)
            }
        }
    }
}