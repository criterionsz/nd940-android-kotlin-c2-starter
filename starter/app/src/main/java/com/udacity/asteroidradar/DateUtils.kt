package com.udacity.asteroidradar

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    fun startDate(): String {
        val formatter =
            SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        val c = Calendar.getInstance()
        return formatter.format(c.time)
    }

    fun endDate(): String {
        val formatter =
            SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        val c = Calendar.getInstance()
        c.add(Calendar.DAY_OF_YEAR, Constants.DEFAULT_END_DATE_DAYS)
        return formatter.format(c.time)
    }
}