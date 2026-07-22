package com.office.meong.presentation.main

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.office.meong.presentation.course.navigation.courseNavGraph
import com.office.meong.presentation.home.navigation.homeNavGraph
import com.office.meong.presentation.main.state.MainAppState

@Composable
fun MeongNavHost(
    paddingValues: PaddingValues,
    appState: MainAppState,
) {
    NavHost(
        navController = appState.navController,
        startDestination = appState.startDestination,
        enterTransition = {
            EnterTransition.None
        },
        exitTransition = {
            ExitTransition.None
        },
        popEnterTransition = {
            EnterTransition.None
        },
        popExitTransition = {
            ExitTransition.None
        },
    ) {
        homeNavGraph(
            paddingValues = paddingValues
        )

        courseNavGraph(
            paddingValues = paddingValues
        )
    }
}
