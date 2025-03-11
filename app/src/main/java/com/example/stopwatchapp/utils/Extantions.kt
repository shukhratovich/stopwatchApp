package com.example.stopwatchapp.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Formatter
import java.util.Locale

object DateFormatter {

    fun formatTime(format: String = "hh:mm:ss", date: Date = Date()): String {
        return SimpleDateFormat(format, Locale.getDefault()).format(date)
    }
    fun formatTimer(time:Long):String{
        var seconds = time / 1000
        var hours = 0L
        var minutes = 0L
        if (seconds >= 3600) {
            hours = seconds / 3600
            seconds -= hours * 3600
        }
        if (seconds >= 60) {
            minutes = seconds / 60
            seconds -= minutes * 60
        }
        return Formatter().format("%1\$02d:%2\$02d:%3\$02d", hours, minutes, seconds).toString()
    }
}
