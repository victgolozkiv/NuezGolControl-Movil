package com.nuezgolcontrol.app

import android.app.Application
import com.nuezgolcontrol.app.data.NuezDatabase
import com.nuezgolcontrol.app.data.NuezRepository

class NuezGolApplication : Application() {
    lateinit var repository: NuezRepository
        private set

    override fun onCreate() {
        super.onCreate()
        val db = NuezDatabase.getInstance(this)
        repository = NuezRepository(
            db.ventaDao(),
            db.cosechaDao(),
            db.pagoTrabajadorDao(),
            db.trabajadorDao(),
            db.gastoDao()
        )
    }
}
