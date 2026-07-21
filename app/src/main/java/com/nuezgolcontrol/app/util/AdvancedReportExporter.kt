package com.nuezgolcontrol.app.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.nuezgolcontrol.app.data.Venta
import com.nuezgolcontrol.app.data.PagoTrabajador
import com.nuezgolcontrol.app.data.Gasto
import com.nuezgolcontrol.app.data.Cosecha
import com.nuezgolcontrol.app.util.Formatters
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object AdvancedReportExporter {
    
    fun generarReporteCompleto(
        context: Context,
        ventas: List<Venta>,
        pagos: List<PagoTrabajador>,
        gastos: List<Gasto>,
        cosechas: List<Cosecha>,
        titulo: String = "Reporte Completo"
    ): Uri? {
        return try {
            val csv = StringBuilder()
            
            // Encabezado
            csv.append("REPORTE DE TEMPORADA - $titulo\n")
            csv.append("Generado: ${SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("es", "MX")).format(Date())}\n\n")
            
            // Resumen General
            val totalIngresos = ventas.sumOf { it.total }
            val totalPagos = pagos.sumOf { if (it.tipoPago == "Kilo") it.kilos * it.precioPorKilo else it.montoRaya }
            val totalGastos = gastos.sumOf { it.monto }
            val totalKgsWestern = cosechas.filter { it.tipoNuez.equals("Western", ignoreCase = true) }.sumOf { it.cantidadKg }
            val totalKgsGuicha = cosechas.filter { it.tipoNuez.equals("Guicha", ignoreCase = true) }.sumOf { it.cantidadKg }
            val totalKgsGeneral = cosechas.sumOf { it.cantidadKg }
            
            csv.append("RESUMEN EJECUTIVO DE OPERACION\n")
            csv.append("Total Ingresos,$totalIngresos\n")
            csv.append("Total Pagos a Empleados,$totalPagos\n")
            csv.append("Total Gastos Operativos,$totalGastos\n")
            csv.append("Total Egresos,${totalPagos + totalGastos}\n")
            csv.append("Ganancia Neta,${totalIngresos - (totalPagos + totalGastos)}\n")
            csv.append("Total Cosechado Western (kg),$totalKgsWestern\n")
            csv.append("Total Cosechado Guicha (kg),$totalKgsGuicha\n")
            csv.append("Total Cosechas General (kg),$totalKgsGeneral\n\n")
            
            // Cosechas detalladas
            csv.append("COSECHAS DETALLADAS (REGISTRO DE CAMPO)\n")
            csv.append("Fecha,Tipo Nuez,Cantidad (kg)\n")
            cosechas.forEach { cosecha ->
                csv.append("${Formatters.fecha(cosecha.fechaMillis)},${cosecha.tipoNuez},${cosecha.cantidadKg}\n")
            }
            csv.append("\n")
            
            // Ventas detalladas
            csv.append("VENTAS DETALLADAS\n")
            csv.append("Fecha,Cliente,Tipo Nuez,Cantidad (kg),Precio Unitario,Total\n")
            ventas.forEach { venta ->
                csv.append("${Formatters.fecha(venta.fechaMillis)},${venta.cliente},${venta.tipoNuez},${venta.cantidad},${venta.precioUnitario},${venta.total}\n")
            }
            csv.append("\n")
            
            // Pagos detallados
            csv.append("PAGOS A EMPLEADOS\n")
            csv.append("Fecha,Trabajador,Tipo Pago,Kilos,Precio por Kilo,Monto Raya,Total\n")
            pagos.forEach { pago ->
                val monto = if (pago.tipoPago == "Kilo") pago.kilos * pago.precioPorKilo else pago.montoRaya
                csv.append("${Formatters.fecha(pago.fecha)},${pago.trabajadorNombre},${pago.tipoPago},${pago.kilos},${pago.precioPorKilo},${pago.montoRaya},$monto\n")
            }
            csv.append("\n")
            
            // Gastos detallados
            csv.append("GASTOS OPERATIVOS\n")
            csv.append("Fecha,Concepto,Monto\n")
            gastos.forEach { gasto ->
                csv.append("${Formatters.fecha(gasto.fecha)},${gasto.concepto},${gasto.monto}\n")
            }
            
            // Guardar archivo
            val fileName = "Reporte_Temporada_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}.csv"
            val file = File(context.cacheDir, fileName)
            file.writeText(csv.toString(), Charsets.UTF_8)
            
            // Retornar URI
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    fun compartirReporte(
        context: Context,
        reportUri: Uri
    ) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/csv"
            putExtra(Intent.EXTRA_STREAM, reportUri)
            putExtra(Intent.EXTRA_SUBJECT, "Reporte Financiero NuezGol")
            putExtra(Intent.EXTRA_TEXT, "Adjunto reporte financiero completo")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Compartir reporte"))
    }
}
