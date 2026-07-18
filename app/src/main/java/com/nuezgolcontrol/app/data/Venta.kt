package com.nuezgolcontrol.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "venta")
data class Venta(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val fechaMillis: Long = System.currentTimeMillis(),
    val cliente: String,
    val tipoNuez: String,
    val cantidad: Double,
    val precioUnitario: Double
) {
    val total: Double get() = cantidad * precioUnitario
}
