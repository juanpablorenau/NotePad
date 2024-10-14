package com.example.data.source.local.impl

import com.example.data.model.db.NoteItemDb
import com.example.data.model.db.NoteItemEmbeddedDb
import com.example.data.source.local.FormatTextDataSource
import com.example.data.source.local.NoteItemDataSource
import com.example.data.source.local.TableDataSource
import com.example.data.source.local.dao.NoteItemDao
import javax.inject.Inject

class NoteItemDataSourceImpl @Inject constructor(
    private val noteItemDao: NoteItemDao,
    private val formatTextDataSource: FormatTextDataSource,
    private val tableDataSource: TableDataSource,
) : NoteItemDataSource {

    override suspend fun insertNoteItem(noteItem: NoteItemDb) {
        noteItemDao.insertNoteItem(noteItem.noteItem)
        formatTextDataSource.insertFormatText(noteItem.formatText)
        if (noteItem.table != null) tableDataSource.insertTable(noteItem.table)
    }

    override suspend fun deleteNoteItem(noteItem: NoteItemEmbeddedDb) {
    }
}