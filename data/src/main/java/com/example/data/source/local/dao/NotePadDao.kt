package com.example.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.db.NoteDbModel

@Dao
interface NotePadDao {

    @Query("SELECT * FROM Notes")
    suspend fun getNotes(): List<NoteDbModel>

    @Query("SELECT * FROM Notes WHERE id = :id")
    suspend fun getNoteById(id: String): NoteDbModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: NoteDbModel)

    @Update
    fun updateNote(note: NoteDbModel)

    @Query("DELETE FROM Notes WHERE id = :id")
    suspend fun deleteNote(id: String)
}