package com.example.data.source.local.dao

import androidx.room.Dao

@Dao
interface NoteDao {

/*    @Transaction
    @Query("SELECT * FROM Notes")
    suspend fun getNotes(): List<NoteDb>

    @Transaction
    @Query("SELECT * FROM Notes WHERE id = :id")
    suspend fun getNoteById(id: String): NoteDb?

    @Transaction
    suspend fun insertNote(note: NoteDb){
        insertNoteEmbedded(note.note)
        insertNoteItems(note.items)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNoteEmbedded(note: NoteEmbeddedDb)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNoteItems(noteItems: List<NoteItemDb>)

    @Transaction
    suspend fun updateNote(note: NoteDb) {
        updateNoteEmbedded(note.note)
        insertNoteItems(note.items)
    }

    @Update
    suspend fun updateNoteEmbedded(note: NoteEmbeddedDb)

    @Transaction
    suspend fun deleteNote(id: String) {
        deleteNoteEmbedded(id)
        deleteNoteItems(id)
    }

    @Query("DELETE FROM Notes WHERE id = :id")
    suspend fun deleteNoteEmbedded(id: String)

    @Query("DELETE FROM NoteItems WHERE noteId IN (:noteId)")
    suspend fun deleteNoteItems(noteId: String)

    @Query("DELETE FROM NoteItems WHERE id = :id")
    suspend fun deleteNoteItem(id: String)*/
}