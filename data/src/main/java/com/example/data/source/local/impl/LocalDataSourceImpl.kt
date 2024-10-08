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
        dispatcher { listOf() }

    override suspend fun getNoteById(id: String): NoteDb? =
        dispatcher {null }

    override suspend fun insertNote(note: NoteDb) {
        dispatcher {  }
    }

    override suspend fun updateNote(note: NoteDb) {
        dispatcher {  }
    }

    override suspend fun deleteNote(id: String) {
        dispatcher {  }
    }

    override suspend fun deleteNoteItem(id: String) {
        dispatcher {  }
    }
}