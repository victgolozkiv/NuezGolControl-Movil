package com.nuezgolcontrol.app.util

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.nuezgolcontrol.app.data.Cosecha
import com.nuezgolcontrol.app.data.PagoTrabajador
import com.nuezgolcontrol.app.data.Venta
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ExcelExporter {

    fun exportarVentas(context: Context, ventas: List<Venta>): Intent {
        val stamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val file = ensureExportFile(context, "ventas_exportadas_$stamp.csv")
        file.bufferedWriter(Charsets.UTF_8).use { out ->
            out.write('\uFEFF'.toString()) // BOM para Excel
            out.appendLine("ID,Fecha,Cliente,Tipo de Nuez,Cantidad (kg),Precio (\$/kg),Total (\$)")
            ventas.forEach { v ->
                out.appendLine(
                    listOf(
                        v.id.toString(),
                        csv(Formatters.fecha(v.fechaMillis)),
                        csv(v.cliente),
                        csv(v.tipoNuez),
                        v.cantidad.toString(),
                        v.precioUnitario.toString(),
                        v.total.toString()
                    ).joinToString(",")
                )
            }
        }
        return shareIntent(context, file, "text/csv")
    }

    fun exportarCosechas(context: Context, cosechas: List<Cosecha>): Intent {
        val stamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val file = ensureExportFile(context, "cosechas_exportadas_$stamp.csv")
        file.bufferedWriter(Charsets.UTF_8).use { out ->
            out.write('\uFEFF'.toString())
            out.appendLine("ID,Fecha y Hora,Tipo de Nuez,Cantidad (kg)")
            cosechas.forEach { c ->
                out.appendLine(
                    listOf(
                        c.id.toString(),
                        csv(Formatters.fecha(c.fechaMillis)),
                        csv(c.tipoNuez),
                        c.cantidadKg.toString()
                    ).joinToString(",")
                )
            }
        }
        return shareIntent(context, file, "text/csv")
    }

    fun exportarPagosTrabajadores(context: Context, pagos: List<PagoTrabajador>): Intent {
        val stamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val file = ensureExportFile(context, "pagos_trabajadores_$stamp.csv")
        file.bufferedWriter(Charsets.UTF_8).use { out ->
            out.write('\uFEFF'.toString()) // BOM para Excel
            out.appendLine("ID,Fecha,Trabajador,Modalidad,Kilos,Precio/kg,Raya,Total (\$)")
            pagos.forEach { p ->
                out.appendLine(
                    listOf(
                        p.id.toString(),
                        csv(Formatters.fecha(p.fecha)),
                        csv(p.trabajadorNombre),
                        csv(p.tipoPago),
                        if (p.tipoPago == "Kilo") p.kilos.toString() else "0",
                        if (p.tipoPago == "Kilo") p.precioPorKilo.toString() else "0",
                        if (p.tipoPago == "Raya") p.montoRaya.toString() else "0",
                        p.total.toString()
                    ).joinToString(",")
                )
            }
        }
        return shareIntent(context, file, "text/csv")
    }

    private fun ensureExportFile(context: Context, name: String): File {
        val dir = File(context.cacheDir, "exports").also { it.mkdirs() }
        return File(dir, name)
    }

    private fun shareIntent(context: Context, file: File, mime: String): Intent {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
        return Intent(Intent.ACTION_SEND).apply {
            type = mime
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }

    private fun csv(value: String): String {
        val escaped = value.replace("\"", "\"\"")
        return "\"$escaped\""
    }
}
