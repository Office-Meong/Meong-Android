package com.office.meong.presentation.main

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.office.meong.presentation.auth.navigation.loginNavGraph
import com.office.meong.presentation.auth.navigation.signupNavGraph
import com.office.meong.presentation.course.navigation.courseNavGraph
import com.office.meong.presentation.explore.navigation.navigation.exploreDetailNavGraph
import com.office.meong.presentation.explore.navigation.navigation.exploreNavGraph
import com.office.meong.presentation.favorite.navigation.favoriteNavGraph
import com.office.meong.presentation.home.navigation.homeNavGraph
import com.office.meong.presentation.main.state.MainAppState
import com.office.meong.presentation.mypage.navigation.myPageNavGraph

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

        signupNavGraph(
            paddingValues = paddingValues
        )

        loginNavGraph(
            paddingValues = paddingValues
        )

        exploreNavGraph(
            paddingValues = paddingValues
        )

        exploreDetailNavGraph(
            paddingValues = paddingValues,
            onBackClick = {}
        )

        favoriteNavGraph(
            paddingValues = paddingValues
        )

        myPageNavGraph(
            paddingValues = paddingValues,
            navController = appState.navController,
            navigateUp = appState::navigateUp
        )
    }
}
