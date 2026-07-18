package com.nuezgolcontrol.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GastoDao {
    @Query("SELECT * FROM gastos ORDER BY fecha DESC")
    fun observeAll(): Flow<List<Gasto>>

    @Insert
    suspend fun insert(gasto: Gasto)

    @Query("DELETE FROM gastos WHERE id = :id")
    suspend fun deleteById(id: Long)
}
