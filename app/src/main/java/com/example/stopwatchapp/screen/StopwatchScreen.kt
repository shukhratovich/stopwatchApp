package com.example.stopwatchapp.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.example.stopwatchapp.presenter.stopwatch.StopwatchContract
import com.example.stopwatchapp.presenter.stopwatch.StopwatchViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StopwatchScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel: StopwatchContract.ViewModel = rememberScreenModel { StopwatchViewModel() }
        val uiState = viewModel.uiState.collectAsState()
        val time = viewModel

        StopwatchScreenContent(uiState = uiState, onEventDispatcher = viewModel::onEventDispatcher)
    }
}

@Composable
private fun StopwatchScreenContent(
    modifier: Modifier = Modifier,
    uiState: State<StopwatchContract.UiState> = remember { mutableStateOf(StopwatchContract.UiState()) },
    onEventDispatcher: (StopwatchContract.Intent) -> Unit = {}
) {

    var currentTime by remember {
        mutableStateOf(
            SimpleDateFormat(
                "hh:mm:ss",
                Locale.getDefault()
            ).format(Date())
        )
    }
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            currentTime = SimpleDateFormat("hh:mm:ss", Locale.getDefault()).format(Date())
        }
    }
    Scaffold { innerPadding ->
        Column(
            modifier
                .padding(innerPadding)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(text = currentTime)
                    Text(text = uiState.value.currentStopWatch)
                    Row {
                        Button(onClick = {
                            if (uiState.value.stateTimerButton) {
                                onEventDispatcher(StopwatchContract.Intent.ClickedStop)
                            } else onEventDispatcher(StopwatchContract.Intent.ClickedStart)
                        }) { if (uiState.value.stateTimerButton) Text("Stop") else Text("Start") }
                        Button(onClick = {
                            onEventDispatcher(StopwatchContract.Intent.ClickedReset)
                        }) {
                            Text("Reset")
                        }
                    }
                }
            }
        }
    }

//    fun getCurrentTime(): String {
//        return SimpleDateFormat("hh:mm:ss", Locale.getDefault()).format(Date())
//    }
}

@Preview
@Composable
private fun StopwatchScreenPreview(modifier: Modifier = Modifier) {
    StopwatchScreenContent()
}

