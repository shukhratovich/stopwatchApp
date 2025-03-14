package com.example.stopwatchapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cafe.adriel.voyager.navigator.Navigator
import com.example.stopwatchapp.screen.stopwatcher.StopwatchScreen
import com.example.stopwatchapp.timer.TimerClock
import com.example.stopwatchapp.ui.theme.StopwatchAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StopwatchAppTheme {
                Navigator(StopwatchScreen())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}