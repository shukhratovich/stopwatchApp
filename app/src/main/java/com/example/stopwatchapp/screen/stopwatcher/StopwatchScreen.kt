package com.example.stopwatchapp.screen.stopwatcher

import android.view.SoundEffectConstants
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.example.stopwatchapp.R

class StopwatchScreen : Screen {
    @Composable
    override fun Content() {
        val context = LocalContext.current.applicationContext
        val viewModel: StopwatchContract.ViewModel =
            rememberScreenModel { StopwatchViewModel(context) }
        val uiState by viewModel.uiState.collectAsState()
        val stopwatchState by viewModel.stopwatchState.collectAsState()

        StopwatchScreenContent(
            stopwatchState = stopwatchState,
            uiState = uiState,
            onEventDispatcher = viewModel::onEventDispatcher
        )
    }
}

@Composable
private fun StopwatchScreenContent(
    stopwatchState: String,
    uiState: StopwatchContract.UiState,
    onEventDispatcher: (StopwatchContract.Intent) -> Unit
) {
    val listState = rememberLazyListState()
    LaunchedEffect(uiState.lapsList) {
        listState.animateScrollToItem(listState.layoutInfo.totalItemsCount)
    }
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .animateContentSize()
                    .clip(RoundedCornerShape(50.dp))
                    .background(color = Color(0xFF00BCD4))
                    .padding(24.dp)

            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = uiState.currentTime, fontSize = 42.sp)
                    Text(text = stopwatchState, fontSize = 72.sp)
                }
            }
            if (uiState.lapsList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .animateContentSize()
                ) {
                    Image(
                        modifier = Modifier.align(Alignment.Center),
                        painter = painterResource(R.drawable.timer),
                        contentDescription = "nothing"
                    )
                }
            } else {
                LazyColumn(
                    Modifier
                        .fillMaxWidth()
                        .animateContentSize()
                        .height(400.dp),
                    state = listState
                ) {
                    items(uiState.lapsList.size) { index ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp)
                        ) {
                            Text(
                                modifier = Modifier.align(Alignment.CenterStart),
                                text = "Lap ${index + 1}",
                                fontSize = 24.sp
                            )
                            Text(
                                modifier = Modifier.align(Alignment.CenterEnd),
                                text = uiState.lapsList[index],
                                fontSize = 24.sp
                            )
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MyCustomButton(
                    text = if (uiState.isRunning) {
                        "Stop"
                    } else {
                        "Start"
                    },
                    color = if (uiState.isRunning) {
                        Color(0xFFFF5722)
                    } else {
                        Color(0xFF8BC34A)
                    },
                    modifier = Modifier,
                    onClick = {
                        if (uiState.isRunning) {
                            onEventDispatcher(StopwatchContract.Intent.ClickedStop)
                        } else {
                            onEventDispatcher(StopwatchContract.Intent.ClickedStart)
                        }
                    }
                )
                MyCustomButton(
                    text = if (uiState.isRunning) {
                        "Lap"
                    } else {
                        "Restart"
                    },
                    onClick = {
                        if (uiState.isRunning) {
                            onEventDispatcher(StopwatchContract.Intent.ClickedLap)
                        } else {
                            onEventDispatcher(StopwatchContract.Intent.ClickedReset)
                        }
                    },
                    color = if (uiState.isRunning) {
                        Color(0xFFFFC107)
                    } else {
                        Color(0xFFFFEB3B)
                    }
                )
            }
        }
    }

}

@Preview
@Composable
private fun StopwatchScreenPreview(modifier: Modifier = Modifier) {
    StopwatchScreenContent(
        stopwatchState = "",
        uiState = StopwatchContract.UiState(),
        onEventDispatcher = { }
    )
}


@Composable
fun MyCustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color
) {
    val view = LocalView.current
    Box(
        modifier = modifier
            .animateContentSize()
            .size(100.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(color = color)
            .clickable {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                onClick()
            }
    ) {
        Text(text = text, modifier = Modifier.align(alignment = Alignment.Center))
    }
}

