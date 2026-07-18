package com.nuezgolcontrol.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gastos")
data class Gasto(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val concepto: String,       // Ej: "Fertilizante", "Pago agua", etc.
    val monto: Double,
    val fecha: Long = System.currentTimeMillis()
)
