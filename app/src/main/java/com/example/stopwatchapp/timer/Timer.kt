package com.example.stopwatchapp.timer

import android.util.Log
import com.example.stopwatchapp.utils.DateFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object TimerClock {
    private var clocksJob: Job? = null

    init {
        if (clocksJob == null) {
            clocksJob = CoroutineScope(Dispatchers.Default).launch {
                while (isActive) {
                    currentTime = SimpleDateFormat("HH:mm:ss a", Locale.getDefault()).format(Date())
                    onCurrentTimeListener?.invoke(currentTime)
                }
            }
        }

    }

    private var job: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)
    private var currentTime: String = ""

    private var startTime: Long = 0L
    private var elapsedTimeInMillis: Long = 0L

    var stopwatchState = MutableStateFlow("00:00:00")

    var onElapsedTimeListener: ((Long) -> Unit)? = null
    var onCurrentTimeListener: ((String) -> Unit)? = null


    fun startTimer() {
        if (job == null) {
            job = scope.launch {
                startTime = System.currentTimeMillis() - elapsedTimeInMillis
                while (isActive) {
                    elapsedTimeInMillis = System.currentTimeMillis() - startTime
                    stopwatchState.value = DateFormatter.formatTimer(elapsedTimeInMillis)
                    onElapsedTimeListener?.invoke(elapsedTimeInMillis)
                }
            }
        }
    }


    fun pauseTimer() {
        job?.cancel()
        job = null
    }

    fun resetTimer() {
        if (job == null) {
            stopwatchState.update { "00:00:00" }
        } else {
            job?.invokeOnCompletion {
                stopwatchState.update { "00:00:00" }
            }
        }
        pauseTimer()
        startTime = 0L
        elapsedTimeInMillis = 0L
    }

    fun setElapsedTime(time: Long) {
        elapsedTimeInMillis = time
    }

    fun getElapsedTime(): Long {
        return elapsedTimeInMillis
    }

    fun onDestroy() {
        Log.d("TTT", "onDestroy: timer destroyed")
        onElapsedTimeListener = null
        onCurrentTimeListener = null
        clocksJob?.cancel()
        clocksJob = null
    }
}