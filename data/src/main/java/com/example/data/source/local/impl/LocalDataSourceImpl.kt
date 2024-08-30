package com.example.data.source.local.impl

import com.example.data.model.db.NoteDb
import com.example.data.source.local.LocalDataSource
import com.example.data.source.local.dao.NoteDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.invoke
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val noteDao: NoteDao,
    private val dispatcher: CoroutineDispatcher,
) : LocalDataSource {

    override suspend fun getNotes(): List<NoteDb> =
        dispatcher { noteDao.getNotes() }

    override suspend fun getNoteById(id: String): NoteDb? =
        dispatcher { noteDao.getNoteById(id) }

    override suspend fun insertNote(note: NoteDb) {
        dispatcher { noteDao.insertNote(note) }
    }

    override suspend fun updateNote(note: NoteDb) {
        dispatcher { noteDao.updateNote(note) }
    }

    override suspend fun deleteNote(id: String) {
        dispatcher { noteDao.deleteNote(id) }
    }

    override suspend fun deleteNoteItem(id: String) {
        dispatcher { noteDao.deleteNoteItem(id) }
    }
}