package com.example.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.db.TableEmbeddedDb

@Dao
interface TableDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTable(table: TableEmbeddedDb)

    @Update
    suspend fun updateTable(table: TableEmbeddedDb)

    @Query("DELETE FROM Tables WHERE id = :id")
    suspend fun deleteTable(id: String)
}