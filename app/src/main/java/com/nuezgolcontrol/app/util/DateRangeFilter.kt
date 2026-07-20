package com.nuezgolcontrol.app.util

import java.util.Calendar
import java.util.Date

data class DateRange(
    val startDate: Long,
    val endDate: Long
)

object DateRangeFilter {
    
    fun getTodayRange(): DateRange {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfDay = calendar.timeInMillis
        
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val endOfDay = calendar.timeInMillis - 1
        
        return DateRange(startOfDay, endOfDay)
    }
    
    fun getThisWeekRange(): DateRange {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val startOfWeek = calendar.timeInMillis
        
        calendar.add(Calendar.WEEK_OF_YEAR, 1)
        val endOfWeek = calendar.timeInMillis - 1
        
        return DateRange(startOfWeek, endOfWeek)
    }
    
    fun getThisMonthRange(): DateRange {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val startOfMonth = calendar.timeInMillis
        
        calendar.add(Calendar.MONTH, 1)
        val endOfMonth = calendar.timeInMillis - 1
        
        return DateRange(startOfMonth, endOfMonth)
    }
    
    fun getLastThirtyDaysRange(): DateRange {
        val calendar = Calendar.getInstance()
        val endDate = calendar.timeInMillis
        
        calendar.add(Calendar.DAY_OF_MONTH, -30)
        val startDate = calendar.timeInMillis
        
        return DateRange(startDate, endDate)
    }
    
    fun getAllTimeRange(): DateRange {
        return DateRange(0, Long.MAX_VALUE)
    }
    
    fun isInRange(timestamp: Long, range: DateRange): Boolean {
        return timestamp >= range.startDate && timestamp <= range.endDate
    }
}
