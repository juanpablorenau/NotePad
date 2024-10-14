package com.example.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.db.NoteItemEmbeddedDb

@Dao
interface NoteItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNoteItem(noteItem: NoteItemEmbeddedDb)

    @Update
    suspend fun updateNoteItem(noteItem: NoteItemEmbeddedDb)

    @Query("DELETE FROM NoteItems WHERE id = :id")
    suspend fun deleteNoteItem(id: String)
}