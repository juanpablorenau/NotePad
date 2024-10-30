package com.example.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.db.FormatTextDb

@Dao
interface FormatTextDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFormatText(formatText: FormatTextDb)

    @Query("DELETE FROM FormatTexts WHERE id = :id")
    suspend fun deleteFormatText(id: String)
}