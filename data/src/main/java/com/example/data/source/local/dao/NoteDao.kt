package com.example.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.data.model.db.NoteDb
import com.example.data.model.db.NoteEmbeddedDb
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Transaction
    @Query("SELECT * FROM Notes")
    fun getNotes(): Flow<List<NoteDb>>

    @Transaction
    @Query("SELECT * FROM Notes WHERE id = :id")
    suspend fun getNoteById(id: String): NoteDb?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEmbeddedDb)

    @Update
    suspend fun updateNote(note: NoteEmbeddedDb)

    @Query("DELETE FROM Notes WHERE id = :id")
    suspend fun deleteNote(id: String)

    @Transaction
    @Query(
        "SELECT * FROM Notes " +
                "WHERE title LIKE '%' || :query || '%'" +
                "OR id IN (" +
                "SELECT noteId FROM NoteItems WHERE text LIKE '%' || :query || '%'" +
                "OR id IN (" +
                    " SELECT noteItemId FROM Tables" +
                    " WHERE id IN (" +
                    "SELECT tableId FROM Cells WHERE text LIKE '%' || :query || '%'))" +
                ")"
    )
    fun searchNotes(query: String): Flow<List<NoteDb>>
}