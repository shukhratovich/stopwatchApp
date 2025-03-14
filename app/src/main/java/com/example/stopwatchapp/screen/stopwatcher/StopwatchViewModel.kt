package com.example.stopwatchapp.screen.stopwatcher

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.collectAsState
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.stopwatchapp.datastore.DataStoreManager
import com.example.stopwatchapp.timer.TimerClock
import com.example.stopwatchapp.utils.DateFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class StopwatchViewModel(context: Context) : ScreenModel, StopwatchContract.ViewModel {
    private val dataStoreManager: DataStoreManager = DataStoreManager(context)
    override val uiState = MutableStateFlow(StopwatchContract.UiState())
    override val stopwatchState = TimerClock.stopwatchState

    init {
        screenModelScope.launch {
            dataStoreManager.timerFlow.collectLatest { elapsedTime ->
                TimerClock.setElapsedTime(elapsedTime)
                stopwatchState.value = DateFormatter.formatTimer(elapsedTime)
            }
        }
        TimerClock.onCurrentTimeListener = { currentTime ->
            reduce { it.copy(currentTime = currentTime) }
        }

//        timer.onElapsedTimeListener = { time ->
//                reduce { it.copy(currentStopWatch = DateFormatter.formatTimer(time)) }
//            stopwatchState.value = DateFormatter.formatTimer(time)
//        }
    }

    private inline fun reduce(block: (StopwatchContract.UiState) -> StopwatchContract.UiState) {
        val old = uiState.value
        val new = block(old)
        uiState.update { new }
    }

    override fun onEventDispatcher(intent: StopwatchContract.Intent) {
        when (intent) {
            is StopwatchContract.Intent.ClickedStart -> {
                reduce { it.copy(isRunning = true) }
                TimerClock.startTimer()
            }

            is StopwatchContract.Intent.ClickedStop -> {
                reduce { it.copy(isRunning = false) }
                TimerClock.pauseTimer()
            }

            is StopwatchContract.Intent.ClickedReset -> {
                reduce { it.copy(isRunning = false, lapsList = emptyList()) }
                TimerClock.resetTimer()
            }

            is StopwatchContract.Intent.ClickedLap -> {
                val newList: MutableList<String> = mutableListOf()
                newList.addAll(uiState.value.lapsList)
                newList.add(stopwatchState.value)
                reduce { it.copy(lapsList = newList) }
            }
        }
    }

    override fun onDispose() {
        super.onDispose()
        runBlocking {
            dataStoreManager.saveData(TimerClock.getElapsedTime())
        }
        TimerClock.onDestroy()
    }
}