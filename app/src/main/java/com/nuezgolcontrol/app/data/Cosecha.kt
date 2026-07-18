package com.nuezgolcontrol.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cosecha")
data class Cosecha(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val fechaMillis: Long = System.currentTimeMillis(),
    val tipoNuez: String,
    val cantidadKg: Double
)
