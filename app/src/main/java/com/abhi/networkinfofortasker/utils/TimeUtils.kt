package com.abhi.networkinfofortasker.utils

import java.util.Calendar
import java.util.Date

object TimeUtils {
    private fun getStartTimeForDay(date: Date): Long {
        val cal: Calendar = Calendar.getInstance()
        cal.time = date
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        return cal.timeInMillis
    }

    private fun getFirstDayOfCurrentMonth(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        return calendar.time
    }

    fun getStartTimeForThisMonth(): Long {
        return getStartTimeForDay(getFirstDayOfCurrentMonth())
    }
    fun getStartTimeForToday():Long{
        return getStartTimeForDay(Calendar.getInstance().time)
    }
}