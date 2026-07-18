package com.nuezgolcontrol.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trabajadores")
data class Trabajador(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String
)
