package com.nuezgolcontrol.app.util

import com.nuezgolcontrol.app.data.Venta
import com.nuezgolcontrol.app.data.PagoTrabajador
import com.nuezgolcontrol.app.data.Gasto

object SearchAndFilterUtils {
    
    fun filtrarVentas(
        ventas: List<Venta>,
        searchQuery: String = "",
        tipoNuez: String? = null,
        minAmount: Double = 0.0,
        maxAmount: Double = Double.MAX_VALUE
    ): List<Venta> {
        return ventas.filter { venta ->
            val matchSearch = if (searchQuery.isNotBlank()) {
                venta.cliente.contains(searchQuery, ignoreCase = true) ||
                venta.tipoNuez.contains(searchQuery, ignoreCase = true)
            } else {
                true
            }
            
            val matchTipo = tipoNuez?.let { venta.tipoNuez == it } ?: true
            val matchAmount = venta.total in minAmount..maxAmount
            
            matchSearch && matchTipo && matchAmount
        }
    }
    
    fun filtrarPagos(
        pagos: List<PagoTrabajador>,
        searchQuery: String = "",
        tipoPago: String? = null,
        minAmount: Double = 0.0,
        maxAmount: Double = Double.MAX_VALUE
    ): List<PagoTrabajador> {
        return pagos.filter { pago ->
            val matchSearch = if (searchQuery.isNotBlank()) {
                pago.trabajadorNombre.contains(searchQuery, ignoreCase = true)
            } else {
                true
            }
            
            val matchTipo = tipoPago?.let { pago.tipoPago == it } ?: true
            val montoTotal = if (pago.tipoPago == "Kilo") pago.kilos * pago.precioPorKilo else pago.montoRaya
            val matchAmount = montoTotal in minAmount..maxAmount
            
            matchSearch && matchTipo && matchAmount
        }
    }
    
    fun filtrarGastos(
        gastos: List<Gasto>,
        searchQuery: String = "",
        minAmount: Double = 0.0,
        maxAmount: Double = Double.MAX_VALUE
    ): List<Gasto> {
        return gastos.filter { gasto ->
            val matchSearch = if (searchQuery.isNotBlank()) {
                gasto.concepto.contains(searchQuery, ignoreCase = true)
            } else {
                true
            }
            
            val matchAmount = gasto.monto in minAmount..maxAmount
            
            matchSearch && matchAmount
        }
    }
    
    fun obtenerTiposNuezUnicos(ventas: List<Venta>): List<String> {
        return ventas.map { it.tipoNuez }.distinct().sorted()
    }
    
    fun obtenerTrabajadoresUnicos(pagos: List<PagoTrabajador>): List<String> {
        return pagos.map { it.trabajadorNombre }.distinct().sorted()
    }
}
