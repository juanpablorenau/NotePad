package com.example.data.source.local.impl

import com.example.data.model.db.NoteItemDb
import com.example.data.model.db.NoteItemEmbeddedDb
import com.example.data.source.local.FormatTextDataSource
import com.example.data.source.local.NoteItemDataSource
import com.example.data.source.local.TableDataSource
import com.example.data.source.local.dao.NoteItemDao
import com.example.data.utils.tryRoom
import javax.inject.Inject

class NoteItemDataSourceImpl @Inject constructor(
    private val noteItemDao: NoteItemDao,
    private val formatTextDataSource: FormatTextDataSource,
    private val tableDataSource: TableDataSource,
) : NoteItemDataSource {

    override suspend fun insertNoteItem(noteItem: NoteItemDb) {
        tryRoom(this.toString()) { noteItemDao.insertNoteItem(noteItem.noteItem) }
        if (noteItem.table != null) tableDataSource.insertTable(noteItem.table)
        formatTextDataSource.insertFormatText(noteItem.formatText)
    }

    override suspend fun deleteNoteItem(noteItem: NoteItemEmbeddedDb) {
    }
}