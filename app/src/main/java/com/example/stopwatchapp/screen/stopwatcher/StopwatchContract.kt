package com.example.stopwatchapp.screen.stopwatcher

import androidx.compose.runtime.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface StopwatchContract {

    interface Intent {
        data object ClickedStart : Intent
        data object ClickedStop : Intent
        data object ClickedReset : Intent
        data object ClickedLap : Intent
    }

    data class UiState(
        val isRunning: Boolean = false,
        val currentTime: String = "00:00:00",
        val currentStopWatch: String = "00:00:00",
        val lapsList: List<String> = listOf()
    )

    interface ViewModel {
        val stopwatchState: MutableStateFlow<String>
        val uiState: StateFlow<UiState>
        fun onEventDispatcher(intent: Intent)
    }
}