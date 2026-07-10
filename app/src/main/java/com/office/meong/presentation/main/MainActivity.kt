package com.office.meong.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.office.meong.core.designsystem.theme.MeongTheme
import com.office.meong.core.network.monitor.NetworkMonitor
import com.office.meong.presentation.main.state.rememberMainAppState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var networkMonitor: NetworkMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MeongTheme {
                val appState = rememberMainAppState(networkMonitor = networkMonitor)

                MainContainer(
                    appState = appState,
                )
            }
        }
    }
}