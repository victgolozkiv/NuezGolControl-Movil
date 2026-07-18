package com.nuezgolcontrol.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [Venta::class, Cosecha::class, PagoTrabajador::class, Trabajador::class, Gasto::class],
    version = 4,
    exportSchema = false
)
abstract class NuezDatabase : RoomDatabase() {
    abstract fun ventaDao(): VentaDao
    abstract fun cosechaDao(): CosechaDao
    abstract fun pagoTrabajadorDao(): PagoTrabajadorDao
    abstract fun trabajadorDao(): TrabajadorDao
    abstract fun gastoDao(): GastoDao

    companion object {
        @Volatile
        private var instance: NuezDatabase? = null

        val MIGRATION_1_3 = object : Migration(1, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS `pago_trabajador` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `trabajadorNombre` TEXT NOT NULL, `tipoPago` TEXT NOT NULL DEFAULT 'Kilo', `kilos` REAL NOT NULL, `precioPorKilo` REAL NOT NULL, `montoRaya` REAL NOT NULL DEFAULT 0.0, `fecha` INTEGER NOT NULL)")
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE `pago_trabajador` ADD COLUMN `tipoPago` TEXT NOT NULL DEFAULT 'Kilo'")
                db.execSQL("ALTER TABLE `pago_trabajador` ADD COLUMN `montoRaya` REAL NOT NULL DEFAULT 0.0")
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Tabla de nombres de trabajadores guardados
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `trabajadores` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`nombre` TEXT NOT NULL UNIQUE)"
                )
                // Tabla de gastos/descuentos
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `gastos` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`concepto` TEXT NOT NULL, " +
                    "`monto` REAL NOT NULL, " +
                    "`fecha` INTEGER NOT NULL)"
                )
            }
        }

        fun getInstance(context: Context): NuezDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    NuezDatabase::class.java,
                    "nuez.db"
                )
                .addMigrations(MIGRATION_1_3, MIGRATION_2_3, MIGRATION_3_4)
                .build().also { instance = it }
            }
        }
    }
}
