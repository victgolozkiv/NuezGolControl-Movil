package com.nuezgolcontrol.app.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CosechaDao {
    @Query("SELECT * FROM cosecha ORDER BY fechaMillis DESC")
    fun observeAll(): Flow<List<Cosecha>>

    @Query("SELECT * FROM cosecha ORDER BY fechaMillis DESC")
    suspend fun getAll(): List<Cosecha>

    @Insert
    suspend fun insert(cosecha: Cosecha): Long

    @Delete
    suspend fun delete(cosecha: Cosecha)

    @Query("DELETE FROM cosecha WHERE id = :id")
    suspend fun deleteById(id: Long)
}
