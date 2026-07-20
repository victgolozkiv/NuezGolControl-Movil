package com.nuezgolcontrol.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

data class Transaccion(
    val tipo: String, // "Venta", "Pago", "Gasto"
    val descripcion: String,
    val monto: Double,
    val fecha: Long
)

data class ReporteResumen(
    val periodoInicio: Long,
    val periodoFin: Long,
    val ventasTotales: Int,
    val pagosTotales: Int,
    val gastosTotales: Int,
    val ingresoTotal: Double,
    val egresoTotal: Double,
    val gananciaNet: Double,
    val transacciones: List<Transaccion>
)
