package com.codecollapse.currencyconverter.utils

import java.util.Calendar

fun getDateFromMill(timeInMillis: Long): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timeInMillis
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1  // Month is zero based
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    return "$year-${String.format("%02d", month)}-${String.format("%02d", day)}"
}