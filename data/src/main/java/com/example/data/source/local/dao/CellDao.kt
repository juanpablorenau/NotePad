package com.example.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.db.CellEmbeddedDb


@Dao
interface CellDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCell(cell: CellEmbeddedDb)

    @Update
    suspend fun updateCell(cell: CellEmbeddedDb)

    @Query("DELETE FROM Cells WHERE id = :id")
    suspend fun deleteCell(id: String)
}