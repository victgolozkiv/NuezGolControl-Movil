package com.nuezgolcontrol.app.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface VentaDao {
    @Query("SELECT * FROM venta ORDER BY fechaMillis DESC")
    fun observeAll(): Flow<List<Venta>>

    @Query("SELECT * FROM venta ORDER BY fechaMillis DESC")
    suspend fun getAll(): List<Venta>

    @Insert
    suspend fun insert(venta: Venta): Long

    @Delete
    suspend fun delete(venta: Venta)

    @Query("DELETE FROM venta WHERE id = :id")
    suspend fun deleteById(id: Long)
}
