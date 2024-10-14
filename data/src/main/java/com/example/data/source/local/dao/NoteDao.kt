package com.example.data.source.local.dao

import androidx.room.*
import com.example.data.model.db.NoteDb
import com.example.data.model.db.NoteEmbeddedDb

@Dao
interface NoteDao {

    @Transaction
    @Query("SELECT * FROM Notes")
    suspend fun getNotes(): List<NoteDb>

    @Transaction
    @Query("SELECT * FROM Notes WHERE id = :id")
    suspend fun getNoteById(id: String): NoteDb?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEmbeddedDb)

    @Update
    suspend fun updateNote(note: NoteEmbeddedDb)

    @Query("DELETE FROM Notes WHERE id = :id")
    suspend fun deleteNote(id: String)
}