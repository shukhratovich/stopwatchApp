package com.example.stopwatchapp

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class TimerClock {

    private var job: Job? = null

    private var startTime: Long = 0
    private var elapsedTimeInMillis: Long = 0
    var onElapsedTimeListener: ((Long) -> Unit)? = null

    fun startTimer() {
        if (job == null) {
            startTime = System.currentTimeMillis() - elapsedTimeInMillis
            job = CoroutineScope(Dispatchers.Default).launch {
                while (isActive) {
                    elapsedTimeInMillis = System.currentTimeMillis() - startTime
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
        pauseTimer()
        elapsedTimeInMillis = 0L
    }
}