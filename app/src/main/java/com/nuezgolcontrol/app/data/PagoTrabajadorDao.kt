package com.nuezgolcontrol.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PagoTrabajadorDao {
    @Insert
    suspend fun insert(pago: PagoTrabajador)

    @Query("SELECT * FROM pago_trabajador ORDER BY fecha DESC")
    fun observeAll(): Flow<List<PagoTrabajador>>

    @Query("DELETE FROM pago_trabajador WHERE id = :id")
    suspend fun deleteById(id: Long)
}
