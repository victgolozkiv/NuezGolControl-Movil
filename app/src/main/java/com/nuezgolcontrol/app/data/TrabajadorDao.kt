package com.nuezgolcontrol.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TrabajadorDao {
    @Query("SELECT * FROM trabajadores ORDER BY nombre ASC")
    fun observeAll(): Flow<List<Trabajador>>

    @Query("SELECT nombre FROM trabajadores ORDER BY nombre ASC")
    suspend fun getAllNombres(): List<String>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(trabajador: Trabajador)

    @Query("DELETE FROM trabajadores WHERE nombre = :nombre")
    suspend fun deleteByName(nombre: String)

    @Query("SELECT COUNT(*) FROM trabajadores WHERE nombre = :nombre")
    suspend fun exists(nombre: String): Int
}
