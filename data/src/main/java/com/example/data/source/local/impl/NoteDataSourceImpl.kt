package com.example.data.source.local.impl

import androidx.room.Transaction
import com.example.data.model.db.NoteDb
import com.example.data.source.local.NoteDataSource
import com.example.data.source.local.NoteItemDataSource
import com.example.data.source.local.dao.NoteDao
import com.example.model.entities.Note
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.invoke
import javax.inject.Inject

class NoteDataSourceImpl @Inject constructor(
    private val noteDao: NoteDao,
    private val noteItemDataSource: NoteItemDataSource,
    private val dispatcher: CoroutineDispatcher,
) : NoteDataSource {

    override suspend fun getNotes(): List<NoteDb> = dispatcher { noteDao.getNotes() }

    override suspend fun getNoteById(id: String): NoteDb? = dispatcher { noteDao.getNoteById(id) }

    @Transaction
    override suspend fun insertNote(note: NoteDb) {
        dispatcher {
            noteDao.insertNote(note.note)
            note.items.forEach { currentItem ->
                noteItemDataSource.insertNoteItem(currentItem)
            }
        }
    }

    @Transaction
    override suspend fun updateNote(note: NoteDb) {
        dispatcher {
            noteDao.updateNote(note.note)
            note.items.forEach { currentItem ->
                noteItemDataSource.insertNoteItem(currentItem)
            }
        }
    }

    override suspend fun deleteNote(note: Note) {
        dispatcher {
            noteDao.deleteNote(note.id)
            note.items.forEach { currentItem ->
                noteItemDataSource.deleteNoteItem(currentItem)
            }
        }
    }
}