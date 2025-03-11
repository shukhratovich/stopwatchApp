package com.example.stopwatchapp.presenter.stopwatch

import android.util.Log
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.stopwatchapp.TimerClock
import com.example.stopwatchapp.utils.DateFormatter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class StopwatchViewModel : ScreenModel, StopwatchContract.ViewModel {
    private val timer = TimerClock()
    override val uiState = MutableStateFlow(StopwatchContract.UiState())

    init {
        timer.onElapsedTimeListener = { time ->
            reduce { it.copy(currentStopWatch = DateFormatter.formatTimer(time)) }
        }
//            timer.elapsedTimeInMillis
//                .map { DateFormatter.format(date = currentStopWatch) }
//                .onEach { time ->
//                reduce { it.copy(currentStopWatch = time) }
//            }.launchIn(screenModelScope)
    }

    private inline fun reduce(block: (StopwatchContract.UiState) -> StopwatchContract.UiState) {
        val old = uiState.value
        val new = block(old)
        uiState.value = new
    }

    override fun onEventDispatcher(intent: StopwatchContract.Intent) {
        when (intent) {
            is StopwatchContract.Intent.ClickedStart -> {
                reduce { it.copy(stateTimerButton = true) }
                Log.d("TTT", "onEventDispatcher: ${uiState.value.stateTimerButton}")
                timer.startTimer()
            }

            is StopwatchContract.Intent.ClickedStop -> {
                reduce { it.copy(stateTimerButton = false) }
                Log.d("TTT", "onEventDispatcher: ${uiState.value.stateTimerButton}")
                timer.pauseTimer()
            }

            is StopwatchContract.Intent.ClickedReset -> {
                reduce { it.copy(currentStopWatch = "00:00:00.0", stateTimerButton = false) }
                timer.resetTimer()
            }
        }
    }
}