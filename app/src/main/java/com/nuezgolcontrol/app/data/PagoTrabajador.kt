package com.nuezgolcontrol.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pago_trabajador")
data class PagoTrabajador(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val trabajadorNombre: String,
    val tipoPago: String = "Kilo", // "Kilo" o "Raya"
    val kilos: Double = 0.0,
    val precioPorKilo: Double = 0.0,
    val montoRaya: Double = 0.0,
    val fecha: Long = System.currentTimeMillis()
) {
    val total: Double
        get() = if (tipoPago == "Raya") montoRaya else kilos * precioPorKilo
}
