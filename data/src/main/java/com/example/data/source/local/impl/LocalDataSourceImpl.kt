package com.example.data.source.local.impl

import com.example.data.model.db.NoteDbModel
import com.example.data.source.local.LocalDataSource
import com.example.data.source.local.dao.NotePadDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.invoke
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val notePadDao: NotePadDao,
    private val dispatcher: CoroutineDispatcher,
) : LocalDataSource {

    override suspend fun getNotes(): List<NoteDbModel> =
        dispatcher { notePadDao.getNotes() }

    override suspend fun getNoteById(id: String): NoteDbModel? =
        dispatcher { notePadDao.getNoteById(id) }

    override suspend fun insertNote(note: NoteDbModel) {
        dispatcher { notePadDao.insertNote(note) }
    }

    override suspend fun updateNote(note: NoteDbModel) {
        dispatcher { notePadDao.updateNote(note) }
    }

    override suspend fun deleteNote(id: String) {
        dispatcher { notePadDao.deleteNote(id) }
    }
}