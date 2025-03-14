package com.example.stopwatchapp.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateFormatter {

//    fun formatTime(format: String = "hh:mm:ss", date: Date = Date()): String {
//        return SimpleDateFormat(format, Locale.getDefault()).format(date)
//    }

    fun formatTimer(time: Long): String {
        return SimpleDateFormat("mm:ss:SS", Locale.getDefault()).format(Date(time))
    }
}
