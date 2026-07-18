package com.nuezgolcontrol.app.data

class NuezRepository(
    private val ventaDao: VentaDao,
    private val cosechaDao: CosechaDao,
    private val pagoTrabajadorDao: PagoTrabajadorDao,
    private val trabajadorDao: TrabajadorDao,
    private val gastoDao: GastoDao
) {
    // Flows observables
    val ventas = ventaDao.observeAll()
    val cosechas = cosechaDao.observeAll()
    val pagosTrabajadores = pagoTrabajadorDao.observeAll()
    val trabajadores = trabajadorDao.observeAll()
    val gastos = gastoDao.observeAll()

    // --- Ventas ---
    suspend fun agregarVenta(cliente: String, tipoNuez: String, cantidad: Double, precioUnitario: Double) {
        ventaDao.insert(Venta(cliente = cliente.trim(), tipoNuez = tipoNuez.trim(), cantidad = cantidad, precioUnitario = precioUnitario))
    }
    suspend fun eliminarVenta(id: Long) = ventaDao.deleteById(id)
    suspend fun obtenerVentas() = ventaDao.getAll()

    // --- Cosechas ---
    suspend fun agregarCosecha(tipoNuez: String, cantidadKg: Double) {
        cosechaDao.insert(Cosecha(tipoNuez = tipoNuez.trim(), cantidadKg = cantidadKg))
    }
    suspend fun eliminarCosecha(id: Long) = cosechaDao.deleteById(id)
    suspend fun obtenerCosechas() = cosechaDao.getAll()

    // --- Pagos Trabajadores ---
    suspend fun agregarPagoTrabajador(
        trabajadorNombre: String,
        tipoPago: String,
        kilos: Double,
        precioPorKilo: Double,
        montoRaya: Double
    ) {
        val nombre = trabajadorNombre.trim()
        pagoTrabajadorDao.insert(
            PagoTrabajador(trabajadorNombre = nombre, tipoPago = tipoPago, kilos = kilos, precioPorKilo = precioPorKilo, montoRaya = montoRaya)
        )
        // Guardar el nombre automáticamente si es nuevo
        if (nombre.isNotBlank()) {
            trabajadorDao.insert(Trabajador(nombre = nombre))
        }
    }
    suspend fun eliminarPagoTrabajador(id: Long) = pagoTrabajadorDao.deleteById(id)

    // --- Trabajadores (nombres guardados) ---
    suspend fun eliminarTrabajador(nombre: String) = trabajadorDao.deleteByName(nombre)

    // --- Gastos ---
    suspend fun agregarGasto(concepto: String, monto: Double) {
        gastoDao.insert(Gasto(concepto = concepto.trim(), monto = monto))
    }
    suspend fun eliminarGasto(id: Long) = gastoDao.deleteById(id)
}
