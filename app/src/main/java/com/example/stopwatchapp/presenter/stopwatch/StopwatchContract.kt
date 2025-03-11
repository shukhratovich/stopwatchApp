package com.example.stopwatchapp.presenter.stopwatch

import kotlinx.coroutines.flow.StateFlow

interface StopwatchContract {

    interface Intent {
        data object ClickedStart : Intent
        data object ClickedStop : Intent
        data object ClickedReset : Intent
    }

    data class UiState(
        val stateTimerButton: Boolean = false,
        val currentStopWatch: String = "00:00:00.0",
    )

    interface ViewModel {
        val uiState: StateFlow<UiState>
        fun onEventDispatcher(intent: Intent)
    }
}