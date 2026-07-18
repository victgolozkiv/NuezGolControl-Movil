package com.nuezgolcontrol.app.util

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object Formatters {
    private val localeMx = Locale("es", "MX")
    private val chihuahua = TimeZone.getTimeZone("America/Chihuahua")

    private val dateTimeFormat = SimpleDateFormat("dd/MM/yyyy hh:mm a", localeMx).apply {
        timeZone = chihuahua
    }

    private val currencyFormat = NumberFormat.getCurrencyInstance(localeMx)
    private val numberFormat = NumberFormat.getNumberInstance(localeMx).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }

    fun fecha(millis: Long): String = dateTimeFormat.format(Date(millis))

    fun dinero(valor: Double): String = currencyFormat.format(valor)

    fun numero(valor: Double): String = numberFormat.format(valor)

    fun kg(valor: Double): String = "${numero(valor)} kg"
}
